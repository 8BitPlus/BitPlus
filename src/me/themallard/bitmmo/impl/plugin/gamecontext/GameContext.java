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

package me.themallard.bitmmo.impl.plugin.gamecontext;

import me.themallard.bitmmo.impl.plugin.chathook.IChatWindow;
import me.themallard.bitmmo.impl.plugin.inputactiontracker.IInputActionTracker;
import me.themallard.bitmmo.impl.plugin.playerhook.IPlayer;

//TODO: Deprecate this in favor of the game's GodObject.
public class GameContext {
	private static IChatWindow ChatWindow;
	private static IPlayer Player;
	private static IInputActionTracker InputActionTracker;

	public static IChatWindow getChatWindow() {
		return ChatWindow;
	}

	public static void setChatWindow(IChatWindow chatWindow) {
		ChatWindow = chatWindow;
	}

	public static IPlayer getPlayer() {
		return Player;
	}

	public static void setPlayer(IPlayer player) {
		Player = player;
	}

	public static IInputActionTracker getInputActionTracker() {
		return InputActionTracker;
	}

	public static void setInputActionTracker(IInputActionTracker inputActionTracker) {
		InputActionTracker = inputActionTracker;
	}
}
