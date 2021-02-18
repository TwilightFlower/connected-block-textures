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
	
	protected BlockState[][] getAll(BlockRenderView view, Direction upD, Direction leftD, BlockPos pos) {
		BlockState[][] result = new BlockState[3][3];
		BlockPos left = pos.offset(leftD);
		BlockPos right = pos.offset(leftD.getOpposite());
		result[0][0] = view.getBlockState(left.offset(upD));
		result[0][1] = view.getBlockState(left);
		result[0][2] = view.getBlockState(left.offset(upD.getOpposite()));
		result[1][0] = view.getBlockState(pos.offset(upD));
		result[1][1] = null;
		result[1][2] = view.getBlockState(pos.offset(upD.getOpposite()));
		result[2][0] = view.getBlockState(right.offset(upD));
		result[2][1] = view.getBlockState(right);
		result[2][2] = view.getBlockState(right.offset(upD.getOpposite()));
		return result;
	}
}
