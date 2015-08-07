package org.nullbool.api.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.tree.ClassNode;
import org.topdank.byteio.util.IOUtil;

import me.themallard.bitmmo.api.output.resource.OutputResource;
import me.themallard.bitmmo.api.output.resource.OutputResourceList;

public class JarParser {

	private final Map<String, ClassNode> classes;
	private final OutputResourceList resources;

	public JarParser(JarFile j) throws IOException {
		classes = new NodeTable<ClassNode>();
		resources = new OutputResourceList();
		try {
			Enumeration<JarEntry> entries = j.entries();
			while (entries.hasMoreElements()) {
				JarEntry en = entries.nextElement();
				if (en.getName().endsWith(".class")) {
					// final ClassNode cn = new ClassNode();
					// final int skip = ClassReader.SKIP_FRAMES;
					// new ClassReader(j.getInputStream(en)).accept(cn, skip);
					ClassNode cn = ClassStructure.create(j.getInputStream(en));
					classes.put(cn.name, cn);
				} else if (!en.getName().contains("META-INF")) {
					
					byte[] bytes = IOUtil.read(j.getInputStream(en));
					resources.add(new OutputResource(en.getName(), bytes));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return a table of parsed classes
	 */
	public Map<String, ClassNode> getParsedClasses() {
		return classes;
	}
	
	public OutputResourceList getParsedResources() {
		return resources;
	}
}