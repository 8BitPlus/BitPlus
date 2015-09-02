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

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.pattern.Pattern;
import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.AnyElement;
import me.themallard.bitmmo.api.analysis.util.pattern.element.FieldElement;
import me.themallard.bitmmo.api.analysis.util.pattern.element.InstructionElement;
import me.themallard.bitmmo.api.analysis.util.pattern.element.MethodElement;

@SupportedHooks(fields = {}, methods = {})
public class RifleAnalyser extends ClassAnalyser {
	public RifleAnalyser() {
		super("Rifle");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		Pattern p = new PatternBuilder()
				.add(new MethodElement(new MethodInsnNode(INVOKESPECIAL, null, null, null, false)),
						new MethodElement(new MethodInsnNode(INVOKESTATIC, null, null, null, false)), new AnyElement(),
						new AnyElement(), new FieldElement(new FieldInsnNode(GETSTATIC, null, null, null)),
						new MethodElement(new MethodInsnNode(INVOKEVIRTUAL, null, null, null, false)),
						new InstructionElement(IFEQ), new AnyElement(),
						new MethodElement(new MethodInsnNode(INVOKEVIRTUAL, null, null, null, false)))
				.build();

		for (MethodNode mn : cn.methods)
			if (p.contains(mn.instructions))
				return true;

		return false;
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return null;
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return null;
	}
}
