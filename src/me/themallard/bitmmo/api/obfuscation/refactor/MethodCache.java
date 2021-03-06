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

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.util.Filter;

public class MethodCache extends DataCache<MethodNode> {
	public MethodCache(Collection<ClassNode> classes) {
		super(classes);
	}

	public MethodCache(Filter<MethodNode> filter) {
		super(filter);
	}

	public MethodCache(Filter<MethodNode> filter, Collection<ClassNode> classes) {
		super(filter, classes);
	}

	@Override
	public void put(ClassNode cn) {
		for (MethodNode m : cn.methods) {
			put(m);
		}
	}

	@Override
	public String makeKey(MethodNode t) {
		return makeKey(t.owner.name, t.name, t.desc);
	}

	@Override
	public void put(MethodNode m) {
		if (canCache(m)) {
			put(makeKey(m), m);
			m.cacheKey();
		}
	}
}