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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.nullbool.api.util.NodeTable;
import org.objectweb.asm.tree.ClassNode;
import org.topdank.byteengineer.commons.data.DataContainer;

public class NodedContainer<C extends ClassNode> extends DataContainer<C> {
	private static final long serialVersionUID = -6169578803641192235L;

	private Map<String, C> lastMap = new HashMap<String, C>();
	private boolean invalidated;

	public NodedContainer() {
		this(16);
	}

	public NodedContainer(int cap) {
		super(cap);
	}

	public NodedContainer(Collection<C> data) {
		super(data);
	}

	@Override
	public boolean add(C c) {
		invalidated = true;
		return super.add(c);
	}

	@Override
	public boolean addAll(Collection<? extends C> c) {
		invalidated = true;
		return super.addAll(c);
	}

	@Override
	public boolean remove(Object c) {
		invalidated = true;
		return super.remove(c);
	}

	@Override
	public Map<String, C> namedMap() {
		if (invalidated) {
			invalidated = false;
			Map<String, C> nodeMap = new NodeTable<C>();
			Iterator<C> it = iterator();
			while (it.hasNext()) {
				C cn = it.next();
				if (nodeMap.containsKey(cn.name)) {
					it.remove();
				} else {
					nodeMap.put(cn.name, cn);
				}
			}
			lastMap = nodeMap;
		}
		return lastMap;
	}
}