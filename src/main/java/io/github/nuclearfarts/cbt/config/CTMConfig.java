package io.github.nuclearfarts.cbt.config;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import io.github.nuclearfarts.cbt.sprite.SpriteProvider;
import io.github.nuclearfarts.cbt.tile.provider.TileProvider;

public interface CTMConfig extends Comparable<CTMConfig> {
	TileProvider getTileProvider();
	Predicate<Identifier> getTileMatcher();
	Predicate<Direction> getFaceMatcher();
	BiPredicate<BlockRenderView, BlockPos> getWorldConditions();
	SpriteProvider createSpriteProvider(Sprite[] sprites);
	boolean affectsModel(ModelIdentifier id, Supplier<Collection<SpriteIdentifier>> textureDeps);
	int getResourcePackPriority();
	int getWeight();
	String getFileName();
	
	static void registerConfigLoader(String formatName, Loader<?> loader) {
		PrivateConstants.CTM_CONFIG_LOADERS.put(formatName, loader);
	}
	
	static CTMConfig load(Properties properties, Identifier location, ResourceManager manager, String packName) throws IOException {
		Loader<?> loader;
		if((loader = PrivateConstants.CTM_CONFIG_LOADERS.get(properties.getProperty("method"))) != null) {
			return loader.load(properties, location, manager, packName);
		}
		throw new IllegalArgumentException("Invalid or unsupported CTM method " + properties.getProperty("method"));
	}
	
	static CTMConfig load(Identifier propertiesLocation, ResourceManager manager) throws IOException {
		Properties properties = new Properties();
		Resource resource = manager.getResource(propertiesLocation);
		properties.load(resource.getInputStream());
		return CTMConfig.load(properties, propertiesLocation, manager, resource.getResourcePackName());
	}

	@FunctionalInterface
	public interface Loader<T extends CTMConfig> {
		T load(Properties properties, Identifier location, ResourceManager manager, String packName) throws IOException;
	}
	
	public static final class PrivateConstants {
		private PrivateConstants() { }
		private static final Map<String, Loader<?>> CTM_CONFIG_LOADERS = new HashMap<>();
	}
}
