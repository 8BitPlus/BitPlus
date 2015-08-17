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

import me.themallard.bitmmo.api.analysis.util.pattern.element.*;

public class PatternElementTest implements Opcodes {
	@Test
	public void testAnyElement() {
		AnyElement e = new AnyElement();
		assertEquals(true, e.matches(null));
	}

	@Test
	public void testFieldElement() {
		FieldInsnNode correct = new FieldInsnNode(PUTFIELD, null, null, null);
		FieldInsnNode correct2 = new FieldInsnNode(PUTFIELD, null, "good", null);
		FieldInsnNode incorrect = new FieldInsnNode(GETFIELD, null, "bad", null);

		FieldElement e = new FieldElement(correct);

		assertEquals(true, e.matches(correct));
		assertEquals(true, e.matches(correct2));
		assertEquals(false, e.matches(incorrect));
	}

	@Test
	public void testInstructionElement() {
		InsnNode correct = new InsnNode(DUP);
		InsnNode incorrect = new InsnNode(ISUB);
		
		InstructionElement e = new InstructionElement(DUP);
		
		assertEquals(true, e.matches(correct));
		assertEquals(false, e.matches(incorrect));
	}
	
	@Test
	public void testMethodElement() {
		MethodInsnNode correct = new MethodInsnNode(PUTFIELD, null, null, null, false);
		MethodInsnNode correct2 = new MethodInsnNode(PUTFIELD, null, "good", null, false);
		MethodInsnNode incorrect = new MethodInsnNode(GETFIELD, null, "bad", null, false);

		MethodElement e = new MethodElement(correct);

		assertEquals(true, e.matches(correct));
		assertEquals(true, e.matches(correct2));
		assertEquals(false, e.matches(incorrect));
	}
}
