package io.github.nuclearfarts.cbt.sprite;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import io.github.nuclearfarts.cbt.config.RepeatingCTMConfig;
import io.github.nuclearfarts.cbt.util.CBTUtil;

public class RepeatingCTMSpriteProvider extends BaseSpriteProvider {
	private final int width;
	private final int height;
	
	public RepeatingCTMSpriteProvider(Sprite[] connects, RepeatingCTMConfig config) {
		super(connects, config);
		width = config.getWidth();
		height = config.getHeight();
	}

	@Override
	public Sprite getSpriteForSide(Direction side, Direction upD, Direction leftD, BlockRenderView view, BlockState state, BlockPos pos, Random random) {
		int wPos = 0;
		int hPos = 0;
		switch(side.getAxis()) {
		case X:
			if(side.getOffsetX() < 0) {
				wPos = CBTUtil.actualMod(pos.getZ(), width);
			} else {
				wPos = CBTUtil.actualMod(pos.getZ() - 1, width);
			}
			hPos = CBTUtil.actualMod(-pos.getY(), height);
			break;
		case Z:
			if(side.getOffsetZ() < 0) {
				wPos = CBTUtil.actualMod(pos.getX() - 1, width);
			} else {
				wPos = CBTUtil.actualMod(pos.getX(), width);
			}
			hPos = CBTUtil.actualMod(-pos.getY(), height);
			break;
		case Y:
			wPos = CBTUtil.actualMod(pos.getX(), width);
			if(side.getOffsetY() < 0) {
				hPos = CBTUtil.actualMod(-pos.getZ() - 1, height);
			} else {
				hPos = CBTUtil.actualMod(pos.getZ(), height);
			}
		}
		return connects[wPos + hPos * width];
	}

}
