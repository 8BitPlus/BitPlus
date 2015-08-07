package org.topdank.byteengineer.commons.data;

import java.net.URL;

import org.objectweb.asm.tree.ClassNode;

public class LocateableJarContents<C extends ClassNode> extends JarContents<C> {

	private final URL[] jarUrls;

	public LocateableJarContents(URL... jarUrls) {
		super();
		this.jarUrls = jarUrls;
	}

	public LocateableJarContents(DataContainer<C> classContents, DataContainer<JarResource> resourceContents, URL... jarUrls) {
		super(classContents, resourceContents);
		this.jarUrls = jarUrls;
	}

	public URL[] getJarUrls() {
		return jarUrls;
	}
}