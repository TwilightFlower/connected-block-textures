package io.github.nuclearfarts.cbt.mixin;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import io.github.nuclearfarts.cbt.ConnectedBlockTextures;
import io.github.nuclearfarts.cbt.config.CTMConfig;
import io.github.nuclearfarts.cbt.model.CBTUnbakedModel;
import io.github.nuclearfarts.cbt.util.CBTUtil;
import io.github.nuclearfarts.cbt.util.VoidSet;
import io.github.nuclearfarts.cbt.util.function.MutableCachingSupplier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
	@Shadow
	private @Final Map<Identifier, UnbakedModel> unbakedModels;
	@Shadow
	private @Final Map<Identifier, UnbakedModel> modelsToBake;
	@Shadow
	private @Final ResourceManager resourceManager;

	@Redirect(method = "<init>", at = @At(value = "INVOKE_STRING", target = "net/minecraft/util/profiler/Profiler.swap(Ljava/lang/String;)V", args = "ldc=textures"))
	private void injectCbtModels(Profiler on, String str) {
		on.swap("ctm");
		ConnectedBlockTextures.overrideIdentifierCharRestriction = true;
		ConnectedBlockTextures.identifierOverrideThread = Thread.currentThread();
		List<CTMConfig> data = new ArrayList<>();
		Collection<Identifier> propertiesIds = resourceManager.findResources("optifine/ctm", s -> s.endsWith(".properties"));
		for(Identifier id : propertiesIds) {
			try {
				data.add(CTMConfig.load(id, resourceManager));
			} catch(Exception e) {
				ConnectedBlockTextures.LOGGER.error("Error loading connected textures config at " + id, e);
			}
		}
		
		Set<Identifier> priorityFails = new HashSet<>();
		MutableCachingSupplier<Collection<SpriteIdentifier>> textureCached = new MutableCachingSupplier<>();
		unbakedModels.forEach((id, model) -> {
			if(id instanceof ModelIdentifier) {
				ModelIdentifier modelId = (ModelIdentifier) id;
				if(!modelId.getVariant().equals("inventory")) {
					UnbakedModel newModel = checkCtmConfigs(modelId, model, textureCached, data, priorityFails);
					if(newModel != model) {
						unbakedModels.put(id, newModel);
						modelsToBake.put(id, newModel);
					}
				}
			}
		});
		ConnectedBlockTextures.overrideIdentifierCharRestriction = false;
		on.swap(str);
	}
	
	@Unique
	private UnbakedModel checkCtmConfigs(ModelIdentifier id, UnbakedModel model, MutableCachingSupplier<Collection<SpriteIdentifier>> textureCached, List<CTMConfig> data, Set<Identifier> priorityFails) {
		Set<CTMConfig> configs = new TreeSet<>();
		boolean foundNewConfigs = false;
		do {
			SortedSet<CTMConfig> newConfigs = new TreeSet<>();
			UnbakedModel javaPls = model;
			textureCached.set(() -> javaPls.getTextureDependencies(((ModelLoader) (Object) this)::getOrLoadModel, VoidSet.get()));
			for(CTMConfig c : data) {
				if(c.affectsModel(id, textureCached) && CBTUtil.allMatchThrowable(textureCached.get(), s -> checkPack(s, c.getResourcePackPriority(), priorityFails))) {
					if(configs.add(c)) {
						newConfigs.add(c);
					}
				}
			}
			foundNewConfigs = !newConfigs.isEmpty();
			if(foundNewConfigs) {
				model = new CBTUnbakedModel(model, newConfigs.toArray(new CTMConfig[newConfigs.size()]));
			}
		} while(foundNewConfigs);
		return model;
	}

	@Unique
	private boolean checkPack(SpriteIdentifier spriteId, int ctmPackPriority, Set<Identifier> fails) {
		Identifier texId = spriteId.getTextureId();
		String spritePack;
		try {
			spritePack = resourceManager.getResource(new Identifier(texId.getNamespace(), "textures/" + texId.getPath() + ".png")).getResourcePackName();
		} catch(IOException e) {
			if(fails.add(texId)) {
				ConnectedBlockTextures.LOGGER.error("Error checking resource pack priority", e);
			}
			return true;
		}
		return ConnectedBlockTextures.RESOURCE_PACK_PRIORITY_MAP.getInt(spritePack) <= ctmPackPriority;
	}
}