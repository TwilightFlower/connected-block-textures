package io.github.nuclearfarts.cbt.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import io.github.nuclearfarts.cbt.config.CTMConfig;
import io.github.nuclearfarts.cbt.sprite.SpriteProvider;

public class CBTUnbakedModel implements UnbakedModel {

	private final UnbakedModel baseModel;
	private final CTMConfig[] configs;

	public CBTUnbakedModel(UnbakedModel baseModel, CTMConfig[] configs) {
		this.baseModel = baseModel;
		this.configs = configs;
	}

	@Override
	public Collection<Identifier> getModelDependencies() {
		return Collections.emptySet();
	}

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		Collection<SpriteIdentifier> baseDeps = baseModel.getTextureDependencies(unbakedModelGetter, unresolvedTextureReferences);
		Collection<SpriteIdentifier> allDeps = new ArrayList<SpriteIdentifier>(baseDeps);
		for(CTMConfig config : configs) {
			allDeps.addAll(config.getTileProvider().getIdsToLoad());
		}
		return allDeps;
	}

	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		try {
			List<SpriteProvider> spriteProviders = new ArrayList<>();
			for(CTMConfig config : configs) {
				spriteProviders.add(config.createSpriteProvider(config.getTileProvider().load(textureGetter)));
			}
			return new CBTBakedModel(baseModel.bake(loader, textureGetter, rotationContainer, modelId), spriteProviders.toArray(new SpriteProvider[spriteProviders.size()]));
			//return config.createBakedModel(sprites, baseModel.bake(loader, textureGetter, rotationContainer, modelId));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
