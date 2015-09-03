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

package me.themallard.bitmmo.impl.plugin;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.util.pattern.Pattern;
import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.LdcElement;
import me.themallard.bitmmo.api.util.Filter;

@Plugin
public class StartMessagePlugin extends SimplePlugin {
	private static final Pattern WELCOME = new PatternBuilder()
			.add(new LdcElement(new LdcInsnNode("Welcome to 8BitMMO"), true)).build();

	public StartMessagePlugin() {
		super("StartMessage");
		addFilter(new Filter<ClassNode>() {
			@Override
			public boolean accept(ClassNode cn) {
				return getRefactoredName(cn.name) == "ui/ChatWindow";
			}
		});
	}

	@Override
	public void run(ClassNode cn) {
		MethodNode mn = cn.getMethodByName("<init>");

		int offset = WELCOME.getOffset(mn.instructions);
		
		if (offset == -1)
			return;
		
		LdcInsnNode ldc = (LdcInsnNode) mn.instructions.get(offset);
		ldc.cst = ldc.cst.toString() + "\nRunning Bit+ Development Build";
	}
}
