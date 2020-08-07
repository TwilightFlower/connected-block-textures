package io.github.nuclearfarts.cbt.tile.loader;

import java.io.IOException;
import java.util.Properties;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import io.github.nuclearfarts.cbt.tile.ImageBackedTile;
import io.github.nuclearfarts.cbt.tile.Tile;
import io.github.nuclearfarts.cbt.util.CBTUtil;

public class DynamicGlassTileLoader implements TileLoader {
	private final Tile[] tiles = new Tile[5];
	
	public DynamicGlassTileLoader(Properties properties, Identifier location, ResourceManager manager) throws IOException {
		NativeImage glass = NativeImage.read(manager.getResource(new Identifier(properties.getProperty("cbt_special_glass_texture"))).getInputStream());
		int background = CBTUtil.toABGR(Integer.parseUnsignedInt(properties.getProperty("cbt_special_glass_background"), 16));
		tiles[0] = new ImageBackedTile(glass); //all-borders
		
		NativeImage work;
		work = CBTUtil.copy(glass);
		hLine(work, 0, 0, 16, background);
		hLine(work, 0, 15, 16, background);
		vLine(work, 0, 1, 14, background);
		vLine(work, 15, 1, 14, background);
		tiles[1] = new ImageBackedTile(work);
		
		work = CBTUtil.copy(glass);
		hLine(work, 1, 0, 14, background);
		hLine(work, 1, 15, 14, background);
		tiles[2] = new ImageBackedTile(work);
		
		work = CBTUtil.copy(glass);
		vLine(work, 0, 1, 14, background);
		vLine(work, 15, 1, 14, background);
		tiles[3] = new ImageBackedTile(work);
		
		work = CBTUtil.copy(glass);
		hLine(work, 1, 0, 14, background);
		hLine(work, 1, 15, 14, background);
		vLine(work, 0, 1, 14, background);
		vLine(work, 15, 1, 14, background);
		tiles[4] = new ImageBackedTile(work);
	}

	@Override
	public Tile[] getTiles() {
		return tiles;
	}

	private static void hLine(NativeImage image, int xStart, int y, int length, int color) {
		for(int x = 0; x < length; x++) {
			image.setPixelColor(x + xStart, y, color);
		}
	}
	
	private static void vLine(NativeImage image, int x, int yStart, int length, int color) {
		for(int y = 0; y < length; y++) {
			image.setPixelColor(x, y + yStart, color);
		}
	}
}
