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

package maaatts.timebot;

import org.nullbool.api.util.ClassStructure;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import me.themallard.bitmmo.api.analysis.util.LdcContains;
import me.themallard.bitmmo.api.util.Filter;
import me.themallard.bitmmo.impl.plugin.Plugin;
import me.themallard.bitmmo.impl.plugin.SimplePlugin;

// depends on ChatHook plugin

@Plugin
public class TimeBot extends SimplePlugin {
	public TimeBot() {
		super("TimeBot");
		registerDependency(ClassStructure.create(TimeInject.class.getResourceAsStream("TimeInject.class")));

		addFilter(new Filter<ClassNode>() {
			@Override
			public boolean accept(ClassNode cn) {
				return getRefactoredName(cn.name) == "HTMud/MainMenu";
			}
		});
	}

	@Override
	public void run(ClassNode cn) {
		for (MethodNode mn : cn.methods) {
			if (!LdcContains.MethodContains(mn, "vSprint"))
				continue;

			InsnList inject = new InsnList();

			inject.add(new TypeInsnNode(Opcodes.NEW, "maaatts/timebot/TimeInject"));
			inject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "maaatts/timebot/TimeInject", "<init>", "()V", false));

			mn.instructions.insertBefore(mn.instructions.get(0), inject);
		}
	}
}
