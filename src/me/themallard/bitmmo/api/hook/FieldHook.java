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

public class FieldHook extends ObfuscatedData {
	private ClassHook owner;

	public FieldHook() {
	}

	public FieldHook(ClassHook owner) {
		this.owner = owner;
	}

	@Override
	public FieldHook var(String name, String value) {
		super.var(name, value);
		return this;
	}

	@Override
	public FieldHook obfuscated(String obfuscated) {
		super.obfuscated(obfuscated);
		return this;
	}

	@Override
	public FieldHook refactored(String refactored) {
		super.refactored(refactored);
		return this;
	}

	public ClassHook owner() {
		return owner;
	}

	public FieldHook owner(ClassHook owner) {
		if (this.owner != null)
			this.owner.fields().remove(this);

		this.owner = owner;
		owner.fields().add(this);
		return this;
	}

	public String baseToString() {
		StringBuilder sb = new StringBuilder("FieldHook");
		sb.append("[");
		sb.append("variables=").append(variables());
		sb.append("]");
		return sb.toString();
	}
}