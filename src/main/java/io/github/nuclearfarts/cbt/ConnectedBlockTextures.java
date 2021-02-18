package io.github.nuclearfarts.cbt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;
import io.github.nuclearfarts.cbt.config.CTMConfig;
import io.github.nuclearfarts.cbt.config.MainTextureConnectingCTMConfig;
import io.github.nuclearfarts.cbt.config.RandomCTMConfig;
import io.github.nuclearfarts.cbt.config.RepeatingCTMConfig;
import io.github.nuclearfarts.cbt.resource.CBTResourcePack;
import io.github.nuclearfarts.cbt.sprite.FullCTMSpriteProvider;
import io.github.nuclearfarts.cbt.sprite.HorizontalCTMSpriteProvider;
import io.github.nuclearfarts.cbt.sprite.HorizontalVerticalCTMSpriteProvider;
import io.github.nuclearfarts.cbt.sprite.RandomSpriteProvider;
import io.github.nuclearfarts.cbt.sprite.RepeatingCTMSpriteProvider;
import io.github.nuclearfarts.cbt.sprite.TopCTMSpriteProvider;
import io.github.nuclearfarts.cbt.sprite.VerticalCTMSpriteProvider;
import io.github.nuclearfarts.cbt.sprite.VerticalHorizontalCTMSpriteProvider;
import io.github.nuclearfarts.cbt.tile.loader.DynamicBookshelfTileLoader;
import io.github.nuclearfarts.cbt.tile.loader.DynamicGlassTileLoader;
import io.github.nuclearfarts.cbt.tile.loader.DynamicSandstoneTileLoader;
import io.github.nuclearfarts.cbt.tile.loader.TileLoader;
import io.github.nuclearfarts.cbt.tile.provider.CompactTileProvider;
import io.github.nuclearfarts.cbt.tile.provider.TileProvider;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class ConnectedBlockTextures implements ModInitializer {
	
	public static final Logger LOGGER = LogManager.getLogger("ConnectedBlockTextures");

	public static CBTResourcePack resourcePack;
	public static boolean overrideIdentifierCharRestriction = false;
	public static Thread identifierOverrideThread = null;
	public static final Object2IntMap<String> RESOURCE_PACK_PRIORITY_MAP = new Object2IntOpenHashMap<>();
	
	public static final short[] CTM_TO_IDEALIZED_BITHACK = {165, 173, 189, 181, 174, 179, 206, 186, 122, 91, 222, 250, 167, 175, 191, 183, 205, 117, 93, 115, 218, 94, 95, 123, 231, 239, 255, 247, 207, 190, 238, 187, 254, 251, 126, 219, 229, 237, 253, 245, 125, 243, 221, 119, 223, 127, 90};
	
	@Override
	public void onInitialize() {
		//connection modes
		CTMConfig.registerConfigLoader("ctm", MainTextureConnectingCTMConfig.loaderForFactory(FullCTMSpriteProvider::new));
		CTMConfig.registerConfigLoader("top", MainTextureConnectingCTMConfig.loaderForFactory(TopCTMSpriteProvider::new));
		CTMConfig.registerConfigLoader("horizontal", MainTextureConnectingCTMConfig.loaderForFactory(HorizontalCTMSpriteProvider::new));
		CTMConfig.registerConfigLoader("ctm_compact", MainTextureConnectingCTMConfig.loaderForFactory(FullCTMSpriteProvider::new));
		CTMConfig.registerConfigLoader("vertical", MainTextureConnectingCTMConfig.loaderForFactory(VerticalCTMSpriteProvider::new));
		CTMConfig.registerConfigLoader("horizontal+vertical", MainTextureConnectingCTMConfig.loaderForFactory(HorizontalVerticalCTMSpriteProvider::new));
		CTMConfig.registerConfigLoader("vertical+horizontal", MainTextureConnectingCTMConfig.loaderForFactory(VerticalHorizontalCTMSpriteProvider::new));
		//legacy random textures/texture replacement modes
		CTMConfig.registerConfigLoader("fixed", RandomCTMConfig.loaderForFactory(RandomSpriteProvider::new));
		CTMConfig.registerConfigLoader("random", RandomCTMConfig.loaderForFactory(RandomSpriteProvider::new));
		//repeating pattern
		CTMConfig.registerConfigLoader("repeat", RepeatingCTMConfig.loaderForFactory(RepeatingCTMSpriteProvider::new));
		
		TileProvider.registerTileProviderFactory("ctm_compact", CompactTileProvider::new);
		
		TileLoader.registerSpecialTileLoader("$CBT_SPECIAL_DYNAMIC_GLASS", DynamicGlassTileLoader::new);
		TileLoader.registerSpecialTileLoader("$CBT_SPECIAL_DYNAMIC_SANDSTONE", DynamicSandstoneTileLoader::new);
		TileLoader.registerSpecialTileLoader("$CBT_SPECIAL_DYNAMIC_BOOKSHELF", DynamicBookshelfTileLoader::new);
	}
	
	public static Identifier id(String string) {
		return new Identifier("connected_block_textures", string);
	}
	
	public static void breakpointHack() {
		System.out.println("bp");
	}
	
	static {
		RESOURCE_PACK_PRIORITY_MAP.defaultReturnValue(-1);
	}
}
