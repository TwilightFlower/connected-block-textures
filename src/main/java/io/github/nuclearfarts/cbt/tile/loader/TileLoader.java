package io.github.nuclearfarts.cbt.tile.loader;

import java.io.IOException;
import java.util.Properties;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import io.github.nuclearfarts.cbt.tile.Tile;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public interface TileLoader {
	Tile[] getTiles();
	
	static void registerSpecialTileLoader(String name, Loader<?> loader) {
		PrivateConstants.SPECIAL_TILE_LOADERS.put(name, loader);
	}
	
	static TileLoader load(Properties properties, Identifier location, ResourceManager manager) throws IOException {
		return PrivateConstants.SPECIAL_TILE_LOADERS.get(properties.getProperty("tiles")).load(properties, location, manager);
	}
	
	public interface Loader<T extends TileLoader> {
		T load(Properties properties, Identifier location, ResourceManager manager) throws IOException;
	}
	
	public static final class PrivateConstants {
		private PrivateConstants() { }
		private static final Object2ObjectMap<String, Loader<?>> SPECIAL_TILE_LOADERS = new Object2ObjectOpenHashMap<>();
		
		static {
			SPECIAL_TILE_LOADERS.defaultReturnValue(BasicTileLoader::new);
		}
	}
}
