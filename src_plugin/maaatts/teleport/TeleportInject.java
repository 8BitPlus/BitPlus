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

package maaatts.teleport;

import me.themallard.bitmmo.impl.plugin.chathook.ChatHookManager;
import me.themallard.bitmmo.impl.plugin.chathook.IChatCallback;
import me.themallard.bitmmo.impl.plugin.gamecontext.GameContext;
import me.themallard.bitmmo.impl.plugin.position.IPosition;

public class TeleportInject implements IChatCallback {
	public TeleportInject() {
		ChatHookManager.registerCallback(this);
	}

	@Override
	public void onChatMessage(String message) {
		if (message.startsWith("/blink")) {
			String subst = message.substring("/blink ".length());
			try {
				int[] pos = parsePosition(subst);
				IPosition orig = GameContext.getPlayer().getPosition();
				GameContext.getPlayer().getPosition().setX(orig.getX() + pos[0]);
				GameContext.getPlayer().getPosition().setY(orig.getY() + pos[1]);
				GameContext.getPlayer().getPosition().setZ(orig.getZ() + pos[2]);
			} catch (Exception e) {
				GameContext.getChatWindow().addChatMessage("Invalid command usage!\nTry /blink x y z");
				return;
			}
			return;
		}

		if (message.startsWith("/goto")) {
			String subst = message.substring("/goto ".length());
			try {
				int[] pos = parsePosition(subst);
				GameContext.getPlayer().getPosition().setX(pos[0]);
				GameContext.getPlayer().getPosition().setY(pos[1]);
				GameContext.getPlayer().getPosition().setZ(pos[2]);
			} catch (Exception e) {
				GameContext.getChatWindow().addChatMessage("Invalid command usage!\nTry /goto x y z");
				return;
			}
			return;
		}

		if (message.startsWith("/xyz")) {
			GameContext.getChatWindow().addChatMessage(GameContext.getPlayer().getPosition().toString());
		}
	}

	@Override
	public void onReceiveMessage(String message) {
	}

	private int[] parsePosition(String pos) throws Exception {
		String[] split = pos.split(" ");
		if (split.length != 3)
			throw new Exception();

		int[] ret = new int[3];

		try {
			for (int i = 0; i < 3; i++)
				ret[i] = Integer.parseInt(split[i]);
		} catch (NumberFormatException e) {
			throw new Exception();
		}

		return ret;
	}
}
