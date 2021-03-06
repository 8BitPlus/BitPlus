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

public class Builder<T> implements Iterable<T> {
	private final List<T> sequenced;
	private final Set<Filter<T>> filters;

	public Builder() {
		sequenced = new ArrayList<T>();
		filters = new HashSet<Filter<T>>();
	}

	public Builder(T[] ts) {
		this();
		for (T t : ts) {
			add(t);
		}
	}

	public int size() {
		return sequenced.size();
	}

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

	public Builder<T> add(T t, boolean check) {
		if (check) {
			if (allow(t, false)) {
			}
		}

		sequenced.add(t);

		return this;
	}

	public Builder<T> add(T t) {
		add(t, true);
		
		return this;
	}

	public Builder<T> addAll(@SuppressWarnings("unchecked") T... ts) {
		return addAll(ts, false);
	}

	public Builder<T> addAll(T[] ts, boolean checkAll) {
		for (T t : ts)
			add(t);

		return this;
	}

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

	public Builder<T> sort() {
		ListIterator<T> it = sequenced.listIterator();
		while (it.hasNext()) {
			T next = it.next();
			if (!allow(next, false))
				it.remove();
		}

		return this;
	}

	public Builder<T> addFilter(Filter<T> filter) {
		filters.add(filter);
		return this;
	}

	public List<T> asList() {
		return Collections.unmodifiableList(sequenced);
	}

	public T[] asArray(T[] t) {
		return sequenced.toArray(t);
	}

	@Override
	public Iterator<T> iterator() {
		return sequenced.listIterator();
	}
}
