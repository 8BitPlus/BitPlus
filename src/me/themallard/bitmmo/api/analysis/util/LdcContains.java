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

/**
 * Helper methods to see if items contain an Ldc with a specific string.
 * 
 * @author mallard
 * @since 1.0
 */
public class LdcContains {
	/**
	 * Check if an instruction contains a string.
	 * 
	 * @param ain Instruction to check
	 * @param s String to check
	 * @return If the instruction contains the string
	 * @since 1.0
	 */
	public static boolean InstructionContains(AbstractInsnNode ain, String s) {
		if (!(ain instanceof LdcInsnNode))
			return false;

		LdcInsnNode ldc = (LdcInsnNode) ain;

		return ldc.cst.toString().contains(s);
	}

	/**
	 * Check if an instruction list contains a string
	 * 
	 * @param il Instruction list to check
	 * @param s String to check
	 * @return If the instruction list contains the string
	 * @since 1.0
	 */
	public static boolean ListContains(InsnList il, String s) {
		for (AbstractInsnNode ain : il.toArray())
			if (InstructionContains(ain, s))
				return true;

		return false;
	}

	/**
	 * Check if a method contains a string
	 * 
	 * @param mn Method to check
	 * @param s String to check
	 * @return If the method contains the string
	 * @since 1.0
	 */
	public static boolean MethodContains(MethodNode mn, String s) {
		return ListContains(mn.instructions, s);
	}

	/**
	 * Check if a class contains a string
	 * 
	 * @param cn Class to check
	 * @param s String to check
	 * @return If the class contains the string
	 * @since 1.0
	 */
	public static boolean ClassContains(ClassNode cn, String s) {
		for (MethodNode mn : cn.methods)
			if (MethodContains(mn, s))
				return true;

		return false;
	}
}
