package io.github.nuclearfarts.cbt.tile;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class ResourceBackedTile implements Tile {
	private final Identifier resource;
	private final ResourceManager resourceManager;
	
	public ResourceBackedTile(Identifier resource, ResourceManager resourceManager) {
		this.resource = resource;
		this.resourceManager = resourceManager;
	}

	@Override
	public boolean hasResource() {
		return true;
	}

	@Override
	public Identifier getResource() {
		return resource;
	}

	@Override
	public BufferedImage getImage() throws IOException {
		return ImageIO.read(resourceManager.getResource(resource).getInputStream());
	}

}
