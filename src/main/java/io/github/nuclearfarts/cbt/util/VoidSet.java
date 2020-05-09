package io.github.nuclearfarts.cbt.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class VoidSet<E> implements Set<E> {
	private static final VoidSet<?> INSTANCE = new VoidSet<>();
	
	@SuppressWarnings("unchecked")
	public static <T> VoidSet<T> get() {
		return (VoidSet<T>) INSTANCE;
	}
	
	private VoidSet() { }

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return VoidIterator.get();
	}

	@Override
	public Object[] toArray() {
		return new Object[0];
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return a;
	}

	@Override
	public boolean add(E e) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public void clear() { }

	private static class VoidIterator<E> implements Iterator<E> {
		private static final VoidIterator<?> INSTANCE = new VoidIterator<>();
		
		@SuppressWarnings("unchecked")
		public static <T> VoidIterator<T> get() {
			return (VoidIterator<T>) INSTANCE;
		}
		
		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public E next() {
			return null;
		}
		
	}
}
