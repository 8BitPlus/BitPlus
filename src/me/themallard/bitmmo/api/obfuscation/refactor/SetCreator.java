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

package me.themallard.bitmmo.api.obfuscation.refactor;

import java.util.HashSet;
import java.util.Set;

import me.themallard.bitmmo.api.util.map.ValueCreator;

public class SetCreator<T> implements ValueCreator<Set<T>> {
	@Override
	public Set<T> create() {
		return new HashSet<T>();
	}
}