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

package me.themallard.bitmmo.impl.plugin;

import java.util.HashSet;
import java.util.Set;

import org.nullbool.api.util.NodeTable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import me.themallard.bitmmo.api.analysis.util.LdcContains;
import me.themallard.bitmmo.api.transformer.Transformer;

public abstract class SimplePlugin extends Transformer {
	private NodeTable<ClassNode> deps;
	private Set<String> instancesCreate;

	public SimplePlugin(String name) {
		super(name);
		deps = new NodeTable<ClassNode>();
		instancesCreate = new HashSet<String>();
	}

	protected final void registerDependency(ClassNode cn) {
		deps.put(cn);
	}

	protected final void registerInstanceCreation(String clazz) {
		instancesCreate.add(clazz);
	}

	public final NodeTable<ClassNode> getDependencies() {
		return this.deps;
	}

	@Override
	public void preRun(ClassNode cn) {
		super.preRun(cn);
		createInstances(cn);
	}

	private void createInstances(ClassNode cn) {
		for (MethodNode mn : cn.methods) {
			if (!LdcContains.MethodContains(mn, "PulpCore-Destroyer"))
				continue;

			for (String clazz : instancesCreate) {
				InsnList inject = new InsnList();

				inject.add(new TypeInsnNode(Opcodes.NEW, clazz));
				inject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, clazz, "<init>", "()V", false));

				mn.instructions.insertBefore(mn.instructions.get(0), inject);
			}
		}
	}
}
