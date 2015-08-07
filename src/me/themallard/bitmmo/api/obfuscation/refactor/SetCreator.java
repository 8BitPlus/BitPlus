package me.themallard.bitmmo.api.obfuscation.refactor;

import java.util.HashSet;
import java.util.Set;

import me.themallard.bitmmo.api.util.map.ValueCreator;

public class SetCreator<T> implements ValueCreator<Set<T>> {
	@Override
	public Set<T> create() {
		return new HashSet<T>();
	}
}