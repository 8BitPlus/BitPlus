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

public class SingleJarDownloader<C extends ClassNode> extends AbstractJarDownloader<C> {

	protected final JarInfo jarInfo;

	public SingleJarDownloader(JarInfo jarInfo) {
		super();
		this.jarInfo = jarInfo;
	}

	public SingleJarDownloader(ASMFactory<C> factory, JarInfo jarInfo) {
		super(factory);
		this.jarInfo = jarInfo;
	}

	@Override
	public void download() throws IOException {
		URL url = null;
		JarURLConnection connection = (JarURLConnection) (url = new URL(jarInfo.formattedURL())).openConnection();
		JarFile jarFile = connection.getJarFile();
		Enumeration<JarEntry> entries = jarFile.entries();
		contents = new LocateableJarContents<C>(url);
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			byte[] bytes = IOUtil.read(jarFile.getInputStream(entry));
			if (entry.getName().endsWith(".class")) {
				C cn = factory.create(bytes, entry.getName());
				if(!contents.getClassContents().namedMap().containsKey(cn.name))
					contents.getClassContents().add(cn);
				
				//if(cn.name.equals("org/xmlpull/v1/XmlPullParser")) {
				//	System.out.println("SingleJarDownloader.download() " +entry.getName() + " " + bytes.length);
				//}
			} else {
				JarResource resource = new JarResource(entry.getName(), bytes);
				contents.getResourceContents().add(resource);
			}
		}
	}
}