package io.github.nuclearfarts.cbt.tile.provider;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import io.github.nuclearfarts.cbt.ConnectedBlockTextures;
import io.github.nuclearfarts.cbt.tile.Tile;

public class CompactTileProvider implements TileProvider {
	private final List<SpriteIdentifier> spriteIds = new ArrayList<>();
	
	public CompactTileProvider(Tile[] origTiles) throws IOException {
		BufferedImage[] origImages = new BufferedImage[5];
		for(int i = 0; i < 5; i++) {
			origImages[i] = origTiles[i].getImage();
		}
		for(int i = 0; i <= 46; i++) {
			BufferedImage tile = new BufferedImage(origImages[0].getWidth(), origImages[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
			short bithack = ConnectedBlockTextures.CTM_TO_IDEALIZED_BITHACK[i];
			//Up-left. up-left-upleft
			
			if((bithack & 0b1010000) == 0) {
				//draw both.
				blitQuarter(0, 0, origImages[0], tile);
			} else if((bithack & 0b10000) == 0) {
				//draw left
				blitQuarter(0, 0, origImages[2], tile);
			} else if((bithack & 0b1000000) == 0) {
				//draw up
				blitQuarter(0, 0, origImages[3], tile);
			} else if((bithack & 0b10000000) == 0) {
				//draw corner
				blitQuarter(0, 0, origImages[4], tile);
			} else {
				//empty
				blitQuarter(0, 0, origImages[1], tile);
			}
			
			//up-right.
			if((bithack & 0b1001000) == 0) {
				//draw both.
				blitQuarter(1, 0, origImages[0], tile);
			} else if((bithack & 0b1000) == 0) {
				//draw right
				blitQuarter(1, 0, origImages[2], tile);
			} else if((bithack & 0b1000000) == 0) {
				//draw up
				blitQuarter(1, 0, origImages[3], tile);
			} else if((bithack & 0b100000) == 0) {
				//draw corner
				blitQuarter(1, 0, origImages[4], tile);
			} else {
				//empty
				blitQuarter(1, 0, origImages[1], tile);
			}
			
			//down-left
			if((bithack & 0b10010) == 0) {
				//draw both.
				blitQuarter(0, 1, origImages[0], tile);
			} else if((bithack & 0b10000) == 0) {
				//draw left
				blitQuarter(0, 1, origImages[2], tile);
			} else if((bithack & 0b10) == 0) {
				//draw down
				blitQuarter(0, 1, origImages[3], tile);
			} else if((bithack & 0b100) == 0) {
				//draw corner
				blitQuarter(0, 1, origImages[4], tile);
			} else {
				//empty
				blitQuarter(0, 1, origImages[1], tile);
			}
			
			//down-right
			if((bithack & 0b1010) == 0) {
				//draw both.
				blitQuarter(1, 1, origImages[0], tile);
			} else if((bithack & 0b1000) == 0) {
				//draw right
				blitQuarter(1, 1, origImages[2], tile);
			} else if((bithack & 0b10) == 0) {
				//draw down
				blitQuarter(1, 1, origImages[3], tile);
			} else if((bithack & 0b1) == 0) {
				//draw corner
				blitQuarter(1, 1, origImages[4], tile);
			} else {
				//empty
				blitQuarter(1, 1, origImages[1], tile);
			}
			
			spriteIds.add(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEX, ConnectedBlockTextures.resourcePack.dynamicallyPutImage(tile)));
		}
	}
	
	private static void blitQuarter(int offsetX, int offsetY, BufferedImage src, BufferedImage dst) {
		int w = src.getWidth() / 2;
		int h = src.getHeight() / 2;
		int x = w * offsetX;
		int y = h * offsetY;
		for(int i = x; i < w + x; i++) {
			for(int j = y; j < h + y; j++) {
				dst.setRGB(i, j, src.getRGB(i, j));
			}
		}
	}

	@Override
	public List<SpriteIdentifier> getIdsToLoad() {
		return spriteIds;
	}

	@Override
	public Sprite[] load(Function<SpriteIdentifier, Sprite> textureGetter) {
		Sprite[] sprites = new Sprite[spriteIds.size()];
		for(int i = 0; i < sprites.length; i++) {
			sprites[i] = textureGetter.apply(spriteIds.get(i));
		}
		return sprites;
	}

}
