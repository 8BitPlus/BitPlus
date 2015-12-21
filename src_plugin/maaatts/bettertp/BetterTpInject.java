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

package maaatts.bettertp;

import me.themallard.bitmmo.impl.plugin.chathook.ChatHookManager;
import me.themallard.bitmmo.impl.plugin.chathook.IChatCallback;
import me.themallard.bitmmo.impl.plugin.gamecontext.GameContext;
import me.themallard.bitmmo.impl.plugin.position.IPosition;
import me.themallard.bitmmo.impl.plugin.tickhook.ITickCallback;
import me.themallard.bitmmo.impl.plugin.tickhook.TickHookManager;
import ojman101.help.HelpManager;

public class BetterTpInject implements IChatCallback, ITickCallback {
	private static final double delta = 25;

	private double dX = 0;
	private double dY = 0;
	private double dZ = 0;

	public BetterTpInject() {
		ChatHookManager.registerCallback(this);
		TickHookManager.registerCallback(this);
		HelpManager.addHelp("bblink", "<x> <y> <z> | Add X Y Z to your position.");
	}

	@Override
	public void preTick() {
		if (GameContext.getPlayer() == null) return;

		IPosition orig = GameContext.getPlayer().getPosition();

		if (dX != 0) {
			double a = Math.min(delta, Math.abs(dX));

			if (dX < 0) a *= -1;

			GameContext.getPlayer().getPosition().setX(orig.getX() + a);

			dX -= a;
		}

		if (dY != 0) {
			double a = Math.min(delta, Math.abs(dY));

			if (dY < 0) a *= -1;

			GameContext.getPlayer().getPosition().setY(orig.getY() + a);

			dY -= a;
		}

		if (dZ != 0) {
			double a = Math.min(delta, Math.abs(dZ));

			if (dZ < 0) a *= -1;

			GameContext.getPlayer().getPosition().setZ(orig.getZ() + a);

			dZ -= a;
		}
	}

	@Override
	public boolean onChatMessage(String message) {
		if (message.startsWith("/bblink")) {
			String subst = message.substring("/bblink ".length());
			try {
				int[] pos = parsePosition(subst);
				GameContext.getChatWindow()
						.addChatMessage(String.format("[Bit+] Blinking +(%d, %d, %d)", pos[0], pos[1], pos[2]));
				dX = pos[0];
				dY = pos[1];
				dZ = pos[2];
			} catch (Exception e) {
				GameContext.getChatWindow().addChatMessage("[Bit+] Invalid command usage!\nTry /bblink x y z");
			}
			return true;
		}

		return false;
	}

	@Override
	public void onReceiveMessage(String message) {
	}

	private static int[] parsePosition(String pos) throws Exception {
		String[] split = pos.split(" ");
		if (split.length != 3) throw new Exception();

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
