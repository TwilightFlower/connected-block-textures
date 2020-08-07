package io.github.nuclearfarts.cbt.tile;

import java.io.IOException;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;

public interface Tile {
	boolean hasResource();
	/**
	 * @throws UnsupportedOperationException if this is a dynamically generated tile.
	 * */
	Identifier getResource();
	NativeImage getImage() throws IOException;
}
