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

package me.themallard.bitmmo.api.analysis;

import java.lang.reflect.Modifier;
import java.util.Map;

import org.nullbool.pi.core.hook.api.Constants;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.Context;
import me.themallard.bitmmo.api.hook.ClassHook;
import me.themallard.bitmmo.api.hook.FieldHook;
import me.themallard.bitmmo.api.hook.MethodHook;

public abstract class ClassAnalyser implements Opcodes {
	private final String name;
	private ClassNode foundClass;
	private ClassHook foundHook;

	public ClassAnalyser(String name) {
		this.name = name;
	}

	public void preRun(Map<String, ClassNode> classes) {
		foundClass = analyse(classes);

		if (foundClass != null)
			foundHook = new ClassHook(foundClass.name, name);
	}

	private ClassNode analyse(Map<String, ClassNode> classes) {
		for (ClassNode cn : classes.values()) {
			boolean match = matches(cn);

			if (match) {
				return cn;
			}
		}

		return null;
	}

	public void runSubs() {
		Builder<IFieldAnalyser> fs = registerFieldAnalysers();
		if (fs != null) {
			for (IFieldAnalyser f : fs) {
				try {
					for (FieldHook fh : f.find(foundClass)) {
						if (fh != null) {
							foundHook.fields().add(fh);
						} else {
							System.out.println("NULL HOOK AFTER EXECUTING: " + f.getClass().getCanonicalName());
						}
					}
				} catch (Exception e) {
					System.err.println(f.getClass().getCanonicalName() + " -> " + e.getClass().getSimpleName());
					e.printStackTrace();
				}
			}
		}

		Builder<IMethodAnalyser> ms = registerMethodAnalysers();
		if (ms != null) {
			for (IMethodAnalyser m : ms) {
				try {
					foundHook.methods().addAll(m.find(foundClass));
				} catch (Exception e) {
					System.err.println(m.getClass().getCanonicalName() + " -> " + e.getClass().getSimpleName());
					e.printStackTrace();
				}
			}
		}
	}

	private SupportedHooks getSupportedHooksAnno() {
		SupportedHooks anno = null;
		Class<?> klass = getClass();
		while (anno == null && klass != null && !klass.equals(Object.class)) {
			anno = klass.getAnnotation(SupportedHooks.class);
			klass = klass.getSuperclass();
		}

		return anno;
	}

	public String[] supportedFields() {
		return getSupportedHooksAnno().fields();
	}

	public String[] supportedMethods() {
		return getSupportedHooksAnno().methods();
	}

	protected abstract Builder<IFieldAnalyser> registerFieldAnalysers();

	protected abstract Builder<IMethodAnalyser> registerMethodAnalysers();

	protected abstract boolean matches(ClassNode cn);

	public FieldHook asFieldHook(ClassHook c, String classAndName, String realName) {
		if (classAndName == null)
			return null;
		String[] parts = classAndName.split("\\.");
		ClassNode cn = Context.current().getClassNodes().get(parts[0]);
		for (Object oF : cn.fields) {
			FieldNode f = (FieldNode) oF;
			if (parts[1].equals(f.name)) {

				// String owner = foundHook.obfuscated();
				// if(!owner.equals(f.owner.name)) {
				// // System.out.printf("[%b] %s.%s doesn't match %s for %s.%n",
				// Modifier.isStatic(f.access), f.owner.name, f.name,
				// foundHook.obfuscated(), realName);
				// owner = f.owner.name;
				// }

				return new FieldHook(foundHook).obfuscated(f.name).refactored(realName)
						.var(Constants.REAL_OWNER, f.owner.name).var(Constants.DESC, f.desc)
						.var(Constants.STATIC, Boolean.toString(Modifier.isStatic(f.access)));
			}
		}
		return null;
	}

	public FieldHook asFieldHook(String classAndName, String realName) {
		return asFieldHook(foundHook, classAndName, realName);
	}

	public FieldHook asFieldHook(FieldNode f, String realName) {
		return asFieldHook(f, realName, Modifier.isStatic(f.access));
	}

	public FieldHook asFieldHook(FieldNode f, String realName, boolean isStatic) {
		FieldHook fh = new FieldHook(new ClassHook(f.owner.name, foundHook.refactored())).obfuscated(f.name)
				.refactored(realName).var(Constants.REAL_OWNER, f.owner.name).var(Constants.DESC, f.desc)
				.var(Constants.STATIC, Boolean.toString(isStatic));
		// if(hasMulti(f.desc))
		// fh.var(FieldHook.MUTLI, Long.toString(multiplier));
		return fh;
	}

	public FieldHook asFieldHook(FieldInsnNode f, String realName) {
		return asFieldHook(f, realName, f.opcode() == PUTSTATIC || f.opcode() == GETSTATIC);
	}

	public FieldHook asFieldHook(FieldInsnNode f, String realName, boolean isStatic) {
		FieldHook fh = new FieldHook(new ClassHook(f.owner, foundHook.refactored())).obfuscated(f.name)
				.refactored(realName).var(Constants.REAL_OWNER, f.owner).var(Constants.DESC, f.desc)
				.var(Constants.STATIC, Boolean.toString(isStatic));
		// if(hasMulti(f.desc))
		// fh.var(FieldHook.MUTLI, Long.toString(multiplier));
		return fh;
	}

	public MethodHook asMethodHook(MethodInsnNode min, String realName) {
		ClassNode cn = Context.current().getClassNodes().get(min.owner);
		for (Object oM : cn.methods) {
			MethodNode m = (MethodNode) oM;
			if (min.name.equals(m.name) && min.desc.equals(m.desc)) {
				return new MethodHook(foundHook).obfuscated(m.name).refactored(realName)
						.var(Constants.REAL_OWNER, min.owner).var(Constants.DESC, m.desc)
						.var(Constants.STATIC, Boolean.toString(Modifier.isStatic(m.access)));
			}
		}
		return null;
	}

	// TODO:
	public MethodHook asMethodHook(String classAndName, String realName) {
		String[] parts = classAndName.split("\\.");
		// System.out.println("methods in " + Arrays.toString(parts));
		ClassNode cn = Context.current().getClassNodes().get(parts[0]);
		for (Object oM : cn.methods) {
			MethodNode m = (MethodNode) oM;
			// System.out.println(" method " + m.name + " " + m.desc);
			if (parts[1].equals(m.name)) {
				return new MethodHook(foundHook).obfuscated(m.name).refactored(realName)
						.var(Constants.REAL_OWNER, cn.name).var(Constants.DESC, m.desc)
						.var(Constants.STATIC, Boolean.toString(Modifier.isStatic(m.access)));
			}
		}
		return null;
	}

	public MethodHook asMethodHook(MethodNode m, String realName) {
		return new MethodHook(foundHook).obfuscated(m.name).refactored(realName).var(Constants.REAL_OWNER, m.owner.name)
				.var(Constants.DESC, m.desc).var(Constants.STATIC, Boolean.toString(Modifier.isStatic(m.access)));
	}

	public String getName() {
		return name;
	}

	public ClassNode getFoundClass() {
		return foundClass;
	}

	public ClassHook getFoundHook() {
		return foundHook;
	}
}
