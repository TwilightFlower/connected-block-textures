package io.github.nuclearfarts.cbt.sprite;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import net.fabricmc.fabric.api.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;

import io.github.nuclearfarts.cbt.config.CTMConfig;

public abstract class BaseSpriteProvider implements SpriteProvider {
	protected final Sprite[] connects; //different subclasses use this different ways
	protected final Predicate<Sprite> tileMatcher;
	protected final Predicate<Direction> faceMatcher;
	protected final BiPredicate<BlockRenderView, BlockPos> worldConditions;
	
	public BaseSpriteProvider(Sprite[] connects, CTMConfig config) {
		this.connects = connects;
		Predicate<Identifier> tileMatcher = config.getTileMatcher();
		this.tileMatcher = tileMatcher == null ? null : s -> tileMatcher.test(s.getId());
		faceMatcher = config.getFaceMatcher();
		worldConditions = config.getWorldConditions();
	}

	@Override
	public boolean affectsBlock(BlockRenderView view, BlockState state, BlockPos pos) {
		return worldConditions.test(view, pos);
	}

	@Override
	public boolean affectsDirection(Direction side) {
		return faceMatcher.test(side);
	}
	
	@Override
	public boolean affectsSprite(QuadView quad, SpriteFinder finder) {
		return tileMatcher == null ? true : tileMatcher.test(finder.find(quad, 0));
	}
	
	protected BlockState[][] getAll(BlockRenderView view, Direction to, BlockPos pos) {
		BlockState[][] result = new BlockState[3][3];
		BlockPos left = getLeftPos(to, pos);
		BlockPos right = getRightPos(to, pos);
		result[0][0] = getUp(view, to, left);
		result[0][1] = view.getBlockState(left);
		result[0][2] = getDown(view, to, left);
		result[1][0] = getUp(view, to, pos);
		result[1][1] = null;
		result[1][2] = getDown(view, to, pos);
		result[2][0] = getUp(view, to, right);
		result[2][1] = view.getBlockState(right);
		result[2][2] = getDown(view, to, right);
		return result;
	}
	
	protected BlockState getUp(BlockRenderView view, Direction to, BlockPos pos) {
		return view.getBlockState(getUpPos(to, pos));
	}
	
	protected BlockPos getUpPos(Direction to, BlockPos pos) {
		switch(to) {
		case UP:
			//return pos.north();
		case DOWN:
			return pos.south();
		case NORTH:
		case SOUTH:
		case EAST:
		case WEST:
			return pos.up();
		default:
			throw new IllegalArgumentException("real enum breaking moment right here");
		}
	}
	
	protected BlockState getLeft(BlockRenderView view, Direction to, BlockPos pos) {
		return view.getBlockState(getLeftPos(to, pos));
	}
	
	protected BlockPos getLeftPos(Direction to, BlockPos pos) {
		switch(to) {
		case UP:
		case DOWN:
			return pos.west();
		case NORTH:
		case SOUTH:
		case EAST:
		case WEST:
			return pos.offset(to.rotateYClockwise());
		default:
			throw new IllegalArgumentException("real enum breaking moment right here");
		}
	}
	
	protected BlockState getRight(BlockRenderView view, Direction to, BlockPos pos) {
		return view.getBlockState(getRightPos(to, pos));
	}
	
	protected BlockPos getRightPos(Direction to, BlockPos pos) {
		switch(to) {
		case UP:
		case DOWN:
			return pos.east();
		case NORTH:
		case SOUTH:
		case EAST:
		case WEST:
			return pos.offset(to.rotateYCounterclockwise());
		default:
			throw new IllegalArgumentException("real enum breaking moment right here");
		}
	}
	
	protected BlockState getDown(BlockRenderView view, Direction to, BlockPos pos) {
		return view.getBlockState(getDownPos(to, pos));
	}
	
	protected BlockPos getDownPos(Direction to, BlockPos pos) {
		switch(to) {
		case UP:
			//return pos.south();
		case DOWN:
			return pos.north();
		case NORTH:
		case SOUTH:
		case EAST:
		case WEST:
			return pos.down();
		default:
			throw new IllegalArgumentException("real enum breaking moment right here");
		}
	}
}
