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

package maaatts.timebot;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.themallard.bitmmo.impl.plugin.chathook.ChatHookManager;
import me.themallard.bitmmo.impl.plugin.chathook.IChatCallback;
import me.themallard.bitmmo.impl.plugin.chathook.IChatWindow;

public class TimeInject implements IChatCallback {
	public TimeInject() {
		ChatHookManager.registerCallback(this);
	}

	@Override
	public void onChatMessage(IChatWindow i, String x) {
	}

	@Override
	public void onReceiveMessage(IChatWindow i, String message) {
		if (message.contains("!time"))
			i.sendChatMessage("The time is " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ".");
	}
}
