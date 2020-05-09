package io.github.nuclearfarts.cbt.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SimplePool<T> {
	private final Queue<T> queue = new ConcurrentLinkedQueue<>();
	private final Supplier<T> supplier;
	private final Consumer<T> resetter;
	
	public SimplePool(Supplier<T> supplier, Consumer<T> resetter) {
		this.supplier = supplier;
		this.resetter = resetter;
	}
	
	public SimplePool(Supplier<T> supplier) {
		this(supplier, t -> {});
	}
	
	public T get() {
		T obj;
		if((obj = queue.poll()) == null) {
			return supplier.get();
		} else {
			resetter.accept(obj);
			return obj;
		}
	}
	
	public void readd(T t) {
		queue.add(t);
	}
}
