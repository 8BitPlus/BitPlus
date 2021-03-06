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

package me.themallard.bitmmo.impl;

import me.themallard.bitmmo.api.Revision;
import me.themallard.bitmmo.api.util.Filter;

public class RevisionFilter implements Filter<Revision> {
	private final int min;

	public RevisionFilter(int min) {
		this.min = min;
	}

	@Override
	public boolean accept(Revision t) {
		int r = -1;

		try {
			r = Integer.parseInt(t.getName());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}

		return r >= min;
	}
}
