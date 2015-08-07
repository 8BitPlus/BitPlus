package me.themallard.bitmmo.impl.server;

import me.themallard.bitmmo.api.ServerType;

public class TestingServer extends ServerType {
	public TestingServer() {
		super("Testing", "core.8bitmmo.net", 1338, "https://s3.amazonaws.com/8BitMMO/HTMudWeb.zip");
	}
}
