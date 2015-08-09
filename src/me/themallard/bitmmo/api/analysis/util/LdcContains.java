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

package me.themallard.bitmmo.api.analysis.util;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class LdcContains {
	public static boolean InstructionContains(AbstractInsnNode ain, String s) {
		if (!(ain instanceof LdcInsnNode))
			return false;

		LdcInsnNode ldc = (LdcInsnNode) ain;

		return ldc.cst.toString().contains(s);
	}

	public static boolean ListContains(InsnList il, String s) {
		for (AbstractInsnNode ain : il.toArray())
			if (InstructionContains(ain, s))
				return true;

		return false;
	}

	public static boolean MethodContains(MethodNode mn, String s) {
		return ListContains(mn.instructions, s);
	}

	public static boolean ClassContains(ClassNode cn, String s) {
		for (MethodNode mn : cn.methods)
			if (MethodContains(mn, s))
				return true;

		return false;
	}
}
