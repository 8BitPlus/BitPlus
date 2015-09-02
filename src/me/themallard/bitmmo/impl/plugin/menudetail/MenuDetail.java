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

package me.themallard.bitmmo.impl.plugin.menudetail;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.Bitmmo;
import me.themallard.bitmmo.api.analysis.util.pattern.Pattern;
import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.LdcElement;
import me.themallard.bitmmo.api.util.Filter;
import me.themallard.bitmmo.impl.plugin.Plugin;
import me.themallard.bitmmo.impl.plugin.SimplePlugin;

@Plugin
public class MenuDetail extends SimplePlugin {
	public MenuDetail() {
		super("MenuDetail");
		addFilter(new Filter<ClassNode>() {
			@Override
			public boolean accept(ClassNode cn) {
				String refactored = getRefactoredName(cn.name);
				return refactored != null && refactored.equals("HTMud/MainMenu");
			}
		});
	}

	@Override
	public void run(ClassNode cn) {
		Pattern p = new PatternBuilder()
				.add(new LdcElement(
						new LdcInsnNode("Copyright 2001-2015 Archive Entertainment LLC.  All Rights Reserved.")))
				.build();

		for (MethodNode mn : cn.methods) {
			int offset = p.getOffset(mn.instructions);

			if (offset == -1)
				continue;

			LdcInsnNode ldc = (LdcInsnNode) mn.instructions.get(offset);
			String newCst = String.format("%s\nBit+ %s", ldc.cst, Bitmmo.VERSION);
			ldc.cst = newCst;
		}
	}
}
