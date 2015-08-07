package me.themallard.bitmmo.api.hook;

public class FieldHook extends ObfuscatedData {
	private ClassHook owner;

	public FieldHook() {
	}

	public FieldHook(ClassHook owner) {
		this.owner = owner;
	}

	@Override
	public FieldHook var(String name, String value) {
		super.var(name, value);
		return this;
	}

	@Override
	public FieldHook obfuscated(String obfuscated) {
		super.obfuscated(obfuscated);
		return this;
	}

	@Override
	public FieldHook refactored(String refactored) {
		super.refactored(refactored);
		return this;
	}

	public ClassHook owner() {
		return owner;
	}

	public FieldHook owner(ClassHook owner) {
		if (this.owner != null)
			this.owner.fields().remove(this);

		this.owner = owner;
		owner.fields().add(this);
		return this;
	}

	public String baseToString() {
		StringBuilder sb = new StringBuilder("FieldHook");
		sb.append("[");
		sb.append("variables=").append(variables());
		sb.append("]");
		return sb.toString();
	}
}