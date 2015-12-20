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

package maaatts.noclip;

import HTMud.InputActionTracker$ActionType;
import me.themallard.bitmmo.impl.plugin.chathook.ChatHookManager;
import me.themallard.bitmmo.impl.plugin.chathook.IChatCallback;
import me.themallard.bitmmo.impl.plugin.chathook.IChatWindow;
import me.themallard.bitmmo.impl.plugin.gamecontext.GameContext;
import me.themallard.bitmmo.impl.plugin.inputactiontracker.IInputActionTracker;
import me.themallard.bitmmo.impl.plugin.playerhook.IPlayer;
import me.themallard.bitmmo.impl.plugin.position.IPosition;
import me.themallard.bitmmo.impl.plugin.tickhook.ITickCallback;
import me.themallard.bitmmo.impl.plugin.tickhook.TickHookManager;
import ojman101.help.HelpManager;

public class NoclipInject implements IChatCallback, ITickCallback {
	
	private boolean nc = false;
	private int ncSpeed = 4;

	public NoclipInject() {
		ChatHookManager.registerCallback(this);
		TickHookManager.registerCallback(this);
		HelpManager.addHelp("Noclip", "Noclip. /nc to enable and /ncspeed speed to change the speed");
	}

	@Override
	public boolean onChatMessage(String message) {
		if (message.equals("/nc")) {
			nc = !nc;
			StringBuilder sb = new StringBuilder();
			sb.append("[Bit+] ");
			sb.append(nc ? "Enabled" : "Disabled");
			sb.append(' ');
			sb.append("noclip.");
			GameContext.getChatWindow().addChatMessage(sb.toString());
			return true;
		}
		
		if(message.startsWith("/ncspeed")) {
			String substring = message.substring("/ncspeed ".length());
			ncSpeed = Integer.parseInt(substring);
			GameContext.getChatWindow().addChatMessage("Changed noclip speed to " + ncSpeed);
			return true;
		}

		return false;
	}

	@Override
	public void onReceiveMessage(String message) {
	}

	@Override
	public void preTick() {
		IInputActionTracker tracker = GameContext.getInputActionTracker();
		IPlayer player = GameContext.getPlayer();
		IChatWindow chatWindow = GameContext.getChatWindow();

		if (nc && tracker != null && player != null && !chatWindow.isVisible()) {
			IPosition orig = player.getPosition();

			if (tracker.isKeyDown(InputActionTracker$ActionType.MOVE_NORTH))
				player.getPosition().setY(orig.getY() - ncSpeed);

			if (tracker.isKeyDown(InputActionTracker$ActionType.MOVE_SOUTH))
				player.getPosition().setY(orig.getY() + ncSpeed);

			if (tracker.isKeyDown(InputActionTracker$ActionType.MOVE_EAST))
				player.getPosition().setX(orig.getX() + ncSpeed);

			if (tracker.isKeyDown(InputActionTracker$ActionType.MOVE_WEST))
				player.getPosition().setX(orig.getX() - ncSpeed);
		}
	}
}
