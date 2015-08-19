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

package me.themallard.bitmmo.api.analysis.util.pattern;

import java.util.List;

import org.objectweb.asm.tree.InsnList;

import me.themallard.bitmmo.api.analysis.util.pattern.element.PatternElement;

/**
 * A list of pattern elements used to search in an instruction list.
 * 
 * @see org.objectweb.asm.tree.InsnList
 * @see me.themallard.bitmmo.api.analysis.util.pattern.element.PatternElement
 * @author mallard
 * @since 1.0
 */
public class Pattern {
	final List<PatternElement> elements;

	/**
	 * Create a new pattern
	 * 
	 * @param elements List of pattern elements
	 * @see me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder
	 * @since 1.0
	 */
	public Pattern(List<PatternElement> elements) {
		this.elements = elements;
	}

	/**
	 * Check if a list contains this pattern.
	 * 
	 * @param list List to search
	 * @return If the list contains this pattern
	 * @since 1.0
	 */
	public boolean contains(InsnList list) {
		return getOffset(list) != -1;
	}

	/**
	 * Find the position of this pattern in an instruction list
	 * 
	 * @param list List to search
	 * @return Offset, or -1 if not found
	 * @since 1.0
	 */
	public int getOffset(InsnList list) {
		for (int i = 0; i < list.size() - elements.size(); i++) {
			boolean x = true;
			for (int j = 0; j < elements.size(); j++) {
				if (!elements.get(j).matches(list.get(i + j))) {
					x = false;
					break;
				}
			}

			if (x)
				return i;
		}

		return -1;
	}
}
