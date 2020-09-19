package io.github.nuclearfarts.cbt.model;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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
			/*if(sideMatcher.test(quad.nominalFace())) {
				Sprite spr = spriteFinder.find(quad, 0);
				if(tileMatcher == null || tileMatcher.test(spr)) {
					Sprite newSpr = getSpriteForSide(quad.lightFace(), blockView, state, pos, randomSupplier.get());
					if(newSpr != null) {
						for(int i = 0; i < 4; i++) {
							quad.spriteBake(0, newSpr, MutableQuadView.BAKE_LOCK_UV);
							//quad.sprite(i, 0, quad.spriteU(i, 0) - spr.getMinU() + newSpr.getMinU(), quad.spriteV(i, 0) - spr.getMinV() + newSpr.getMinV());
						}
					}
				}
			}*/
			for(SpriteProvider provider : spriteProviders) {
				//use short-circuiting to our advantage to save the more expensive ones for last
				if(provider.affectsDirection(quad.nominalFace()) && provider.affectsBlock(blockView, state, pos) && provider.affectsSprite(quad, spriteFinder)) {
					Sprite newSpr;
					if((newSpr = provider.getSpriteForSide(quad.nominalFace(), blockView, state, pos, randomSupplier.get())) != null) {
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
