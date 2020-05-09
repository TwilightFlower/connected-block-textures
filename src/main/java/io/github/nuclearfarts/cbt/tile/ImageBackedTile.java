package io.github.nuclearfarts.cbt.tile;

import java.awt.image.BufferedImage;
import net.minecraft.util.Identifier;

public class ImageBackedTile implements Tile {
	private final BufferedImage image;
	
	public ImageBackedTile(BufferedImage image) {
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
	public BufferedImage getImage() {
		return image;
	}

}
