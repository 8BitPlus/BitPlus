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
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;
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
import me.themallard.bitmmo.api.analysis.util.pattern.element.LdcElement;
import me.themallard.bitmmo.api.hook.FieldHook;
import me.themallard.bitmmo.api.hook.MethodHook;

@SupportedHooks(fields = { "instance&Lat;" }, methods = { "getInstance&()Lat;" })
public class BGMDownloaderAnalyser extends ClassAnalyser {
	public BGMDownloaderAnalyser() {
		super("BGMDownloader");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		return new PatternBuilder().add(new LdcElement(new LdcInsnNode("BGM Music Download: Complete!"))).build().contains(cn);
	}

	public class InstanceAnalyser implements IFieldAnalyser {
		@Override
		public List<FieldHook> find(ClassNode cn) {
			List<FieldHook> list = new ArrayList<FieldHook>();

			for (FieldNode fn : cn.fields) {
				String refactored = getRefactoredNameByType(fn.desc);

				if (refactored == null)
					continue;

				if (refactored.equals("BGMDownloader"))
					list.add(asFieldHook(fn, "instance"));
			}

			return list;
		}
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return new Builder<IFieldAnalyser>().add(new InstanceAnalyser());
	}

	public class GetInstanceAnalyser implements IMethodAnalyser {
		@Override
		public List<MethodHook> find(ClassNode cn) {
			List<MethodHook> list = new ArrayList<MethodHook>();

			Pattern p = new PatternBuilder().add(new FieldElement(new FieldInsnNode(GETSTATIC, null, null, null)),
					new InstructionElement(IFNONNULL), new AnyElement(), new InstructionElement(DUP)).build();

			for (MethodNode mn : cn.methods) {
				if (p.contains(mn.instructions))
					list.add(asMethodHook(mn, "getInstance"));
			}

			return list;
		}
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return new Builder<IMethodAnalyser>().add(new GetInstanceAnalyser());
	}
}
