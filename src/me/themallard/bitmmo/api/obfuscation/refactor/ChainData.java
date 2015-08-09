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
import java.util.Iterator;
import java.util.Set;

import org.objectweb.asm.tree.MethodNode;

public class ChainData {

	private final MethodNode centre;
	private final Set<MethodNode> supers;
	private final Set<MethodNode> delegates;
	private final Set<MethodNode> aggregates;

	public ChainData(MethodNode m, Set<MethodNode> supers, Set<MethodNode> delegates) {
		this.centre = m;
		this.supers = supers;
		this.delegates = delegates;

		this.supers.remove(m);
		this.delegates.remove(m);

		aggregates = new HashSet<MethodNode>();
		aggregates.addAll(supers);
		aggregates.addAll(delegates);
	}

	public Set<MethodNode> getSupers() {
		return supers;
	}

	public Set<MethodNode> getDelegates() {
		return delegates;
	}

	public Set<MethodNode> getAggregates() {
		return aggregates;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Centre: ").append(centre.key()).append("   (").append(supers.size()).append(", ")
				.append(delegates.size()).append(")");

		boolean sups = supers.size() > 0;
		boolean dels = delegates.size() > 0;
		if (sups || dels) {
			sb.append("\n");
		}

		if (sups) {
			sb.append("   >S>U>P>E>R>S>\n");
			Iterator<MethodNode> it = supers.iterator();
			while (it.hasNext()) {
				MethodNode sup = it.next();
				sb.append("    ").append(sup.key());
				if (it.hasNext() || dels)
					sb.append("\n");
			}
		}

		if (dels) {
			sb.append("   >D>E>L>E>G>A>T>E>S>\n");
			Iterator<MethodNode> it = delegates.iterator();
			while (it.hasNext()) {
				MethodNode del = it.next();
				sb.append("    ").append(del.key());
				if (it.hasNext())
					sb.append("\n");
			}
		}

		return sb.toString();
	}
}