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

package me.themallard.bitmmo.api.analysis;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;

import me.themallard.bitmmo.api.hook.FieldHook;

/**
 * @author Bibl (don't ban me pls)
 * @created 4 May 2015
 */
public abstract interface IFieldAnalyser {

	public abstract List<FieldHook> find(ClassNode cn);
}