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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import me.themallard.bitmmo.api.util.Filter;

/**
 * Easily create lists of objects
 * 
 * @author mallard
 * @param <T> Object to build
 * @since 1.0
 */
public class Builder<T> implements Iterable<T> {
	private final List<T> sequenced;
	private final Set<Filter<T>> filters;

	/**
	 * Create an empty builder
	 * 
	 * @since 1.0
	 */
	public Builder() {
		sequenced = new ArrayList<T>();
		filters = new HashSet<Filter<T>>();
	}

	/**
	 * Create a builder with starting value
	 * 
	 * @param ts Starting value
	 * @since 1.0
	 */
	public Builder(T[] ts) {
		this();
		for (T t : ts) {
			add(t);
		}
	}

	/**
	 * Get number of objects in this builder
	 * 
	 * @return Number of objects
	 * @since 1.0
	 */
	public int size() {
		return sequenced.size();
	}

	/**
	 * Check if filter will accept object
	 * 
	 * @param t Object to check
	 * @param runall If true, carry on running after failure
	 * @return True if none of the filters failed, false otherwise.
	 * @since 1.0
	 */
	public boolean allow(T t, boolean runall) {
		if (runall) {
			boolean res = true;
			for (Filter<T> f : filters) {
				res &= f.accept(t);
			}
			return res;
		} else {
			for (Filter<T> f : filters) {
				if (!f.accept(t))
					return false;
			}
			return true;
		}
	}

	/**
	 * Add a new object
	 * 
	 * @param t Object to add
	 * @param check Check the object with filters
	 * @return This, for chaining calls
	 * @since 1.0
	 */
	public Builder<T> add(T t, boolean check) {
		if (check) {
			if (allow(t, false)) {
			}
		}

		sequenced.add(t);

		return this;
	}

	/**
	 * Add a new object and check it
	 * 
	 * @param t Object to add
	 * @return This, for chaining calls
	 * @since 1.0
	 */
	public Builder<T> add(T t) {
		add(t, true);

		return this;
	}

	/**
	 * Add multiple objects
	 * 
	 * @param ts Objects to add
	 * @return This, for chaining calls
	 * @since 1.0
	 */
	public Builder<T> addAll(@SuppressWarnings("unchecked") T... ts) {
		return addAll(ts, false);
	}

	/**
	 * Add multiple objects
	 * 
	 * @param ts
	 * @param checkAll
	 * @return This, for chaining calls
	 * @since 1.0
	 */
	public Builder<T> addAll(T[] ts, boolean checkAll) {
		for (T t : ts)
			add(t);

		return this;
	}

	/**
	 * Replace everything that matches the filter with another object
	 * 
	 * @param filter Filter to check
	 * @param t Object to replace with
	 * @return This, for chaining calls
	 * @since 1.0
	 */
	public Builder<T> replace(Filter<T> filter, T t) {
		ListIterator<T> it = sequenced.listIterator();
		while (it.hasNext()) {
			T next = it.next();
			if (filter.accept(next)) {
				it.set(t);
			}
		}

		return this;
	}

	/**
	 * Remove everything that matches the filter and insert object after
	 * 
	 * @param filter Filter to check with
	 * @param t Object to insert
	 * @return This, for chaining calls
	 * @since 1.0
	 */
	public Builder<T> replaceAfter(Filter<T> filter, T t) {
		ListIterator<T> it = sequenced.listIterator();
		while (it.hasNext()) {
			T next = it.next();
			if (filter.accept(next)) {
				it.remove();
			}
		}

		sequenced.add(t);

		return this;
	}

	/**
	 * Remove everything that matches a filter
	 * 
	 * @param filter Filter to check with
	 * @return This, for chaining calls
	 * @since 1.0
	 */
	public Builder<T> remove(Filter<T> filter) {
		ListIterator<T> it = sequenced.listIterator();
		while (it.hasNext()) {
			T next = it.next();
			if (filter.accept(next)) {
				it.remove();
			}
		}

		return this;
	}

	/**
	 * Remove everything that doesn't match the filters
	 * 
	 * @return This, for chaining calls
	 * @since 1.0
	 */
	public Builder<T> sort() {
		ListIterator<T> it = sequenced.listIterator();
		while (it.hasNext()) {
			T next = it.next();
			if (!allow(next, false))
				it.remove();
		}

		return this;
	}

	/**
	 * Add a new filter
	 * 
	 * @param filter Filter to add
	 * @return This, for chaining calls
	 * @since 1.0
	 */
	public Builder<T> addFilter(Filter<T> filter) {
		filters.add(filter);
		return this;
	}

	/**
	 * Get builder contents as list
	 * 
	 * @return A list containing the contents of this build
	 * @since 1.0
	 */
	public List<T> asList() {
		return Collections.unmodifiableList(sequenced);
	}

	/**
	 * Get builder contents as array
	 * 
	 * @param a - the array into which the elements of this list are to be
	 *            stored, if it is big enough; otherwise, a new array of the
	 *            same runtime type is allocated for this purpose.
	 * @return An array containing the contents of this builder
	 * @since 1.0
	 */
	public T[] asArray(T[] t) {
		return sequenced.toArray(t);
	}

	@Override
	public Iterator<T> iterator() {
		return sequenced.listIterator();
	}
}
