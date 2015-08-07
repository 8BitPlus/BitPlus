package me.themallard.bitmmo.api;

public abstract class ServerType {
	private final String name;
	private final String host;
	private final int port;
	private final String resource;

	public ServerType(String name, String host, int port, String resource) {
		this.name = name;
		this.host = host;
		this.port = port;
		this.resource = resource;
	}

	public String getName() {
		return this.name;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getResource() {
		return resource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + port;
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerType other = (ServerType) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (port != other.port)
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}
}
