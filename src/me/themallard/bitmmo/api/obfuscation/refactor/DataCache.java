/* Copyright (C) 2015 maaatts

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package me.themallard.bitmmo.api.obfuscation.refactor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.tree.ClassNode;

import me.themallard.bitmmo.api.util.Filter;

public abstract class DataCache<T> {
	private final Filter<T> filter;
	private final Map<String, T> map;

	public DataCache(Filter<T> filter) {
		this.filter = filter;
		map = new HashMap<String, T>();
	}

	public DataCache(Filter<T> filter, Collection<ClassNode> classes) {
		this(filter);
		put(classes);
	}

	public DataCache(Collection<ClassNode> classes) {
		this(Filter.acceptAll(), classes);
	}

	public void put(Collection<ClassNode> classes) {
		for (ClassNode cn : classes) {
			put(cn);
		}
	}

	public boolean canCache(T t) {
		return filter.accept(t);
	}

	public abstract void put(ClassNode cn);

	public abstract String makeKey(T t);

	public abstract void put(T t);

	public void put(String key, T t) {
		map.put(key, t);
	}

	public void reset() {
		Collection<T> methods = map.values();
		map.clear();
		for (T m : methods) {
			put(m);
		}
	}

	public static String makeKey(String owner, String name, String desc) {
		return new StringBuilder(owner).append(".").append(name).append(desc).toString();
	}

	public T get(String owner, String name, String desc) {
		return get(makeKey(owner, name, desc));
	}

	public T get(String key) {
		if (map.containsKey(key))
			return map.get(key);
		return null;
	}

	public void clear() {
		map.clear();
	}

	public int size() {
		return map.size();
	}
}
