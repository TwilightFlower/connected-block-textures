package io.github.nuclearfarts.cbt.util;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import io.github.nuclearfarts.cbt.util.function.ThrowingPredicate;

public class CBTUtil {
	
	public static Identifier appendId(Identifier path, String str) {
		return new Identifier(path.getNamespace(), path.getPath() + str);
	}
	
	public static Identifier prependId(Identifier path, String str) {
		return new Identifier(path.getNamespace(), str + path.getPath());
	}
	
	public static String ensurePngExtension(String string) {
		if(!string.endsWith(".png")) {
			return string + ".png";
		}
		return string;
	}

	//time to avoid streams!
	public static <T, U> boolean mapAnyMatch(Collection<? extends T> collection, Function<? super T, ? extends U> mapper, Predicate<U> predicate) {
		for(T t : collection) {
			if(predicate.test(mapper.apply(t))) {
				return true;
			}
		}
		return false;
	}
	
	public static <T, U extends Throwable> boolean allMatchThrowable(Collection<? extends T> collection, ThrowingPredicate<T, U> predicate) throws U {
		for(T t : collection) {
			if(!predicate.test(t)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean satisfiesAny(Collection<IntPredicate> predicates, int i) {
		for(IntPredicate p : predicates) {
			if(p.test(i)) {
				return true;
			}
		}
		return false;
	}
	
	public static Identifier directoryOf(Identifier id) {
		String path = id.getPath();
		return new Identifier(id.getNamespace(), path.substring(0, path.lastIndexOf('/')));
	}
	
	public static String fileNameOf(Identifier id) {
		String path = id.getPath();
		return path.substring(path.lastIndexOf('/'), path.length());
	}
	
	public static Identifier stripBlankVariants(ModelIdentifier modelId) {
		if(modelId.getVariant().equals("")) {
			return new Identifier(modelId.getNamespace(), modelId.getPath());
		}
		return modelId;
	}
	
	public static Identifier stripVariants(ModelIdentifier modelId) {
		return new Identifier(modelId.getNamespace(), modelId.getPath());
	}

	public static NativeImage copy(NativeImage image) {
		NativeImage newImage = new NativeImage(image.getFormat(), image.getWidth(), image.getHeight(), true);
		newImage.copyFrom(image);
		return newImage;
	}
	
	public static int actualMod(int in, int modulo) {
		int mod = in % modulo;
		if(mod < 0) {
			mod += modulo;
		}
		return mod;
	}
	
	public static int toABGR(int argb) {
		return ((argb >> 24) << 24) |          // Alpha
	            ((argb >> 16) & 0xFF) |         // Red  -> Blue
	            ((argb >> 8) & 0xFF) << 8 |     // Green
	            ((argb) & 0xFF) << 16;          // Blue -> Red
	}
}
