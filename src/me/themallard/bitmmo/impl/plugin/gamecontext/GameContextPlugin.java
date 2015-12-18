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

package me.themallard.bitmmo.impl.plugin.gamecontext;

import org.nullbool.api.util.ClassStructure;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.LdcElement;
import me.themallard.bitmmo.api.util.Filter;
import me.themallard.bitmmo.impl.plugin.Plugin;
import me.themallard.bitmmo.impl.plugin.SimplePlugin;

@Plugin
public class GameContextPlugin extends SimplePlugin implements Opcodes {
	public GameContextPlugin() {
		super("GameContext");
		registerDependency(ClassStructure.create(GameContext.class.getResourceAsStream("GameContext.class")));

		addFilter(new Filter<ClassNode>() {
			@Override
			public boolean accept(ClassNode cn) {
				return getRefactoredName(cn.name) == "ui/ChatWindow" || getRefactoredName(cn.name) == "Player" ||
						cn.name.equals("HTMud/InputActionTracker");
			}
		});
	}

	@Override
	public void run(ClassNode cn) {
		switch (getRefactoredName(cn.name)) {
		case "ui/ChatWindow":
			doChatWindow(cn);
			break;
		case "Player":
			doPlayer(cn);
			break;
		case "HTMud/InputActionTracker":
			doInputTracker(cn);
		default:
			break;
		}
	}
	
	private void doInputTracker(ClassNode cn) {
		MethodNode mn = cn.getMethodByName("<init>");
		mn.instructions.insert(mn.instructions.get(mn.instructions.size() - 2),
				createContextCall(mn, "setInputActionTracker", "me/themallard/bitmmo/impl/plugin/inputactiontracker/IInputActionTracker"));
	}

	private void doPlayer(ClassNode cn) {
		addInterface(cn, "me/themallard/bitmmo/impl/plugin/playerhook/IPlayer");

		for (MethodNode mn : cn.methods) {
			if (!mn.name.equals("<init>"))
				continue;

			if (!new PatternBuilder().add(new LdcElement(new LdcInsnNode("char-color-overlay.png"))).build()
					.contains(mn.instructions))
				continue;

			mn.instructions.insert(mn.instructions.get(mn.instructions.size() - 2),
					createContextCall(mn, "setPlayer", "me/themallard/bitmmo/impl/plugin/playerhook/IPlayer"));

			break;
		}
	}

	private void doChatWindow(ClassNode cn) {
		MethodNode mn = cn.getMethodByName("<init>");
		mn.instructions.insert(mn.instructions.get(mn.instructions.size() - 2),
				createContextCall(mn, "setChatWindow", "me/themallard/bitmmo/impl/plugin/chathook/IChatWindow"));
	}

	private InsnList createContextCall(MethodNode mn, String contextCallName, String type) {
		InsnList list = new InsnList();

		String desc = "(L" + type + ";)V)";

		list.add(new VarInsnNode(ALOAD, 0));
		list.add(new MethodInsnNode(INVOKESTATIC, "me/themallard/bitmmo/impl/plugin/gamecontext/GameContext",
				contextCallName, desc, false));

		return list;
	}
}
