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

package me.themallard.bitmmo.api.transformer;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.sun.xml.internal.ws.org.objectweb.asm.Type;

import me.themallard.bitmmo.api.Context;
import me.themallard.bitmmo.api.analysis.AbstractAnalysisProvider;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.util.Filter;

public abstract class Transformer {
	private final String name;
	private Set<Filter<ClassNode>> filters;

	public Transformer(String name) {
		this.name = name;
		this.filters = new HashSet<Filter<ClassNode>>();
	}

	public void preRun(ClassNode cn) {
	}

	public boolean accept(ClassNode cn) {
		for (Filter<ClassNode> f : filters) {
			if (!f.accept(cn))
				return false;
		}

		return true;
	}

	public void addFilter(Filter<ClassNode> filter) {
		filters.add(filter);
	}

	public abstract void run(ClassNode cn);

	public String getName() {
		return this.name;
	}

	// utils
	protected final String getRefactoredName(String original) {
		AbstractAnalysisProvider provider = Context.current();
		Stream<ClassAnalyser> stream = provider.getAnalysers().stream();
		stream = stream.filter(a -> a.getFoundHook().obfuscated().equals(original));
		ClassAnalyser a = stream.findFirst().orElse(null);
		return a != null ? a.getFoundHook().refactored() : null;
	}

	public String getRefactoredNameByType(String desc) {
		String objectname = Type.getType(desc).toString();

		if (objectname.length() < 3)
			return null;

		return getRefactoredName(objectname.substring(1, objectname.length() - 1));
	}

	protected final void addInterface(ClassNode node, String iface) {
		node.interfaces.add(iface);
	}

	protected final void createGetter(ClassNode cn, FieldNode fn, String name) {
		MethodVisitor mv = cn.visitMethod(Opcodes.ACC_PUBLIC, name, "()" + fn.desc, null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, cn.name, fn.name, fn.desc);
		mv.visitInsn(Type.getType(fn.desc).getOpcode(Opcodes.IRETURN));
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	protected final void createSetter(ClassNode cn, FieldNode fn, String name) {
		MethodVisitor mv = cn.visitMethod(Opcodes.ACC_PUBLIC, name, "(" + fn.desc + ")V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Type.getType(fn.desc).getOpcode(Opcodes.ILOAD), 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, cn.name, fn.name, fn.desc);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}
}
