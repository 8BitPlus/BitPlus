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

package me.themallard.bitmmo.impl.analysis;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.pattern.Pattern;
import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.*;
import me.themallard.bitmmo.api.hook.FieldHook;

@SupportedHooks(fields = { "rx&I", "ry&I", "x&D", "y&D", "z&D" }, methods = {})
public class PositionAnalyser extends ClassAnalyser {
	public PositionAnalyser() {
		super("Position");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		Pattern p = new PatternBuilder().add(new InstructionElement(DUP), new InstructionElement(INVOKESPECIAL),
				new LdcElement(new LdcInsnNode("R(")), new InstructionElement(INVOKEVIRTUAL), new AnyElement(),
				new InstructionElement(GETFIELD)).build();

		for (MethodNode mn : cn.methods) {
			if (p.contains(mn.instructions))
				return true;
		}

		return false;
	}

	public class FieldPositionAnalyser implements IFieldAnalyser {
		@Override
		public List<FieldHook> find(ClassNode cn) {
			List<FieldHook> list = new ArrayList<FieldHook>();

			MethodNode mn = cn.getMethodByName("toString");

			int n = 0;
			for (AbstractInsnNode ain : mn.instructions.toArray()) {
				if (!(ain instanceof FieldInsnNode))
					continue;

				FieldInsnNode fin = (FieldInsnNode) ain;

				if (fin.opcode() != GETFIELD)
					continue;

				switch (n) {
				case 0:
					list.add(asFieldHook(fin, "rx"));
					break;
				case 1:
					list.add(asFieldHook(fin, "ry"));
					break;
				case 2:
					list.add(asFieldHook(fin, "x"));
					break;
				case 3:
					list.add(asFieldHook(fin, "y"));
					break;
				case 4:
					list.add(asFieldHook(fin, "z"));
					break;
				default:
					break;
				}

				n++;
			}

			return list;
		}
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return new Builder<IFieldAnalyser>().add(new FieldPositionAnalyser());
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return null;
	}
}
