package io.github.nuclearfarts.cbt.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.level.ColorResolver;

public class CursedBiomeThing implements ColorResolver {
	private static final SimplePool<CursedBiomeThing> POOL = new SimplePool<CursedBiomeThing>(CursedBiomeThing::new);
	
	Biome result;
	
	private CursedBiomeThing() { }
	
	public static Biome getBiome(BlockRenderView view, BlockPos pos) {
		CursedBiomeThing cursed = POOL.get();
		view.getColor(pos, cursed);
		Biome result = cursed.result;
		POOL.readd(cursed);
		return result;
	}

	@Override
	public int getColor(Biome biome, double d, double e) {
		result = biome;
		return 0;
	}

}
