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

package me.themallard.bitmmo.api.util;

/**
 * Filter objects
 * 
 * @author mallard
 * @since 1.0
 */
public abstract interface Filter<T> {
	public static final Filter<Object> ACCEPT_ALL = new Filter<Object>() {
		@Override
		public boolean accept(Object t) {
			return true;
		}
	};

	/**
	 * Accept all objects.
	 * 
	 * @return New filter that will accept everything.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Filter<T> acceptAll() {
		return (Filter<T>) ACCEPT_ALL;
	}

	/**
	 * Check if filter will allow an object.
	 * 
	 * @param t Object to check
	 * @return If filter will allow
	 */
	public abstract boolean accept(T t);
}
