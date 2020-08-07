package io.github.nuclearfarts.cbt.tile;

import java.io.IOException;

import net.minecraft.client.texture.NativeImage;
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
	public NativeImage getImage() throws IOException {
		return NativeImage.read(resourceManager.getResource(resource).getInputStream());
	}

}
