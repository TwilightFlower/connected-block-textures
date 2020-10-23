package io.github.nuclearfarts.cbt.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.base.Predicates;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.Biome;

import io.github.nuclearfarts.cbt.ConnectedBlockTextures;
import io.github.nuclearfarts.cbt.sprite.SpriteProvider;
import io.github.nuclearfarts.cbt.tile.provider.TileProvider;
import io.github.nuclearfarts.cbt.util.CBTUtil;
import io.github.nuclearfarts.cbt.util.CursedBiomeThing;

//Java generics are awful.
public abstract class BaseCTMConfig<Self extends BaseCTMConfig<Self>> implements CTMConfig {

	protected final TileProvider tileProvider;
	protected final Predicate<Identifier> tileMatcher;
	protected final Predicate<Identifier> blockMatcher;
	protected final Predicate<Direction> faceMatcher;
	protected final BiPredicate<BlockRenderView, BlockPos> worldConditions;
	protected final int packPriority;
	protected final int weight;
	protected final String fileName;
	protected final SpriteProviderFactory<Self> spriteProviderFactory;
	
	public BaseCTMConfig(Properties properties, Identifier location, ResourceManager manager, SpriteProviderFactory<Self> bakedModelFactory, String packName) throws IOException {
		if(properties.containsKey("matchTiles")) {
			tileMatcher = Arrays.stream(properties.getProperty("matchTiles").split(" ")).map(s -> CBTUtil.prependId(new Identifier(s), "block/")).collect(Collectors.toCollection(HashSet::new))::contains;
		} else {
			tileMatcher = null;
		}
		
		if(properties.containsKey("matchBlocks")) {
			blockMatcher = Arrays.stream(properties.getProperty("matchBlocks").split(" ")).map(Identifier::new).collect(Collectors.toCollection(HashSet::new))::contains;
		} else {
			blockMatcher = Predicates.alwaysTrue();
		}
		
		Predicate<Biome> biomeMatcher;
		if(properties.containsKey("biomes")) {
			//@SuppressWarnings("resource")
			Registry<Biome> biomes = MinecraftClient.getInstance().world.getRegistryManager().get(Registry.BIOME_KEY);
			biomeMatcher = Arrays.stream(properties.getProperty("biomes").split(" ")).map(Identifier::new).map(biomes::get).collect(Collectors.toCollection(HashSet::new))::contains;
		} else {
			biomeMatcher = null;
		}
		
		IntPredicate heightMatcher;
		if(properties.containsKey("heights")) {
			List<IntPredicate> heightTests = new ArrayList<>();
			for(String range : properties.getProperty("heights").split(" ")) {
				String[] split = range.split("-");
				int min = Integer.parseInt(split[0]);
				int max = Integer.parseInt(split[1]);
				heightTests.add(i -> min <= i && i <= max);
			}
			if(heightTests.size() == 1) {
				heightMatcher = heightTests.get(0);
			} else {
				heightMatcher = i -> CBTUtil.satisfiesAny(heightTests, i);
			}
		} else {
			heightMatcher = i -> true;
		}
		
		if(biomeMatcher != null) {
			worldConditions = (w, p) -> biomeMatcher.test(CursedBiomeThing.getBiome(w, p)) && heightMatcher.test(p.getY());
		} else {
			worldConditions = (w, p) -> heightMatcher.test(p.getY());
		}
		
		if(properties.containsKey("faces")) {
			Set<Direction> faces = EnumSet.noneOf(Direction.class);
			for(String face : properties.getProperty("faces").split(" ")) {
				switch(face) {
				case "sides":
					faces.add(Direction.NORTH);
					faces.add(Direction.EAST);
					faces.add(Direction.WEST);
					faces.add(Direction.SOUTH);
					break;
				case "top":
					faces.add(Direction.UP);
					break;
				case "bottom":
					faces.add(Direction.DOWN);
					break;
				case "all":
					for(Direction d : Direction.values()) {
						faces.add(d);
					}
					break;
				default:
					faces.add(Direction.valueOf(face.toUpperCase(Locale.ENGLISH))); //avoid the turkish bug
					break;
				}
			}
			faceMatcher = faces::contains;
		} else {
			faceMatcher = Predicates.alwaysTrue();
		}
		
		weight = Integer.parseInt(properties.getProperty("weight", "0"));
		
		if(properties.containsKey("cbt_special_change_pack_if_loaded")) {
			String packOverride = properties.getProperty("cbt_special_change_pack_if_loaded");
			if(ConnectedBlockTextures.RESOURCE_PACK_PRIORITY_MAP.containsKey(packOverride)) {
				packName = packOverride;
			}
		}
		
		this.packPriority = ConnectedBlockTextures.RESOURCE_PACK_PRIORITY_MAP.getInt(packName);
		this.spriteProviderFactory = bakedModelFactory;
		this.tileProvider = TileProvider.load(properties, CBTUtil.directoryOf(location), manager);
		this.fileName = CBTUtil.fileNameOf(location);
	}

	public int compareTo(CTMConfig other) {
		if(getResourcePackPriority() != other.getResourcePackPriority()) {
			return getResourcePackPriority() - other.getResourcePackPriority();
		}
		
		if(getWeight() != other.getWeight()) {
			return getWeight() - other.getWeight();
		}
		
		if(!getFileName().equals(other.getFileName())) {
			return getFileName().compareTo(other.getFileName());
		}
		
		return 0;
	}
	
	@Override
	public TileProvider getTileProvider() {
		return tileProvider;
	}
	
	@Override
	public Predicate<Identifier> getTileMatcher() {
		return tileMatcher;
	}
	
	@Override
	public Predicate<Direction> getFaceMatcher() {
		return faceMatcher;
	}
	
	@Override
	public BiPredicate<BlockRenderView, BlockPos> getWorldConditions() {
		return worldConditions;
	}

	@Override
	public SpriteProvider createSpriteProvider(Sprite[] sprites) {
		return spriteProviderFactory.create(sprites, getSelf());
	}

	@Override
	public boolean affectsModel(ModelIdentifier id, Supplier<Collection<SpriteIdentifier>> textureDeps) {
		return (blockMatcher.test(id) || blockMatcher.test(CBTUtil.stripVariants(id))) && (tileMatcher == null || CBTUtil.mapAnyMatch(textureDeps.get(), SpriteIdentifier::getTextureId, tileMatcher));
	}

	@Override
	public int getResourcePackPriority() {
		return packPriority;
	}
	
	@Override
	public int getWeight() {
		return weight;
	}
	
	@Override
	public String getFileName() {
		return fileName;
	}
	
	@FunctionalInterface
	public interface SpriteProviderFactory<C extends BaseCTMConfig<C>> {
		SpriteProvider create(Sprite[] sprites, C config);
	}
	
	protected abstract Self getSelf(); //java pls
}
