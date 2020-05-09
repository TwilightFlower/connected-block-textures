package io.github.nuclearfarts.cbt.tile.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import io.github.nuclearfarts.cbt.tile.ImageBackedTile;
import io.github.nuclearfarts.cbt.tile.ResourceBackedTile;
import io.github.nuclearfarts.cbt.tile.Tile;
import io.github.nuclearfarts.cbt.util.CBTUtil;

public class DynamicBookshelfTileLoader implements TileLoader {
	Tile[] tiles = new Tile[4];
	
	public DynamicBookshelfTileLoader(Properties properties, Identifier location, ResourceManager manager) throws IOException {
		Identifier textureLocation = new Identifier(properties.getProperty("cbt_special_bookshelf_texture"));
		BufferedImage bookshelf = ImageIO.read(manager.getResource(textureLocation).getInputStream());
		tiles[3] = new ResourceBackedTile(textureLocation, manager);
		
		BufferedImage work;
		work = CBTUtil.copy(bookshelf);
		copyVLine(work, 4, 1, 6, 15, 9);
		copyVLine(work, 8, 9, 6, 15, 1);
		tiles[0] = new ImageBackedTile(work);
		work = CBTUtil.copy(work);
		copyVLine(work, 5, 1, 6, 0, 9);
		copyVLine(work, 9, 9, 6, 0, 1);
		tiles[1] = new ImageBackedTile(work);
		work = CBTUtil.copy(bookshelf);
		copyVLine(work, 5, 1, 6, 0, 9);
		copyVLine(work, 9, 9, 6, 0, 1);
		tiles[2] = new ImageBackedTile(work);
	}

	@Override
	public Tile[] getTiles() {
		return tiles;
	}

	private void copyVLine(BufferedImage on, int srcX, int srcY, int height, int dstX, int dstY) {
		for(int i = 0; i < height; i++) {
			on.setRGB(dstX, dstY + i, on.getRGB(srcX, srcY + i));
		}
	}
}
