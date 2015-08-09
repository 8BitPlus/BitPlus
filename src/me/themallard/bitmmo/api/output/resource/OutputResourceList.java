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

package me.themallard.bitmmo.api.output.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import me.themallard.bitmmo.api.util.Filter;

public class OutputResourceList implements Iterable<OutputResource>, Collection<OutputResource> {
	private List<OutputResource> list;

	public OutputResourceList() {
		list = new ArrayList<OutputResource>();
	}

	public OutputResourceList(List<OutputResource> res) {
		this();
		addAll(res);
	}

	public OutputResourceList filter(Filter<OutputResource> f) {
		List<OutputResource> tmp = new ArrayList<OutputResource>();

		for (OutputResource or : tmp) {
			if (f.accept(or))
				tmp.add(or);
		}

		return new OutputResourceList(tmp);
	}

	@Override
	public Iterator<OutputResource> iterator() {
		return list.iterator();
	}

	@Override
	public boolean add(OutputResource e) {
		return list.add(e);
	}

	public boolean addAll(OutputResource... e) {
		return addAll(Arrays.asList(e));
	}

	public boolean addAll(OutputResourceList e) {
		return addAll(e);
	}

	@Override
	public boolean addAll(Collection<? extends OutputResource> c) {
		return list.addAll(c);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}
}
