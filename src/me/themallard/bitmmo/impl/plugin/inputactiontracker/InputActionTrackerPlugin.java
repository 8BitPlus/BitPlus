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
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

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

	private void createIsKeyDown(ClassNode cn) {
		// again too lazy to make actual work
		// steal it from original code and hope that it works ;)

		for (MethodNode mn : cn.methods) {
			if (mn.desc.equals("(LHTMud/InputActionTracker$ActionType;)Z")) {
				InsnList method = mn.instructions;

				MethodVisitor mv = cn.visitMethod(ACC_PUBLIC, "isKeyDown", mn.desc, null, null);
				method.accept(mv);
				mv.visitEnd();
				break;
			}
		}
	}

	private void createSetKeyDown(ClassNode cn) {
		InsnList insn = new InsnList();

		MethodNode mn = new MethodNode(cn, ACC_PUBLIC, "setKeyDown", "(LHTMud/InputActionTracker$ActionType;Z)V", null,
				null);

		// ((Key) this.enumMap.get(actionType)).setKeyPressed(value);

		// aload0
		// getfield this.enumMap Ljava/util/EnumMap;
		// aload1
		// invokevirtual \
		// java/util/EnumMap.get((Ljava/lang/Object;)Ljava/lang/Object;);
		// checkcast Key
		// aload2
		// invokevirtual Key.setKeyPressed((Z)V);
		// return

		mn.visitMaxs(3, 3);
		
		insn.add(new VarInsnNode(ALOAD, 0));

		FieldNode enumMap = cn.getField(null, "Ljava/util/EnumMap;");
		insn.add(new FieldInsnNode(GETFIELD, cn.name, enumMap.name, enumMap.desc));

		insn.add(new VarInsnNode(ALOAD, 1));

		insn.add(new MethodInsnNode(INVOKEVIRTUAL, "java/util/EnumMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;",
				false));

		insn.add(new TypeInsnNode(CHECKCAST, "Key"));

		insn.add(new VarInsnNode(ILOAD, 2));

		insn.add(new MethodInsnNode(INVOKEVIRTUAL, "Key", "setKeyPressed", "(Z)V", false));

		insn.add(new InsnNode(RETURN));

		mn.instructions.insert(insn);

		cn.methods.add(mn);
	}

	@Override
	public void run(ClassNode cn) {
		addInterface(cn, "me/themallard/bitmmo/impl/plugin/inputactiontracker/IInputActionTracker");

		createIsKeyDown(cn);
		createSetKeyDown(cn);
	}
}
