package io.github.nuclearfarts.cbt.tile.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import io.github.nuclearfarts.cbt.tile.ResourceBackedTile;
import io.github.nuclearfarts.cbt.tile.Tile;
import io.github.nuclearfarts.cbt.util.CBTUtil;

public class BasicTileLoader implements TileLoader {
	private static final Pattern RANGE_PATTERN = Pattern.compile("^\\d+-\\d+$");
	
	private final Tile[] tiles;
	
	public BasicTileLoader(Properties properties, Identifier location, ResourceManager manager) {
		this(properties.getProperty("tiles").split(" "), location, manager);
	}
	
	public BasicTileLoader(String[] tileDefs, Identifier location, ResourceManager manager) {
		List<Tile> loadedTiles = new ArrayList<>();
		for(String tileDef : tileDefs) {
			if(RANGE_PATTERN.matcher(tileDef).find()) {
				String[] defSplit = tileDef.split("-");
				int min = Integer.parseInt(defSplit[0]);
				int max = Integer.parseInt(defSplit[1]);
				for(int i = min; i <= max; i++) {
					loadedTiles.add(new ResourceBackedTile(CBTUtil.appendId(location, "/" + i + ".png"), manager));
				}
			} else {
				if(tileDef.contains("/")) {
					if(tileDef.startsWith("~")) {
						tileDef = "optifine" + tileDef.substring(1);
					}
					loadedTiles.add(new ResourceBackedTile(new Identifier(CBTUtil.ensurePngExtension(tileDef)), manager));
				} else {
					loadedTiles.add(new ResourceBackedTile(CBTUtil.appendId(location, "/" + CBTUtil.ensurePngExtension(tileDef)), manager));
				}
			}
		}
		this.tiles = loadedTiles.toArray(new Tile[loadedTiles.size()]);
	}

	@Override
	public Tile[] getTiles() {
		return tiles;
	}
}
