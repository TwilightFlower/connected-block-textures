package io.github.nuclearfarts.cbt.model;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

import io.github.nuclearfarts.cbt.sprite.SpriteProvider;

public class CBTBakedModel extends ForwardingBakedModel {
	protected final SpriteProvider[] spriteProviders;
	
	public CBTBakedModel(BakedModel baseModel, SpriteProvider[] spriteProviders) {
		wrapped = baseModel;
		this.spriteProviders = spriteProviders;
	}
	
	@Override
	public boolean isVanillaAdapter() {
		return false;
	}
	
	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		SpriteFinder spriteFinder = SpriteFinder.get(MinecraftClient.getInstance().getBakedModelManager().method_24153(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE));
		context.pushTransform(quad -> {
			for(SpriteProvider provider : spriteProviders) {
				//use short-circuiting to our advantage to save the more expensive ones for last
				if(provider.affectsDirection(quad.nominalFace()) && provider.affectsBlock(blockView, state, pos) && provider.affectsSprite(quad, spriteFinder)) {
					//which way is up relative to the texture?
					Vector3f lowV1 = new Vector3f();
					int lowV1I = -1;
					Vector3f lowV2 = new Vector3f();
					Vector3f lowU1 = new Vector3f();
					int lowU1I = -1;
					Vector3f lowU2 = new Vector3f();
					
					Vector3f current = new Vector3f();
					Vector3f center = new Vector3f();
					
					float lastLowV = Float.MAX_VALUE;
					float lastLowU = Float.MAX_VALUE;
					for(int i = 0; i < 4; i++) {
						float v = quad.spriteV(i, 0);
						float u = quad.spriteU(i, 0);
						quad.copyPos(i, current);
						center.add(current);
						if(v < lastLowV) {
							lowV1I = i;
							lastLowV = v;
							quad.copyPos(i, lowV1);
						}
						if(u < lastLowU) {
							lowU1I = i;
							lastLowU = u;
							quad.copyPos(i, lowU1);
						}
					}
					// find center
					center.scale(1f/4f);
					// 2nd lowest
					lastLowV = Float.MAX_VALUE;
					lastLowU = Float.MAX_VALUE;
					for(int i = 0; i < 4; i++) {
						float v = quad.spriteV(i, 0);
						float u = quad.spriteU(i, 0);
						if(lowV1I != i && v < lastLowV) {
							lastLowV = v;
							quad.copyPos(i, lowV2);
						}
						if(lowU1I != i && u < lastLowU) {
							lastLowU = u;
							quad.copyPos(i, lowU2);
						}
					}
					
					// find center of low-v edge
					lowV1.add(lowV2);
					lowV1.scale(0.5f);
					// find center of low-u edge
					lowU1.add(lowU2);
					lowU1.scale(0.5f);
					// find vector between center of quad and center of edge
					lowV1.subtract(center);
					lowU1.subtract(center);
					
					Direction upDir = Direction.getFacing(lowV1.getX(), lowV1.getY(), lowV1.getZ());
					Direction leftDir = Direction.getFacing(lowU1.getX(), lowU1.getY(), lowU1.getZ());
					
					Sprite newSpr;
					if((newSpr = provider.getSpriteForSide(quad.nominalFace(), upDir, leftDir, blockView, state, pos, randomSupplier.get())) != null) {
						quad.spriteBake(0, newSpr, MutableQuadView.BAKE_LOCK_UV);
					}
				}
			}
			return true;
		});
		super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
		context.popTransform();
	}
	
	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		throw new UnsupportedOperationException("CBT models should never try to render as an item! THIS IS A PROBLEM!");
	}
}
