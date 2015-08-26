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

package me.themallard.bitmmo.impl.plugin.chathook;

import org.nullbool.api.util.ClassStructure;
import org.objectweb.asm.MethodVisitor;
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
public class ChatHook extends SimplePlugin implements Opcodes {

	public ChatHook() {
		super("ChatHook");
		addFilter(new Filter<ClassNode>() {
			@Override
			public boolean accept(ClassNode cn) {
				return getRefactoredName(cn.name) == "ui/ChatWindow";
			}
		});

		registerDependency(ClassStructure.create(ChatHookManager.class.getResourceAsStream("ChatHookManager.class")));
		registerDependency(ClassStructure.create(IChatWindow.class.getResourceAsStream("IChatWindow.class")));
		registerDependency(ClassStructure.create(IChatCallback.class.getResourceAsStream("IChatCallback.class")));
	}

	@Override
	public void run(ClassNode cn) {
		hookSendMessage(cn);
		hookRecieveMessage(cn);
		createSendMessage(cn);
		createAddMessage(cn);
		addInterface(cn, "me/themallard/bitmmo/impl/plugin/chathook/IChatWindow");
	}

	private void hookSendMessage(ClassNode cn) {
		Pattern p = new PatternBuilder().add(new InstructionElement(INVOKESTATIC), new AnyElement(),
				new LdcElement(new LdcInsnNode("Chat")), new InstructionElement(INVOKEVIRTUAL)).build();

		for (MethodNode mn : cn.methods) {
			if (!p.contains(mn.instructions))
				continue;

			int offset = p.getOffset(mn.instructions) + 4;

			InsnList inject = new InsnList();
			inject.add(new VarInsnNode(ALOAD, 0));
			inject.add(new VarInsnNode(ALOAD, 2));
			inject.add(new MethodInsnNode(INVOKESTATIC, "me/themallard/bitmmo/impl/plugin/chathook/ChatHookManager",
					"onChatMessage", "(Lme/themallard/bitmmo/impl/plugin/chathook/IChatWindow;Ljava/lang/String;)V",
					false));

			mn.instructions.insert(mn.instructions.get(offset), inject);
		}
	}

	private void hookRecieveMessage(ClassNode cn) {
		Pattern p = new PatternBuilder().add(new InstructionElement(DUP), new InstructionElement(INVOKESPECIAL),
				new LdcElement(new LdcInsnNode("\n"))).build();

		for (MethodNode mn : cn.methods) {
			if (!p.contains(mn.instructions))
				continue;

			int offset = p.getOffset(mn.instructions);

			MethodInsnNode getMsgInsn = (MethodInsnNode) mn.instructions.get(offset + 5);

			InsnList inject = new InsnList();
			inject.add(new VarInsnNode(ALOAD, 0));
			inject.add(new VarInsnNode(ALOAD, 1));
			inject.add(new MethodInsnNode(INVOKEVIRTUAL, getMsgInsn.owner, getMsgInsn.name, "()Ljava/lang/String;",
					false));
			inject.add(new MethodInsnNode(INVOKESTATIC, "me/themallard/bitmmo/impl/plugin/chathook/ChatHookManager",
					"onReceiveMessage", "(Lme/themallard/bitmmo/impl/plugin/chathook/IChatWindow;Ljava/lang/String;)V",
					false));

			mn.instructions.insertBefore(mn.instructions.get(offset), inject);
		}
	}

	// too lazy to deobfuscate all network/chat stuff, use info from method
	// calls to create new send message function
	private void createSendMessage(ClassNode cn) {
		Pattern p = new PatternBuilder().add(new InstructionElement(INVOKESTATIC), new AnyElement(),
				new LdcElement(new LdcInsnNode("Chat")), new InstructionElement(INVOKEVIRTUAL)).build();

		MethodInsnNode newBuilder = null;
		MethodInsnNode setChatText = null;
		MethodInsnNode ebola1 = null;
		MethodInsnNode ebola2 = null;
		MethodInsnNode ebola3 = null;
		MethodInsnNode ebola4 = null;
		MethodInsnNode ebola5 = null;
		MethodInsnNode ebola6 = null;
		MethodInsnNode ebola7 = null;

		for (MethodNode mn : cn.methods) {
			if (!p.contains(mn.instructions))
				continue;

			int offset = p.getOffset(mn.instructions);

			newBuilder = (MethodInsnNode) mn.instructions.get(offset - 9); // newbuilder
			// then var insn of parameter, ldc or w/e
			setChatText = (MethodInsnNode) mn.instructions.get(offset - 7); // setchattext
			ebola1 = (MethodInsnNode) mn.instructions.get(offset - 6); // U a
			ebola2 = (MethodInsnNode) mn.instructions.get(offset - 5); // U p
			ebola3 = (MethodInsnNode) mn.instructions.get(offset - 4); // Player
																		// r
			ebola4 = (MethodInsnNode) mn.instructions.get(offset - 3); // setMapID
			ebola5 = (MethodInsnNode) mn.instructions.get(offset - 2); // build
			// store that crap
			ebola6 = (MethodInsnNode) mn.instructions.get(offset); // i e
			// load crap again
			// LDC "Chat"
			ebola7 = (MethodInsnNode) mn.instructions.get(offset + 3); // i a
		}

		{
			MethodVisitor mv = cn.visitMethod(ACC_PUBLIC, "sendChatMessage", "(Ljava/lang/String;)V", null, null);
			mv.visitMethodInsn(INVOKESTATIC, newBuilder.owner, newBuilder.name, newBuilder.desc, newBuilder.itf);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, setChatText.owner, setChatText.name, setChatText.desc, setChatText.itf);
			mv.visitMethodInsn(INVOKESTATIC, ebola1.owner, ebola1.name, ebola1.desc, ebola1.itf);
			mv.visitMethodInsn(INVOKEVIRTUAL, ebola2.owner, ebola2.name, ebola2.desc, ebola2.itf);
			mv.visitMethodInsn(INVOKEVIRTUAL, ebola3.owner, ebola3.name, ebola3.desc, ebola3.itf);
			mv.visitMethodInsn(INVOKEVIRTUAL, ebola4.owner, ebola4.name, ebola4.desc, ebola4.itf);
			mv.visitMethodInsn(INVOKEVIRTUAL, ebola5.owner, ebola5.name, ebola5.desc, ebola5.itf);
			mv.visitVarInsn(ASTORE, 2);
			mv.visitMethodInsn(INVOKESTATIC, ebola6.owner, ebola6.name, ebola6.desc, ebola6.itf);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitLdcInsn("Chat");
			mv.visitMethodInsn(INVOKEVIRTUAL, ebola7.owner, ebola7.name, ebola7.desc, ebola7.itf);
			mv.visitInsn(RETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
	}

	private void createAddMessage(ClassNode cn) {
		Pattern p = new PatternBuilder().add(new InstructionElement(ALOAD), new LdcElement(new LdcInsnNode("")),
				new InstructionElement(ALOAD), new InstructionElement(INVOKEVIRTUAL)).build();

		MethodInsnNode ebola1 = null;

		for (MethodNode mn : cn.methods) {
			if (!p.contains(mn.instructions))
				continue;

			int offset = p.getOffset(mn.instructions);

			ebola1 = (MethodInsnNode) mn.instructions.get(offset + 3);
		}

		{
			MethodVisitor mv = cn.visitMethod(ACC_PUBLIC, "addChatMessage", "(Ljava/lang/String;)V", null, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, ebola1.owner, ebola1.name, ebola1.desc, ebola1.itf);
			mv.visitInsn(RETURN);
			mv.visitEnd();
		}
	}
}
