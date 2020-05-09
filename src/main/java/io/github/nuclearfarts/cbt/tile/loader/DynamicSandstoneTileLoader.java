package io.github.nuclearfarts.cbt.tile.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import io.github.nuclearfarts.cbt.tile.ImageBackedTile;
import io.github.nuclearfarts.cbt.tile.Tile;

public class DynamicSandstoneTileLoader implements TileLoader {
	private Tile[] tiles = new Tile[1];
	
	public DynamicSandstoneTileLoader(Properties properties, Identifier location, ResourceManager manager) throws IOException {
		BufferedImage sandstone = ImageIO.read(manager.getResource(new Identifier(properties.getProperty("cbt_special_sandstone_texture"))).getInputStream());
		for(int x = 0; x < 16; x++) {
			for(int y = 0; y < 3; y++) {
				sandstone.setRGB(15 - x, y, sandstone.getRGB(x, y + 12));
			}
		}
		tiles[0] = new ImageBackedTile(sandstone);
	}

	@Override
	public Tile[] getTiles() {
		return tiles;
	}

}
