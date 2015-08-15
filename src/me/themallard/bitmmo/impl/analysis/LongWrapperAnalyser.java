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

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.hook.FieldHook;
import me.themallard.bitmmo.api.hook.MethodHook;

@SupportedHooks(fields = { "value&J" }, methods = { "get&()J" })
public class LongWrapperAnalyser extends ClassAnalyser {
	public LongWrapperAnalyser() {
		super("LongWrapper");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		MethodNode hashCode = cn.getMethodByName("hashCode");

		if (hashCode == null)
			return false;

		// aload0
		// getfield this.a
		// invokestatic java.lang.Long valueOf
		// invokevirtual hashCode
		// ireturn

		int patternLength = 4;

		for (int i = 0; i < hashCode.instructions.size() - patternLength; i++) {
			if (hashCode.instructions.get(i + 1).opcode() == GETFIELD
					&& hashCode.instructions.get(i + 2).opcode() == INVOKESTATIC
					&& hashCode.instructions.get(i + 3).opcode() == INVOKEVIRTUAL
					&& hashCode.instructions.get(i + 4).opcode() == IRETURN)
				return true;
		}

		return false;
	}

	public class ValueFieldAnalyser implements IFieldAnalyser {
		@Override
		public List<FieldHook> find(ClassNode cn) {
			List<FieldHook> list = new ArrayList<FieldHook>();

			list.add(asFieldHook(cn.fields.get(0), "value"));

			return list;
		}
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return new Builder<IFieldAnalyser>().add(new ValueFieldAnalyser());
	}

	public class GetMethodAnalyser implements IMethodAnalyser {
		@Override
		public List<MethodHook> find(ClassNode cn) {
			List<MethodHook> list = new ArrayList<MethodHook>();

			for (MethodNode mn : cn.methods) {
				if (mn.name.equals("hashCode") || mn.name.equals("equals") || mn.name.equals("<init>"))
					continue;

				list.add(asMethodHook(mn, "get"));
			}

			return list;
		}
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return new Builder<IMethodAnalyser>().add(new GetMethodAnalyser());
	}
}
