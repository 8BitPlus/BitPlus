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

package me.themallard.bitmmo.impl.plugin.tickhook;

import java.util.HashSet;
import java.util.Set;

public class TickHookManager {
	private static Set<ITickCallback> callbacks = new HashSet<ITickCallback>();

	public static void registerCallback(ITickCallback callback) {
		callbacks.add(callback);
	}

	public static void unregisterCallback(ITickCallback callback) {
		if (callbacks.contains(callback))
			callbacks.remove(callback);
	}

	public static void preTick() {
		for (ITickCallback c : callbacks)
			c.preTick();
	}
	
	public static void postTick() {
		for (ITickCallback c : callbacks)
			c.postTick();
	}
}
