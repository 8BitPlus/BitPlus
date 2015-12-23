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

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.pattern.Pattern;
import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.InstructionElement;
import me.themallard.bitmmo.api.hook.FieldHook;

@SupportedHooks(fields = {"keyPressed&Z"}, methods = {})
public class KeyAnalyser extends ClassAnalyser {
	private static final Pattern p = new PatternBuilder().add(new InstructionElement(ALOAD),
			new InstructionElement(GETFIELD), new InstructionElement(PUTFIELD), new InstructionElement(ALOAD),
			new InstructionElement(ICONST_0), new InstructionElement(PUTFIELD), new InstructionElement(ALOAD),
			new InstructionElement(GETFIELD), new InstructionElement(ICONST_M1), new InstructionElement(IF_ICMPEQ))
			.build();

	@Override
	protected boolean matches(ClassNode cn) {
		return p.contains(cn);
	}

	public KeyAnalyser() {
		super("Key");
	}

	public class KeyPressedAnalyser implements IFieldAnalyser {
		@Override
		public List<FieldHook> find(ClassNode cn) {
			List<FieldHook> list = new ArrayList<FieldHook>();
			list.add(asFieldHook(cn.fields.get(2), "keyPressed"));
			return list;
		}
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return new Builder<IFieldAnalyser>().add(new KeyPressedAnalyser());
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		// TODO Auto-generated method stub
		return null;
	}
}
