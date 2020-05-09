package io.github.nuclearfarts.cbt.sprite;

import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import io.github.nuclearfarts.cbt.config.ConnectingCTMConfig;

public class FullCTMSpriteProvider extends ConnectingSpriteProvider {

	//i did not write this manually, i generated it. do not ask how. it's evil and involved Swing.
	public static final byte[] BITHACK_TO_CTM = {0, 0, 12, 12, 0, 0, 12, 12, 1, 1, 4, 13, 1, 1, 4, 13, 3, 3, 5, 5, 3, 3, 15, 15, 2, 2, 7, 31, 2, 2, 29, 14, 0, 0, 12, 12, 0, 0, 12, 12, 1, 1, 4, 13, 1, 1, 4, 13, 3, 3, 5, 5, 3, 3, 15, 15, 2, 2, 7, 31, 2, 2, 29, 14, 36, 36, 24, 24, 36, 36, 24, 24, 16, 16, 6, 28, 16, 16, 6, 28, 17, 17, 19, 19, 17, 17, 43, 43, 18, 18, 46, 9, 18, 18, 21, 22, 36, 36, 24, 24, 36, 36, 24, 24, 37, 37, 30, 25, 37, 37, 30, 25, 17, 17, 19, 19, 17, 17, 43, 43, 40, 40, 8, 23, 40, 40, 34, 45, 0, 0, 12, 12, 0, 0, 12, 12, 1, 1, 4, 13, 1, 1, 4, 13, 3, 3, 5, 5, 3, 3, 15, 15, 2, 2, 7, 31, 2, 2, 29, 14, 0, 0, 12, 12, 0, 0, 12, 12, 1, 1, 4, 13, 1, 1, 4, 13, 3, 3, 5, 5, 3, 3, 15, 15, 2, 2, 7, 31, 2, 2, 29, 14, 36, 36, 24, 24, 36, 36, 24, 24, 16, 16, 6, 28, 16, 16, 6, 28, 39, 39, 41, 41, 39, 39, 27, 27, 42, 42, 20, 35, 42, 42, 10, 44, 36, 36, 24, 24, 36, 36, 24, 24, 37, 37, 30, 25, 37, 37, 30, 25, 39, 39, 41, 41, 39, 39, 27, 27, 38, 38, 11, 33, 38, 38, 32, 26};
	
	public FullCTMSpriteProvider(Sprite[] connects, ConnectingCTMConfig<?> config) {
		super(connects, config);
	}

	@Override
	public Sprite getSpriteForSide(Direction side, BlockRenderView view, BlockState state, BlockPos pos, Random random) {
		BlockState[][] blocks = getAll(view, side, pos);
		return this.connects[BITHACK_TO_CTM[awfulBitHack(blocks, state)]];
	}
	
	private int awfulBitHack(BlockState[][] blocks, BlockState state) {
		blocks[1][1] = state;
		//System.out.println(Arrays.deepToString(blocks));
		int result = 0;
		int i = 0;
		for(int j = 0; j < blocks.length; j++) {
			for(int k = 0; k < blocks.length; k++) {
				if(j == 1 && k == 1) {
					continue;
				}
				if(connectionMatcher.test(state, blocks[blocks.length - (k + 1)][blocks.length - (j + 1)])) {
					result |= (1 << i);
				}
				i++;
			}
		}
		return result;
	}
}
