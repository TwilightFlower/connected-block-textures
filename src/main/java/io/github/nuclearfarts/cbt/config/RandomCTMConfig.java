package io.github.nuclearfarts.cbt.config;

import java.io.IOException;
import java.util.Properties;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class RandomCTMConfig extends BaseCTMConfig<RandomCTMConfig> {
	
	public static CTMConfig.Loader<RandomCTMConfig> loaderForFactory(SpriteProviderFactory<RandomCTMConfig> factory) {
		return (p, l, m, n) -> new RandomCTMConfig(p, l, m, factory, n);
	}
	
	public RandomCTMConfig(Properties properties, Identifier location, ResourceManager manager, SpriteProviderFactory<RandomCTMConfig> bakedModelFactory, String packName) throws IOException {
		super(properties, location, manager, bakedModelFactory, packName);
	}

	@Override
	protected RandomCTMConfig getSelf() {
		return this;
	}
}
