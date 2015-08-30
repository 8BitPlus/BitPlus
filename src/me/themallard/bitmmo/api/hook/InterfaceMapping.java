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

package me.themallard.bitmmo.api.hook;

public class InterfaceMapping {
	private ClassHook owner;
	private String canonicalName;

	public InterfaceMapping(ClassHook owner, String canonicalName) {
		this.owner = owner;
		this.canonicalName = canonicalName;
	}

	public ClassHook getOwner() {
		return owner;
	}

	public InterfaceMapping setOwner(ClassHook owner) {
		if (this.owner != null)
			this.owner.interfaces().remove(this);

		this.owner = owner;
		owner.interfaces().add(this);
		return this;
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	public InterfaceMapping setCanonicalName(String canonicalName) {
		this.canonicalName = canonicalName;
		return this;
	}
}
