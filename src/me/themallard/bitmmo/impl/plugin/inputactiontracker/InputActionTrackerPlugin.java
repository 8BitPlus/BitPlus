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

package me.themallard.bitmmo.impl.plugin.inputactiontracker;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.util.Filter;
import me.themallard.bitmmo.impl.plugin.Plugin;
import me.themallard.bitmmo.impl.plugin.SimplePlugin;

@Plugin
public class InputActionTrackerPlugin extends SimplePlugin implements Opcodes {
	public InputActionTrackerPlugin() {
		super("InputActionTracker");
		registerDependency(IInputActionTracker.class);
		addFilter(new Filter<ClassNode>() {
			@Override
			public boolean accept(ClassNode cn) {
				return cn.name.equals("HTMud/InputActionTracker");
			}
		});
	}

	@Override
	public void run(ClassNode cn) {
		addInterface(cn, "me/themallard/bitmmo/impl/plugin/inputactiontracker/IInputActionTracker");

		// again too lazy to make actual work
		// steal it from original code and hope that it works ;)
		InsnList method = null;

		for (MethodNode mn : cn.methods) {
			if (mn.desc.equals("(LHTMud/InputActionTracker$ActionType;)Z")) {
				method = mn.instructions;
				break;
			}
		}
		
		MethodVisitor mv = cn.visitMethod(ACC_PUBLIC, "isKeyDown", "(LHTMud/InputActionTracker$ActionType;)Z", null, null);
		method.accept(mv);
		mv.visitEnd();
	}
}
