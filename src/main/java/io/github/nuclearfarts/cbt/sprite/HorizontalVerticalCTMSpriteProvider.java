package io.github.nuclearfarts.cbt.sprite;

import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import io.github.nuclearfarts.cbt.config.ConnectingCTMConfig;

public class HorizontalVerticalCTMSpriteProvider extends ConnectingSpriteProvider {

	public HorizontalVerticalCTMSpriteProvider(Sprite[] connects, ConnectingCTMConfig<?> config) {
		super(connects, config);
	}

	@Override
	public Sprite getSpriteForSide(Direction side, BlockRenderView view, BlockState state, BlockPos pos, Random random) {
		boolean left = testLeft(view, side, pos, state);
		boolean right = testRight(view, side, pos, state);
		boolean down = testDown(view, side, pos, state);
		boolean up = testUp(view, side, pos, state);
		if(left && right) {
			return connects[1];
		} else if(left && !right) {
			return connects[2];
		} else if(!left && right) {
			return connects[0];
		} else if(down && up) {
			return connects[5];
		} else if(down && !up) {
			return connects[6];
		} else if(!down && up) {
			return connects[4];
		} else {
			return connects[3];
		}
	}

}
