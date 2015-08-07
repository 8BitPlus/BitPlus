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