package io.github.nuclearfarts.cbt.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.world.BiomeColorCache;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.level.ColorResolver;

import io.github.nuclearfarts.cbt.ColorCacheHack;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
	@Shadow
	private @Final Object2ObjectArrayMap<ColorResolver, BiomeColorCache> colorCache;
	
	@Inject(method = "<init>", at = @At("RETURN"))
	private void cachePls(CallbackInfo callback) {
		colorCache.defaultReturnValue(new ColorCacheHack());
	}
}
