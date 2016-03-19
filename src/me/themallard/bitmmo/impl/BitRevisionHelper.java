/* Copyright (C) 2016 maaatts

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

import java.net.URL;
import java.util.Scanner;

import me.themallard.bitmmo.api.util.IRevisionHelper;

public class BitRevisionHelper implements IRevisionHelper {
	@Override
	public String getLatestRevision() {
		// TODO: Grab this from the website
		try {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(new URL(String.format("%s/latest.txt", getUploadSite())).openStream(), "UTF-8")
					.useDelimiter("\\A");
			String s = scanner.nextLine();
			scanner.close();
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		return null;
	}

	@Override
	public String getUploadSite() {
		return "http://mattysmith.co.uk/HTMud";
	}
}
