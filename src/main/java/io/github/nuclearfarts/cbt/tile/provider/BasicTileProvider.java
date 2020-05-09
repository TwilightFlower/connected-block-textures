package io.github.nuclearfarts.cbt.tile.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;

import io.github.nuclearfarts.cbt.ConnectedBlockTextures;
import io.github.nuclearfarts.cbt.tile.Tile;

public class BasicTileProvider implements TileProvider {
	private final List<SpriteIdentifier> ids = new ArrayList<>();
	
	public BasicTileProvider(Tile[] tiles) {
		for(Tile tile : tiles) {
			ids.add(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEX, ConnectedBlockTextures.resourcePack.dynamicallyPutTile(tile)));
		}
	}

	@Override
	public List<SpriteIdentifier> getIdsToLoad() {
		//System.out.println(ids);
		return ids;
	}

	@Override
	public Sprite[] load(Function<SpriteIdentifier, Sprite> textureGetter) {
		Sprite[] sprites = new Sprite[ids.size()];
		for(int i = 0; i < ids.size(); i++) {
			sprites[i] = textureGetter.apply(ids.get(i));
		}
		return sprites;
	}

}
