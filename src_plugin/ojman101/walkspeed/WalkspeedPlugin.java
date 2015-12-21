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

package ojman101.walkspeed;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.util.pattern.Pattern;
import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.InstructionElement;
import me.themallard.bitmmo.api.analysis.util.pattern.element.LdcElement;
import me.themallard.bitmmo.api.util.Filter;
import me.themallard.bitmmo.impl.plugin.Plugin;
import me.themallard.bitmmo.impl.plugin.SimplePlugin;

@Plugin
public class WalkspeedPlugin extends SimplePlugin implements Opcodes {
	
	public WalkspeedPlugin() {
		super("Walkspeed");
		registerDependency(WalkspeedInject.class);
		registerInstanceCreation("ojman101/walkspeed/WalkspeedInject");

		addFilter(new Filter<ClassNode>() {
			@Override
			public boolean accept(ClassNode cn) {
				String refactored = getRefactoredName(cn.name);
				return refactored != null && refactored.equals("Player");
			}
		});
	}

	@Override
	public void run(ClassNode cn) {
		masterWalkSpeed(cn);
		roadWalkSpeed(cn);
		sandWalkSpeed(cn);
	}
	
	public void masterWalkSpeed(ClassNode cn) {
		Pattern p = new PatternBuilder().add(new LdcElement(new LdcInsnNode(0.08D))).build();

		for (MethodNode mn : cn.methods) {
			int offset = p.getOffset(mn.instructions);

			if (offset != -1) {
				LdcInsnNode ldc = (LdcInsnNode) mn.instructions.get(offset);
				mn.instructions.insert(ldc, new MethodInsnNode(INVOKESTATIC, "ojman101/walkspeed/WalkspeedInject", "getWalkSpeed", "()D", false));
				mn.instructions.remove(ldc);
				break;
			}
		}
	}
	
	public void roadWalkSpeed(ClassNode cn) {
		Pattern p = new PatternBuilder().add(new LdcElement(new LdcInsnNode(2.0D)), new InstructionElement(DMUL),
				new InstructionElement(DSTORE)).build();

		for (MethodNode mn : cn.methods) {
			int offset = p.getOffset(mn.instructions);

			if (offset != -1) {
				LdcInsnNode ldc = (LdcInsnNode) mn.instructions.get(offset);
				mn.instructions.insert(ldc, new MethodInsnNode(INVOKESTATIC, "ojman101/walkspeed/WalkspeedInject", "getRoadWalkSpeed", "()D", false));
				mn.instructions.remove(ldc);
				break;
			}
		}
	}
	
	public void sandWalkSpeed(ClassNode cn) {
		Pattern p = new PatternBuilder().add(new LdcElement(new LdcInsnNode(2.0D)), new InstructionElement(DDIV),
				new InstructionElement(DSTORE)).build();

		for (MethodNode mn : cn.methods) {
			int offset = p.getOffset(mn.instructions);

			if (offset != -1) {
				LdcInsnNode ldc = (LdcInsnNode) mn.instructions.get(offset);
				mn.instructions.insert(ldc, new MethodInsnNode(INVOKESTATIC, "ojman101/walkspeed/WalkspeedInject", "getSandWalkSpeed", "()D", false));
				mn.instructions.remove(ldc);
				mn.instructions.get(offset + 1).setOpcode(DMUL);
				break;
			}
		}
	}
}