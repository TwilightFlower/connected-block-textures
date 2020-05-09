package io.github.nuclearfarts.cbt.mixin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceReloadMonitor;
import net.minecraft.util.Unit;

import io.github.nuclearfarts.cbt.ConnectedBlockTextures;
import io.github.nuclearfarts.cbt.resource.CBTResourcePack;

@Mixin(ReloadableResourceManagerImpl.class)
public abstract class ReloadableResourceManagerImplMixin implements ReloadableResourceManager {
	@Inject(method = "beginMonitoredReload", at = @At("HEAD"))
	private void injectCBTPack(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<ResourcePack> packs, CallbackInfoReturnable<ResourceReloadMonitor> cir) {
		ConnectedBlockTextures.RESOURCE_PACK_PRIORITY_MAP.clear();
		for(int i = 0; i < packs.size(); i++) {
			System.out.println(i + " " + packs.get(i).getName());
			ConnectedBlockTextures.RESOURCE_PACK_PRIORITY_MAP.put(packs.get(i).getName(), i);
		}
		System.out.println("sans undertale");
		packs.add(ConnectedBlockTextures.resourcePack = new CBTResourcePack(this));
	}

}
