package org.nullbool.pi.core.hook.api;

/**
 * @author Bibl (don't ban me pls)
 * @created 6 Jul 2015 17:23:37
 */
public abstract interface Constants {

	// Shared
	public static final String DESC = "desc.obfuscated";
	public static final String STATIC = "attr.static";
	
	// Field
	public static final String REAL_OWNER = "owner.real";
	public static final String MUTLI = "attr.multi";
	public static final String ENCODER = "attr.multi.encoder";

	// Method
	public static final String METHOD_TYPE = "attr.type";
	public static final String MAX_STACK = "attr.maxs";
	public static final String MAX_LOCALS = "attr.maxl";
	public static final String CALLBACK = "callback";
	public static final String PATCH = "patch";
	public static final String PATCH_POSITION = "attr.type.patch.pos";
	public static final String START = "attr.type.patch.pos.start";
	public static final String END = "attr.type.patch.pos.end";
	public static final String SAFE_OPAQUE = "attr.safeopaque";
}