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

package maaatts.leet;

import me.themallard.bitmmo.impl.plugin.chathook.ChatHookManager;
import me.themallard.bitmmo.impl.plugin.chathook.IChatCallback;
import me.themallard.bitmmo.impl.plugin.gamecontext.GameContext;

public class LeetInject implements IChatCallback {
	private boolean leet = false;

	public LeetInject() {
		ChatHookManager.registerCallback(this);
	}

	@Override
	public boolean onChatMessage(String message) {
		if (message.equals("/leet")) {
			leet = !leet;
			StringBuilder sb = new StringBuilder();
			sb.append("[Bit+] ");
			sb.append(leet ? "Enabled" : "Disabled");
			sb.append(' ');
			sb.append("l33t speak mode.");
			GameContext.getChatWindow().addChatMessage(sb.toString());
			return true;
		}

		if (message.startsWith("/"))
			return false;

		if (leet) {
			GameContext.getChatWindow().sendChatMessage(leet(message));
			return true;
		}

		return false;
	}

	private static final String leet(String msg) {
		msg = msg.toLowerCase();

		msg = msg.replaceAll("o", "0");
		msg = msg.replaceAll("e", "3");
		msg = msg.replaceAll("i", "!");
		msg = msg.replaceAll("a", "4");

		char[] tmp = msg.toCharArray();
		for (int i = 0; i < tmp.length; i++) {
			if (i % 2 == 0)
				tmp[i] = Character.toLowerCase(tmp[i]);
			else
				tmp[i] = Character.toUpperCase(tmp[i]);
		}
		return String.valueOf(tmp);
	}

	@Override
	public void onReceiveMessage(String message) {
	}
}
