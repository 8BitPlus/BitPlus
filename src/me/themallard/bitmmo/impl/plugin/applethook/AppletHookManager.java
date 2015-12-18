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

package me.themallard.bitmmo.impl.plugin.applethook;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

public class AppletHookManager {
	private static Set<IAppletCallback> callbacks = new HashSet<IAppletCallback>();

	public static void registerCallback(IAppletCallback callback) {
		callbacks.add(callback);
	}

	public static void unregisterCallback(IAppletCallback callback) {
		if (callbacks.contains(callback))
			callbacks.remove(callback);
	}
	
	/**
	 * @see java.applet.Applet#update(Graphics)
	 */
	public static void update(Graphics g) {
		for (IAppletCallback c : callbacks)
			c.update(g);
	}
	
	/**
	 * @see java.applet.Applet#paint(Graphics)
	 */
	public static void paint(Graphics g) {
		for (IAppletCallback c : callbacks)
			c.paint(g);
	}
}
