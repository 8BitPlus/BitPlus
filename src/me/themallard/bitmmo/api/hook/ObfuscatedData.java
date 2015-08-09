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

import java.util.HashMap;
import java.util.Map;

public class ObfuscatedData {
	public static final String OBFUSCATED = "name.obfuscated";
	public static final String REFACTORED = "name.refactored";

	private final Map<String, String> variables = new HashMap<String, String>();

	public ObfuscatedData() {
	}

	public ObfuscatedData(String s, boolean refactored) {
		var(refactored ? REFACTORED : OBFUSCATED, s);
	}

	public ObfuscatedData(String obfuscated, String refactored) {
		obfuscated(obfuscated).refactored(refactored);
	}

	public Map<String, String> variables() {
		return variables;
	}

	public ObfuscatedData var(String k, String v) {
		variables.put(k, v);
		return this;
	}

	public String val(String k) {
		return variables.get(k);
	}

	public String val(String k, String def) {
		if (!variables.containsKey(k))
			return def;

		return variables.get(k);
	}

	public String obfuscated() {
		return variables.get(OBFUSCATED);
	}

	public ObfuscatedData obfuscated(String obfuscated) {
		var(OBFUSCATED, obfuscated);
		return this;
	}

	public String refactored() {
		return variables.get(REFACTORED);
	}

	public ObfuscatedData refactored(String refactored) {
		var(REFACTORED, refactored);
		return this;
	}
}
