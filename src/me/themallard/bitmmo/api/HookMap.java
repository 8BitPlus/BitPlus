package me.themallard.bitmmo.api;

import java.util.ArrayList;
import java.util.List;

import me.themallard.bitmmo.api.hook.ClassHook;
import me.themallard.bitmmo.api.hook.FieldHook;
import me.themallard.bitmmo.api.hook.MethodHook;

public class HookMap {

	private static final int CURRENT_VERSION = 3;

	private final int version;
	private List<ClassHook> classes;

	public HookMap() {
		this(CURRENT_VERSION);
	}

	public HookMap(int ver) {
		this.version = ver;
		classes = new ArrayList<ClassHook>();
	}

	public HookMap(List<ClassHook> classes) {
		this(CURRENT_VERSION, classes);
	}

	public HookMap(int ver, List<ClassHook> classes) {
		this.version = ver;
		this.classes = classes;
	}

	public List<ClassHook> classes() {
		return classes;
	}

	public void addClass(ClassHook hook) {
		classes.add(hook);
	}

	public ClassHook forName(String o, boolean obf) {
		for (ClassHook c : classes) {
			if (obf) {
				if (c.obfuscated().equals(o))
					return c;
			} else {
				if (c.refactored().equals(o))
					return c;
			}
		}
		return null;
	}

	public void classes(List<ClassHook> classes) {
		this.classes = classes;
	}

	public int version() {
		return version;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ClassHook c : classes) {
			sb.append(c).append("\n");
			for (FieldHook h : c.fields()) {
				sb.append("\t").append(h).append("\n");
			}
			for (MethodHook h : c.methods()) {
				sb.append("\t").append(h).append("\n");
			}
		}
		return sb.toString();
	}
}