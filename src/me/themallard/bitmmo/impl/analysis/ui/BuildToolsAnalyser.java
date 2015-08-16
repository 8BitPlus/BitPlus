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

package me.themallard.bitmmo.impl.analysis.ui;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.LdcContains;
import me.themallard.bitmmo.api.analysis.util.pattern.Pattern;
import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.AnyElement;
import me.themallard.bitmmo.api.analysis.util.pattern.element.FieldElement;
import me.themallard.bitmmo.api.analysis.util.pattern.element.InstructionElement;
import me.themallard.bitmmo.api.hook.MethodHook;

@SupportedHooks(fields = {}, methods = { "decrementLock&()V", "incrementLock&()V" })
public class BuildToolsAnalyser extends ClassAnalyser implements Opcodes {

	public BuildToolsAnalyser() {
		super("ui/BuildTools");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		return LdcContains.ClassContains(cn, "Z Axis Lock:");
	}

	public class AxisLockModifierAnalyser implements IMethodAnalyser {

		// TODO: Use a Filter for this?

		// pattern
		// dup
		// getfield *
		// bipush 8
		// isub
		// putfield *

		@Override
		public List<MethodHook> find(ClassNode cn) {
			List<MethodHook> list = new ArrayList<MethodHook>();

			for (MethodNode mn : cn.methods) {
				Pattern p = new PatternBuilder().add(new InstructionElement(DUP),
						new FieldElement(new FieldInsnNode(GETFIELD, null, null, null)), new InstructionElement(BIPUSH),
						new AnyElement(), new FieldElement(new FieldInsnNode(PUTFIELD, null, null, null))).build();

				int offset = p.getOffset(mn.instructions);

				if (offset != -1) {
					if (mn.instructions.get(offset + 3).opcode() == ISUB)
						list.add(asMethodHook(mn, "decrementLock"));
					else
						list.add(asMethodHook(mn, "incrementLock"));
				}
			}

			return list;
		}

	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return null;
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return new Builder<IMethodAnalyser>().add(new AxisLockModifierAnalyser());
	}
}
