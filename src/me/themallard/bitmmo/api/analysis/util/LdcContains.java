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
