package me.themallard.bitmmo.api.hook;

import org.objectweb.asm.tree.InsnList;

public class MethodHook extends ObfuscatedData {
	private ClassHook owner;
	private InsnList instructions;

	public MethodHook() {
	}

	public MethodHook(ClassHook owner) {
		this.owner = owner;
	}

	public MethodHook(InsnList instructions) {
		this.instructions = instructions;
	}

	@Override
	public MethodHook var(String name, String value) {
		super.var(name, value);
		return this;
	}

	@Override
	public MethodHook obfuscated(String obfuscated) {
		super.obfuscated(obfuscated);
		return this;
	}

	@Override
	public MethodHook refactored(String refactored) {
		super.refactored(refactored);
		return this;
	}

	public ClassHook owner() {
		return owner;
	}

	public MethodHook owner(ClassHook owner) {
		if (this.owner != null)
			this.owner.methods().remove(this);

		this.owner = owner;
		owner.methods().add(this);
		return this;
	}

	public InsnList insns() {
		return instructions;
	}

	public MethodHook insns(InsnList instructions) {
		this.instructions = instructions;
		return this;
	}
}