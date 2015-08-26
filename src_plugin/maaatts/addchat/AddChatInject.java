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

package maaatts.addchat;

import maaatts.chathook.ChatHookManager;
import maaatts.chathook.IChatCallback;
import maaatts.chathook.IChatWindow;

public class AddChatInject implements IChatCallback {
	public AddChatInject() {
		ChatHookManager.registerCallback(this);
	}

	@Override
	public void onChatMessage(IChatWindow i, String x) {
		if (x.startsWith("/docrap")) {
			i.addChatMessage("very gud");
		}
	}

	@Override
	public void onReceiveMessage(IChatWindow i, String message) {
	}
}
