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
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.LdcElement;
import me.themallard.bitmmo.api.hook.FieldHook;

@SupportedHooks(fields = { "position&Lbm;" }, methods = {})
public class EntityAnalyser extends ClassAnalyser {
	public EntityAnalyser() {
		super("Entity");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		return new PatternBuilder().add(new LdcElement(new LdcInsnNode("Odd, while moving an entity "))).build()
				.contains(cn);
	}

	public class PositionAnalyser implements IFieldAnalyser {
		@Override
		public List<FieldHook> find(ClassNode cn) {
			List<FieldHook> list = new ArrayList<FieldHook>();

			for (FieldNode fn : cn.fields) {
				String refactored = getRefactoredNameByType(fn.desc);

				if (refactored == null)
					continue;

				if (refactored.equals("Position"))
					list.add(asFieldHook(fn, "position"));
			}

			return list;
		}
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return new Builder<IFieldAnalyser>().add(new PositionAnalyser());
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return null;
	}
}
