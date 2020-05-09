package io.github.nuclearfarts.cbt.config;

import java.io.IOException;
import java.util.Properties;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class RepeatingCTMConfig extends BaseCTMConfig<RepeatingCTMConfig> {
	private final int width;
	private final int height;
	
	public static CTMConfig.Loader<RepeatingCTMConfig> loaderForFactory(SpriteProviderFactory<RepeatingCTMConfig> factory) {
		return (p, l, m, n) -> new RepeatingCTMConfig(p, l, m, factory, n);
	}

	public RepeatingCTMConfig(Properties properties, Identifier location, ResourceManager manager, SpriteProviderFactory<RepeatingCTMConfig> bakedModelFactory, String packName) throws IOException {
		super(properties, location, manager, bakedModelFactory, packName);
		width = Integer.parseInt(properties.getProperty("width"));
		height = Integer.parseInt(properties.getProperty("height"));
	}

	@Override
	protected RepeatingCTMConfig getSelf() {
		return this;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
