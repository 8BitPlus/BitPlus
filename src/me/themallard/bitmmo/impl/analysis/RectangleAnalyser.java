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

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.LdcContains;
import me.themallard.bitmmo.api.hook.FieldHook;
import me.themallard.bitmmo.api.hook.MethodHook;

@SupportedHooks(fields = { "x&I", "y&I", "width&I", "height&I" }, methods = { "area&()I" })
public class RectangleAnalyser extends ClassAnalyser implements Opcodes {
	public RectangleAnalyser() {
		super("Rectangle");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		return LdcContains.ClassContains(cn, "Rectangle: ");
	}

	public class XYWHAnalyser implements IFieldAnalyser {
		@Override
		public List<FieldHook> find(ClassNode cn) {
			List<FieldHook> list = new ArrayList<FieldHook>();

			// Rectangle.class never changes so it is probably safe to do this
			list.add(asFieldHook(cn.fields.get(0), "x"));
			list.add(asFieldHook(cn.fields.get(1), "y"));
			list.add(asFieldHook(cn.fields.get(2), "width"));
			list.add(asFieldHook(cn.fields.get(3), "height"));

			return list;
		}
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return new Builder<IFieldAnalyser>().add(new XYWHAnalyser());
	}

	public class AreaAnalyser implements IMethodAnalyser {
		@Override
		public List<MethodHook> find(ClassNode cn) {
			List<MethodHook> list = new ArrayList<MethodHook>();

			for (MethodNode mn : cn.methods) {
				// aload
				// getfield
				// aload
				// getfield
				// imul
				// ireturn

				if (!mn.desc.equals("()I"))
					continue;

				if (mn.instructions.size() != 6)
					continue;

				if (mn.instructions.get(4).opcode() == IMUL)
					list.add(asMethodHook(mn, "area"));
			}

			return list;
		}
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return new Builder<IMethodAnalyser>().add(new AreaAnalyser());
	}
}
