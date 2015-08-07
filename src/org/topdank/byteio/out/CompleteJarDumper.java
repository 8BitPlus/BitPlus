package org.topdank.byteio.out;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.nullbool.api.util.ClassStructure;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.topdank.byteengineer.commons.data.JarContents;
import org.topdank.byteengineer.commons.data.JarResource;
import org.topdank.byteio.util.Debug;

import me.themallard.bitmmo.api.obfuscation.refactor.ClassTree;

/**
 * Dumps ClassNodes and JarResources back into a file on the local system.
 *
 * @author Bibl
 */
public class CompleteJarDumper implements JarDumper {

	private final JarContents<?> contents;
	private final ClassTree classTree;

	/**
	 * Creates a new JarDumper.
	 *
	 * @param contents
	 *            Contents of jar.
	 */
	public CompleteJarDumper(JarContents<ClassNode> contents) {
		this.contents = contents;
		classTree = new ClassTree(contents.getClassContents());
	}

	public JarOutputStream getOut(File file) throws IOException {
		return new JarOutputStream(new FileOutputStream(file));
	}

	/**
	 * Dumps the jars contents.
	 *
	 * @param file
	 *            File to dump it to.
	 */
	@Override
	public void dump(File file) throws IOException {
		if (file.exists())
			file.delete();
		file.createNewFile();
		JarOutputStream jos = getOut(file);
		int classesDumped = 0;
		int resourcesDumped = 0;
		for (ClassNode cn : contents.getClassContents()) {
			classesDumped += dumpClass(jos, cn.name, cn);
		}
		for (JarResource res : contents.getResourceContents()) {
			resourcesDumped += dumpResource(jos, res.getName(), res.getData());
		}
		if (!Debug.debugging)
			System.out.println("Dumped " + classesDumped + " classes and " + resourcesDumped + " resources to "
					+ file.getAbsolutePath());

		jos.close();
	}

	/**
	 * Writes the {@link ClassNode} to the Jar.
	 *
	 * @param out
	 *            The {@link JarOutputStream}.
	 * @param cn
	 *            The ClassNode.
	 * @param name
	 *            The entry name.
	 * @throws IOException
	 *             If there is a write error.
	 * @return The amount of things dumped, 1 or if you're not dumping it 0.
	 */
	@Override
	public int dumpClass(JarOutputStream out, String name, ClassNode cn) throws IOException {
		JarEntry entry = new JarEntry(cn.name + ".class");
		out.putNextEntry(entry);
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES) {
			@Override
			protected String getCommonSuperClass(final String type1, final String type2) {
				ClassNode ccn = classTree.getClass(type1);
				ClassNode dcn = classTree.getClass(type2);

				// System.out.println(type1 + " " + type2);
				if (ccn == null) {
					classTree.build(ClassStructure.create(type1));
					return getCommonSuperClass(type1, type2);
				}

				if (dcn == null) {
					classTree.build(ClassStructure.create(type2));
					return getCommonSuperClass(type1, type2);
				}

				Set<ClassNode> c = classTree.getSupers(ccn);
				Set<ClassNode> d = classTree.getSupers(dcn);

				if (c.contains(dcn))
					return type1;

				if (d.contains(ccn))
					return type2;

				if (Modifier.isInterface(ccn.access) || Modifier.isInterface(dcn.access)) {
					return "java/lang/Object";
				} else {
					do {
						ClassNode nccn = classTree.getClass(ccn.superName);
						if (nccn == null)
							break;
						ccn = nccn;
						c = classTree.getSupers(ccn);
					} while (!c.contains(dcn));
					return ccn.name;
				}
			}
		};
		cn.accept(writer);
		out.write(writer.toByteArray());
		return 1;
	}

	/**
	 * Writes a resource to the Jar.
	 *
	 * @param out
	 *            The {@link JarOutputStream}.
	 * @param name
	 *            The name of the file.
	 * @param file
	 *            File as a byte[].
	 * @throws IOException
	 *             If there is a write error.
	 * @return The amount of things dumped, 1 or if you're not dumping it 0.
	 */
	@Override
	public int dumpResource(JarOutputStream out, String name, byte[] file) throws IOException {
		JarEntry entry = new JarEntry(name);
		out.putNextEntry(entry);
		out.write(file);
		return 1;
	}
}