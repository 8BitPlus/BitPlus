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
