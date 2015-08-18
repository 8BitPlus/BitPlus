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
import org.objectweb.asm.tree.FieldInsnNode;

/**
 * Matches with a field element, provided it has the right parameters.
 * 
 * @author mallard
 * @since 1.0
 * @see me.themallard.bitmmo.api.analysis.util.pattern.element.PatternElement
 */
public class FieldElement implements PatternElement {
	private final FieldInsnNode insn;

	/**
	 * Create a new Field Element.
	 * 
	 * @param insn Field Node. Use null as a wildcard parameter.
	 */
	public FieldElement(FieldInsnNode insn) {
		this.insn = insn;
	}

	@Override
	public boolean matches(AbstractInsnNode ain) {
		if (!(ain instanceof FieldInsnNode))
			return false;

		if (insn.opcode() == ain.opcode() && insn.name == null && insn.owner == null && insn.desc == null)
			return true;

		if (insn.opcode() != ain.opcode())
			return false;

		FieldInsnNode fin = (FieldInsnNode) ain;

		if (insn.name != null && !fin.name.equals(insn.name))
			return false;

		if (insn.owner != null && !fin.name.equals(insn.owner))
			return false;

		if (insn.desc != null && !fin.name.equals(insn.desc))
			return false;

		return true;
	}
}
