package io.github.nuclearfarts.cbt.sprite;

import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import io.github.nuclearfarts.cbt.config.ConnectingCTMConfig;

public class VerticalCTMSpriteProvider extends ConnectingSpriteProvider {

	public VerticalCTMSpriteProvider(Sprite[] connects, ConnectingCTMConfig<?> config) {
		super(connects, config);
	}

	@Override
	public Sprite getSpriteForSide(Direction side, Direction upD, Direction leftD, BlockRenderView view, BlockState state, BlockPos pos, Random random) {
		boolean down = testDown(view, upD, pos, state);
		boolean up = testUp(view, upD, pos, state);
		if(down && up) {
			return connects[1];
		} else if(down && !up) {
			return connects[2];
		} else if(!down && up) {
			return connects[0];
		} else {
			return connects[3];
		}
	}

}
