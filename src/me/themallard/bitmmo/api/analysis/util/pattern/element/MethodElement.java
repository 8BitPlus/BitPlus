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

package me.themallard.bitmmo.api.analysis.util.pattern.element;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class MethodElement implements PatternElement {
	private final MethodInsnNode insn;

	public MethodElement(MethodInsnNode insn) {
		this.insn = insn;
	}

	@Override
	public boolean matches(AbstractInsnNode ain) {
		if (!(ain instanceof MethodInsnNode))
			return false;

		if (insn.opcode() == ain.opcode() && insn.name == null && insn.owner == null)
			return true;

		MethodInsnNode min = (MethodInsnNode) ain;

		if (insn.name != null && !min.name.equals(insn.name))
			return false;

		if (insn.owner != null && !min.name.equals(insn.owner))
			return false;

		if (insn.desc != null && !min.name.equals(insn.desc))
			return false;

		return true;
	}
}
