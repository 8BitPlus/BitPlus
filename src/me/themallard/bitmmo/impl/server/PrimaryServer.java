package me.themallard.bitmmo.impl.server;

import me.themallard.bitmmo.api.ServerType;

public class PrimaryServer extends ServerType {
	public PrimaryServer() {
		super("Primary", "core.8bitmmo.net", 1337, "https://s3.amazonaws.com/8BitMMO/HTMudWeb.zip");
	}
}
