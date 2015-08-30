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

import java.util.ArrayList;
import java.util.List;

public class ClassHook extends ObfuscatedData {
	private final List<InterfaceMapping> interfaces = new ArrayList<InterfaceMapping>();
	private final List<FieldHook> fields = new ArrayList<FieldHook>();
	private final List<MethodHook> methods = new ArrayList<MethodHook>();

	public ClassHook() {
	}

	public ClassHook(String name, boolean refactored) {
		super(name, refactored);
	}

	public ClassHook(String name, String refactored) {
		super(name, refactored);
	}

	@Override
	public ClassHook var(String k, String v) {
		super.var(k, v);
		return this;
	}

	@Override
	public ClassHook obfuscated(String obfuscated) {
		super.obfuscated(obfuscated);
		return this;
	}

	@Override
	public ObfuscatedData refactored(String refactored) {
		super.refactored(refactored);
		return this;
	}

	public List<InterfaceMapping> interfaces() {
		return interfaces;
	}

	public List<FieldHook> fields() {
		return fields;
	}

	public List<MethodHook> methods() {
		return methods;
	}
}
