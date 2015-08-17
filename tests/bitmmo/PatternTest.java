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

package bitmmo;

import static org.junit.Assert.*;

import org.junit.Test;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import me.themallard.bitmmo.api.analysis.util.pattern.*;
import me.themallard.bitmmo.api.analysis.util.pattern.element.*;

public class PatternTest implements Opcodes {
	@Test
	public void testContains() {
		InsnList list = new InsnList();
		// this is just junk code
		// its contents is irrelevant
		list.add(new InsnNode(DUP));
		list.add(new InsnNode(ISUB));
		list.add(new InsnNode(IMUL));
		list.add(new MethodInsnNode(PUTFIELD, null, null, null, false));
		list.add(new InsnNode(IADD));

		Pattern correct = new PatternBuilder().add(new InstructionElement(ISUB), new InstructionElement(IMUL),
				new MethodElement(new MethodInsnNode(PUTFIELD, null, null, null, false))).build();

		Pattern incorrect = new PatternBuilder().add(new InstructionElement(ISUB), new InstructionElement(DUP)).build();

		assertEquals(true, correct.contains(list));
		assertEquals(false, incorrect.contains(list));
	}

	@Test
	public void testOffset() {
		InsnList list = new InsnList();
		// this is just junk code
		// its contents is irrelevant
		list.add(new InsnNode(DUP));
		list.add(new InsnNode(ISUB));
		list.add(new InsnNode(IMUL));
		list.add(new MethodInsnNode(PUTFIELD, null, null, null, false));
		list.add(new InsnNode(IADD));

		Pattern correct = new PatternBuilder().add(new InstructionElement(ISUB), new InstructionElement(IMUL)).build();
		Pattern correct2 = new PatternBuilder().add(new InstructionElement(IMUL),
				new MethodElement(new MethodInsnNode(PUTFIELD, null, null, null, false))).build();

		assertEquals(1, correct.getOffset(list));
		assertEquals(2, correct2.getOffset(list));
	}
}
