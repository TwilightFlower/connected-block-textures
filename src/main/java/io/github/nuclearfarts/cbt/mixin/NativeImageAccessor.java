package io.github.nuclearfarts.cbt.mixin;

import java.nio.channels.WritableByteChannel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.texture.NativeImage;

@Mixin(NativeImage.class)
public interface NativeImageAccessor {
	@Invoker
	boolean invokeWrite(WritableByteChannel channel);
}
