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

package me.themallard.bitmmo.impl.plugin.playerhook;

import org.nullbool.api.util.ClassStructure;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import com.sun.xml.internal.ws.org.objectweb.asm.Type;

import me.themallard.bitmmo.api.util.Filter;
import me.themallard.bitmmo.impl.plugin.Plugin;
import me.themallard.bitmmo.impl.plugin.SimplePlugin;

@Plugin
public class PlayerPlugin extends SimplePlugin implements Opcodes {
	public PlayerPlugin() {
		super("PlayerHook");
		addFilter(new Filter<ClassNode>() {
			@Override
			public boolean accept(ClassNode cn) {
				return getRefactoredName(cn.name) == "Player";
			}
		});

		registerDependency(ClassStructure.create(IPlayer.class.getResourceAsStream("IPlayer.class")));
	}

	@Override
	public void run(ClassNode cn) {
		MethodVisitor mv = cn.visitMethod(Opcodes.ACC_PUBLIC, "getPosition",
				"()" + "Lme/themallard/bitmmo/impl/plugin/position/IPosition;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "Entity", "position", "LPosition;");
		mv.visitInsn(Type.getType("Lme/themallard/bitmmo/impl/plugin/position/IPosition;").getOpcode(Opcodes.IRETURN));
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}
}
