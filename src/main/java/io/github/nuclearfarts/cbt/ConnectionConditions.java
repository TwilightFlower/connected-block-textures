package io.github.nuclearfarts.cbt;

import java.util.function.BiPredicate;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModels;

public enum ConnectionConditions implements BiPredicate<BlockState, BlockState> {
	BLOCK((t, o) -> t.getBlock() == o.getBlock()),
	BLOCKS((t, o) -> t.getBlock() == o.getBlock()),
	STATE((t, o) -> t == o),
	MATERIAL((t, o) -> t.getMaterial() == o.getMaterial()),
	TILE((t, o) -> {
		BlockModels models = MinecraftClient.getInstance().getBakedModelManager().getBlockModels();
		return models.getSprite(t) == models.getSprite(o);
	});

	private final BiPredicate<BlockState, BlockState> func;
	
	private ConnectionConditions(BiPredicate<BlockState, BlockState> func) {
		this.func = func;
	}
	
	@Override
	public boolean test(BlockState myState, BlockState otherState) {
		return func.test(myState, otherState);
	}

}
