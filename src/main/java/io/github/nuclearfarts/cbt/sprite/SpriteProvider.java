package io.github.nuclearfarts.cbt.sprite;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import net.fabricmc.fabric.api.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;

public interface SpriteProvider {
	boolean affectsBlock(BlockRenderView view, BlockState state, BlockPos pos);
	boolean affectsDirection(Direction side);
	boolean affectsSprite(QuadView quad, SpriteFinder finder);
	Sprite getSpriteForSide(Direction side, BlockRenderView view, BlockState state, BlockPos pos, Random random);
}
