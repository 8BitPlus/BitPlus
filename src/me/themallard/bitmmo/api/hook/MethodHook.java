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