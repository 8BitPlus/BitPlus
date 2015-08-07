package me.themallard.bitmmo.api;

import me.themallard.bitmmo.api.hook.ClassHook;

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
