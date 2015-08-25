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

package maaatts.chathook;

import java.util.HashSet;
import java.util.Set;

public class ChatHookManager {
	private static Set<IChatCallback> callbacks = new HashSet<IChatCallback>();

	public static void registerCallback(IChatCallback callback) {
		callbacks.add(callback);
	}

	public static void unregisterCallback(IChatCallback callback) {
		if (callbacks.contains(callback))
			callbacks.remove(callback);
	}
	
	public static void onChatMessage(IChatWindow i, String x) {
		for (IChatCallback c : callbacks)
			c.onChatMessage(i, x);
	}
	
	public static void onReceiveMessage(IChatWindow i, String x) {
		for (IChatCallback c : callbacks)
			c.onReceiveMessage(i, x);
	}
}
