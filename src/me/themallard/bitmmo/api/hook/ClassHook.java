package me.themallard.bitmmo.api.hook;

import java.util.ArrayList;
import java.util.List;

import me.themallard.bitmmo.api.InterfaceMapping;

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
