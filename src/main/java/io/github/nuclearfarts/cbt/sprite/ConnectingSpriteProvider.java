package io.github.nuclearfarts.cbt.sprite;

import java.util.function.BiPredicate;

import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import io.github.nuclearfarts.cbt.config.ConnectingCTMConfig;

public abstract class ConnectingSpriteProvider extends BaseSpriteProvider {
	protected final BiPredicate<BlockState, BlockState> connectionMatcher;
	
	public ConnectingSpriteProvider(Sprite[] connects, ConnectingCTMConfig<?> config) {
		super(connects, config);
		this.connectionMatcher = config.getConnectionMatcher();
	}

	protected boolean testUp(BlockRenderView view, Direction to, BlockPos pos, BlockState thisState) {
		return connectionMatcher.test(thisState, getUp(view, to, pos));
	}
	
	protected boolean testLeft(BlockRenderView view, Direction to, BlockPos pos, BlockState thisState) {
		return connectionMatcher.test(thisState, getLeft(view, to, pos));
	}
	
	protected boolean testRight(BlockRenderView view, Direction to, BlockPos pos, BlockState thisState) {
		return connectionMatcher.test(thisState, getRight(view, to, pos));
	}
	
	protected boolean testDown(BlockRenderView view, Direction to, BlockPos pos, BlockState thisState) {
		return connectionMatcher.test(thisState, getDown(view, to, pos));
	}
}
