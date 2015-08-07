package me.themallard.bitmmo.api.obfuscation.refactor;

public abstract interface IRemapper {
	public abstract String resolveClassName(String oldName);

	public abstract String resolveFieldName(String owner, String name, String desc);

	public abstract String resolveMethodName(String owner, String name, String desc, boolean isStatic);
}