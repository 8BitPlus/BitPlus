package me.themallard.bitmmo.api.obfuscation.refactor;

import java.util.Collection;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import me.themallard.bitmmo.api.util.Filter;

public class FieldCache extends DataCache<FieldNode> {
	public FieldCache(Collection<ClassNode> classes) {
		super(classes);
	}

	public FieldCache(Filter<FieldNode> filter) {
		super(filter);
	}

	public FieldCache(Filter<FieldNode> filter, Collection<ClassNode> classes) {
		super(filter, classes);
	}

	@Override
	public void put(ClassNode cn) {
		for (FieldNode f : cn.fields) {
			put(f);
		}
	}

	@Override
	public String makeKey(FieldNode t) {
		return makeKey(t.owner.name, t.name, t.desc);
	}

	@Override
	public void put(FieldNode f) {
		if (canCache(f)) {
			put(makeKey(f), f);
		}
	}
}