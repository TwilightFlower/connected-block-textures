package io.github.nuclearfarts.cbt.tile.provider;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import io.github.nuclearfarts.cbt.tile.Tile;
import io.github.nuclearfarts.cbt.tile.loader.TileLoader;
import io.github.nuclearfarts.cbt.util.CBTUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public interface TileProvider {
	List<SpriteIdentifier> getIdsToLoad();
	Sprite[] load(Function<SpriteIdentifier, Sprite> textureGetter);
	
	static TileProvider load(Properties properties, Identifier location, ResourceManager manager) throws IOException {
		return PrivateConstants.TILE_PROVIDERS.get(properties.getProperty("method")).create(TileLoader.load(properties, location, manager).getTiles());
	}
	
	static TileProvider load(Identifier propertiesLocation, ResourceManager manager) throws IOException {
		Properties properties = new Properties();
		properties.load(manager.getResource(propertiesLocation).getInputStream());
		return load(properties, CBTUtil.directoryOf(propertiesLocation), manager);
	}
	
	static void registerTileProviderFactory(String method, Factory<?> loader) {
		PrivateConstants.TILE_PROVIDERS.put(method, loader);
	}
	
	@FunctionalInterface
	public interface Factory<T extends TileProvider> {
		T create(Tile[] tiles) throws IOException;
	}
	
	public static final class PrivateConstants {
		private PrivateConstants() { }
		private static final Object2ObjectMap<String, Factory<?>> TILE_PROVIDERS = new Object2ObjectOpenHashMap<>();
		
		static {
			TILE_PROVIDERS.defaultReturnValue(BasicTileProvider::new);
		}
	}
}
