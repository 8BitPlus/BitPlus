package org.topdank.byteio.in;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.tree.ClassNode;
import org.topdank.byteengineer.commons.asm.ASMFactory;
import org.topdank.byteengineer.commons.data.JarInfo;
import org.topdank.byteengineer.commons.data.JarResource;
import org.topdank.byteengineer.commons.data.LocateableJarContents;
import org.topdank.byteio.util.IOUtil;

public class MultiJarDownloader<C extends ClassNode> extends AbstractJarDownloader<C> {

	protected final JarInfo[] jarInfos;

	public MultiJarDownloader(JarInfo... jarInfos) {
		super();
		this.jarInfos = jarInfos;
	}

	public MultiJarDownloader(ASMFactory<C> factory, JarInfo... jarInfos) {
		super(factory);
		this.jarInfos = jarInfos;
	}

	@Override
	public void download() throws IOException {
		URL[] urls = new URL[jarInfos.length];
		for (int i = 0; i < jarInfos.length; i++) {
			urls[i] = new URL(jarInfos[i].formattedURL());
		}
		contents = new LocateableJarContents<C>(urls);
		for (JarInfo jarinfo : jarInfos) {
			JarURLConnection connection = (JarURLConnection) new URL(jarinfo.formattedURL()).openConnection();
			JarFile jarFile = connection.getJarFile();
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				byte[] bytes = IOUtil.read(jarFile.getInputStream(entry));
				if (entry.getName().endsWith(".class")) {
					C cn = factory.create(bytes, entry.getName());
					contents.getClassContents().add(cn);
				} else {
					JarResource resource = new JarResource(entry.getName(), bytes);
					contents.getResourceContents().add(resource);
				}
			}
		}
	}
}