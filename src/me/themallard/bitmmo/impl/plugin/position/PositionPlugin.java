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

package me.themallard.bitmmo.impl.plugin.position;

import org.nullbool.api.util.ClassStructure;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import me.themallard.bitmmo.api.util.Filter;
import me.themallard.bitmmo.impl.plugin.Plugin;
import me.themallard.bitmmo.impl.plugin.SimplePlugin;

@Plugin
public class PositionPlugin extends SimplePlugin implements Opcodes {
	public PositionPlugin() {
		super("Position");
		addFilter(new Filter<ClassNode>() {
			@Override
			public boolean accept(ClassNode cn) {
				return getRefactoredName(cn.name) == "Position";
			}
		});

		registerDependency(ClassStructure.create(IPosition.class.getResourceAsStream("IPosition.class")));
	}

	@Override
	public void run(ClassNode cn) {
		addInterface(cn, "me/themallard/bitmmo/impl/plugin/position/IPosition");
		createGetter(cn, cn.fields.get(0), "getX");
		createGetter(cn, cn.fields.get(1), "getY");
		createGetter(cn, cn.fields.get(2), "getZ");
		createGetter(cn, cn.fields.get(3), "getRX");
		createGetter(cn, cn.fields.get(4), "getRY");
		
		createSetter(cn, cn.fields.get(0), "setX");
		createSetter(cn, cn.fields.get(1), "setY");
		createSetter(cn, cn.fields.get(2), "setZ");
		createSetter(cn, cn.fields.get(3), "setRX");
		createSetter(cn, cn.fields.get(4), "setRY");
	}
}
