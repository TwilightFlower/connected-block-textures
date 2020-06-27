package io.github.nuclearfarts.cbt.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.Identifier;

import io.github.nuclearfarts.cbt.ConnectedBlockTextures;

@Mixin(Identifier.class)
public class IdentifierMixin {
	@Inject(method = "isPathValid(Ljava/lang/String;)Z", at = @At("HEAD"), cancellable = true)
	private static void overrideIdentifierRestrictions(CallbackInfoReturnable<Boolean> callback) {
		if(ConnectedBlockTextures.overrideIdentifierCharRestriction && Thread.currentThread() == ConnectedBlockTextures.identifierOverrideThread) {
			callback.setReturnValue(true);
		}
	}
}
