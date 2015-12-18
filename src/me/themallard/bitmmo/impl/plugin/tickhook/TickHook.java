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

package me.themallard.bitmmo.impl.plugin.tickhook;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.util.pattern.Pattern;
import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.LdcElement;
import me.themallard.bitmmo.api.util.Filter;
import me.themallard.bitmmo.impl.plugin.Plugin;
import me.themallard.bitmmo.impl.plugin.SimplePlugin;

@Plugin
public class TickHook extends SimplePlugin implements Opcodes {
	private static final Pattern PATTERN = new PatternBuilder()
			.add(new LdcElement(new LdcInsnNode("Pulpcore lying as to elapsed time... "))).build();

	public TickHook() {
		super("TickHook");
		registerDependency(TickHookManager.class);
		registerDependency(ITickCallback.class);
		addFilter(new Filter<ClassNode>() {
			@Override
			public boolean accept(ClassNode cn) {
				return getRefactoredName(cn.name) == "GameThread";
			}
		});
	}

	private void registerPreTickHook(MethodNode mn) {
		int offset = PATTERN.getOffset(mn.instructions) - 20;

		mn.instructions.insert(mn.instructions.get(offset), new MethodInsnNode(INVOKESTATIC,
				"me/themallard/bitmmo/impl/plugin/tickhook/TickHookManager", "preTick", "()V", false));
	}

	@Override
	public void run(ClassNode cn) {
		for (MethodNode mn : cn.methods) {
			if (PATTERN.contains(mn.instructions)) {
				registerPreTickHook(mn);
				break;
			}
		}
	}
}
