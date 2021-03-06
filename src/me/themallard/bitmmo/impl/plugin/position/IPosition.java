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

package me.themallard.bitmmo.impl.plugin.position;

public interface IPosition {
	public double getX();

	public double getY();

	public double getZ();

	public int getRX();

	public int getRY();

	public void setX(double n);

	public void setY(double n);

	public void setZ(double n);

	public void setRX(int n);

	public void setRY(int n);
	
	public default void set(IPosition pos) {
		setX(pos.getX());
		setY(pos.getY());
		setZ(pos.getZ());
		setRX(pos.getRX());
		setRY(pos.getRY());
	}

	public String toString();
}
