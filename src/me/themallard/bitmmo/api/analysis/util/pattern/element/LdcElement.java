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
import org.objectweb.asm.tree.LdcInsnNode;

public class LdcElement implements PatternElement {
	private final LdcInsnNode insn;
	private final boolean contains;

	public LdcElement(LdcInsnNode insn) {
		this(insn, false);
	}

	public LdcElement(LdcInsnNode insn, boolean contains) {
		this.insn = insn;
		this.contains = false;
	}

	@Override
	public boolean matches(AbstractInsnNode ain) {
		if (!(ain instanceof LdcInsnNode))
			return false;

		if (insn.opcode() == ain.opcode() && insn.cst == null)
			return true;

		if (insn.opcode() != ain.opcode())
			return false;

		LdcInsnNode min = (LdcInsnNode) ain;

		if (!contains) {
			if (insn.cst != null && !min.cst.equals(insn.cst))
				return false;
		} else {
			if (insn.cst != null && !min.cst.toString().contains(insn.cst.toString()))
				return false;
		}

		return true;
	}
}
