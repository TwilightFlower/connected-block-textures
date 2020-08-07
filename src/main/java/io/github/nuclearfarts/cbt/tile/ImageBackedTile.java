package io.github.nuclearfarts.cbt.tile;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;

public class ImageBackedTile implements Tile {
	private final NativeImage image;
	
	public ImageBackedTile(NativeImage image) {
		this.image = image;
	}

	@Override
	public boolean hasResource() {
		return false;
	}

	@Override
	public Identifier getResource() {
		throw new UnsupportedOperationException("Tile has no underlying resource!");
	}

	@Override
	public NativeImage getImage() {
		return image;
	}

}
