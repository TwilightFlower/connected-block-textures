package io.github.nuclearfarts.cbt.config;

import java.io.IOException;
import java.util.Properties;
import java.util.function.BiPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public abstract class ConnectingCTMConfig<Self extends ConnectingCTMConfig<Self>> extends BaseCTMConfig<Self> {
	public ConnectingCTMConfig(Properties properties, Identifier location, ResourceManager manager, SpriteProviderFactory<Self> bakedModelFactory, String packName) throws IOException {
		super(properties, location, manager, bakedModelFactory, packName);
	}
	
	public abstract BiPredicate<BlockState, BlockState> getConnectionMatcher();
}
