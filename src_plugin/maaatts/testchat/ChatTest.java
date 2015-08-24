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

package maaatts.testchat;

import org.nullbool.api.util.ClassStructure;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.themallard.bitmmo.api.analysis.util.pattern.Pattern;
import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.*;
import me.themallard.bitmmo.api.util.Filter;
import me.themallard.bitmmo.impl.plugin.Plugin;
import me.themallard.bitmmo.impl.plugin.SimplePlugin;

@Plugin
public class ChatTest extends SimplePlugin implements Opcodes {

	public ChatTest() {
		super("ChatTest");
		addFilter(new Filter<ClassNode>() {
			@Override
			public boolean accept(ClassNode cn) {
				return getRefactoredName(cn.name) == "ui/ChatWindow";
			}
		});

		registerDependency(ClassStructure.create(ChatInjector.class.getResourceAsStream("ChatInjector.class")));
	}

	@Override
	public void run(ClassNode cn) {
		Pattern p = new PatternBuilder().add(new InstructionElement(INVOKESTATIC), new AnyElement(),
				new LdcElement(new LdcInsnNode("Chat")), new InstructionElement(INVOKEVIRTUAL)).build();

		for (MethodNode mn : cn.methods) {
			if (!p.contains(mn.instructions))
				continue;

			System.out.println("pattern found" + mn.name);

			int offset = p.getOffset(mn.instructions) + 4;

			InsnList inject = new InsnList();
			inject.add(new VarInsnNode(ALOAD, 2));
			inject.add(new MethodInsnNode(INVOKESTATIC, "maaatts/testchat/ChatInjector", "onChatMessage",
					"(Ljava/lang/String;)V", false));

			mn.instructions.insert(mn.instructions.get(offset), inject);
		}
	}

}
