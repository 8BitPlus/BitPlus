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

package me.themallard.bitmmo.impl.plugin.applethook;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.themallard.bitmmo.api.util.Filter;
import me.themallard.bitmmo.impl.plugin.Plugin;
import me.themallard.bitmmo.impl.plugin.SimplePlugin;

@Plugin
public class AppletHook extends SimplePlugin implements Opcodes {
	public AppletHook() {
		super("AppletHook");

		addFilter(new Filter<ClassNode>() {
			public boolean accept(ClassNode cn) {
				return cn.name != null && cn.name.equals("pulpcore/platform/applet/CoreApplet");
			}
		});

		registerDependency(AppletHookManager.class);
		registerDependency(IAppletCallback.class);
	}

	private void addGraphicsHook(MethodNode mn, boolean update) {
		String method = update ? "update" : "paint";
		String desc = mn.desc;

		InsnList insns = new InsnList();

		insns.add(new VarInsnNode(ALOAD, 1));
		insns.add(new MethodInsnNode(INVOKESTATIC, "me/themallard/bitmmo/impl/plugin/applethook/AppletHookManager",
				method, desc, false));

		mn.instructions.insertBefore(mn.instructions.get(mn.instructions.size() - 1), insns);
	}

	@Override
	public void run(ClassNode cn) {
		addGraphicsHook(cn.getMethodByName("update"), true);
		addGraphicsHook(cn.getMethodByName("paint"), false);
	}
}
