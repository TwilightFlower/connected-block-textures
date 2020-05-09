package io.github.nuclearfarts.cbt.config;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.function.BiPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import io.github.nuclearfarts.cbt.ConnectionConditions;

public class MainTextureConnectingCTMConfig extends ConnectingCTMConfig<MainTextureConnectingCTMConfig> {
	private final BiPredicate<BlockState, BlockState> connectionMatcher;
	
	public static CTMConfig.Loader<MainTextureConnectingCTMConfig> loaderForFactory(SpriteProviderFactory<MainTextureConnectingCTMConfig> factory) {
		return (p, l, m, n) -> new MainTextureConnectingCTMConfig(p, l, m, factory, n);
	}
	
	public MainTextureConnectingCTMConfig(Properties properties, Identifier location, ResourceManager manager, SpriteProviderFactory<MainTextureConnectingCTMConfig> bakedModelFactory, String packName) throws IOException {
		super(properties, location, manager, bakedModelFactory, packName);
		String connect = properties.getProperty("connect", properties.containsKey("matchBlocks") ? "blocks" : "tiles");
		connectionMatcher = ConnectionConditions.valueOf(connect.toUpperCase(Locale.ENGLISH));
	}

	@Override
	public BiPredicate<BlockState, BlockState> getConnectionMatcher() {
		return connectionMatcher;
	}

	@Override
	protected MainTextureConnectingCTMConfig getSelf() {
		return this;
	}

}
