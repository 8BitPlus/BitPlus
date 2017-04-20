/* Copyright (C) 2017 maaatts

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

package me.themallard.bitmmo.impl;

import me.themallard.bitmmo.api.util.IRevisionHelper;

public class LatestRevisionHelper implements IRevisionHelper {

	@Override
	public String getLatestRevision() {
		// we lose some of the "migrations" of analysis stuff by doing this
		// oh well
		return "9999";
	}

	@Override
	public String getOnlineFilePath() {
		return "https://s3.amazonaws.com/8BitMMO/HTMudWeb.jar";
	}

}
