package org.nullbool.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassStructure extends ClassNode {

	public final List<ClassStructure> supers = new ArrayList<>();
	public final List<ClassStructure> delegates = new ArrayList<>();

	private final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
	private byte[] bytes;

	public ClassStructure() {
		super(Opcodes.ASM5);
	}

	public static ClassStructure create(final byte[] bytes) {
		final ClassReader reader = new ClassReader(bytes);
		final ClassStructure node = new ClassStructure();
		// TODO: edited to SKIP_FRAMES
		reader.accept(node, ClassReader.SKIP_FRAMES);
		return node;
	}

	public static ClassStructure create(final InputStream in) {
		try {
			final ClassReader cr = new ClassReader(in);
			final ClassStructure cs = new ClassStructure();
			// TODO: edited to SKIP_FRAMES
			cr.accept(cs, ClassReader.SKIP_FRAMES);
			return cs;
		} catch (IOException e) {
			return null;
		}
	}

	public static ClassStructure create(String name) {
		try {
			final ClassReader cr = new ClassReader(name.replace(".", "/"));
			final ClassStructure cs = new ClassStructure();
			// TODO: edited to SKIP_FRAMES
			cr.accept(cs, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
			return cs;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] getBytes(final boolean cached) {
		if (cached && bytes != null)
			return bytes;
		accept(writer);
		return (bytes = writer.toByteArray());
	}

	public byte[] getBytes() {
		return getBytes(false);
	}

	@Override
	public String toString() {
		return name;
		/*
		 * final StringBuilder sb = new StringBuilder(name).append(" extends "
		 * ).append(superName); if (interfaces.size() == 0) return
		 * sb.toString(); sb.append(" implements ").append(interfaces.get(0));
		 * for (final String iface : interfaces.subList(1, interfaces.size())) {
		 * sb.append(", ").append(iface); } return sb.toString();
		 */
	}

	public MethodNode getMethodFromSuper(final String name, final String desc) {
		for (final ClassStructure super_ : supers) {
			for (final MethodNode mn : super_.methods) {
				if (mn.name.equals(name) && mn.desc.equals(desc)) {
					return mn;
				}
			}
		}
		return null;
	}

	public boolean isInherited(final String name, final String desc) {
		return getMethodFromSuper(name, desc) != null;
	}

	public boolean isInherited(final MethodNode mn) {
		return mn.owner.name.equals(name) && isInherited(mn.name, mn.desc);
	}

	@Override
	public MethodNode getMethod(final String name, final String desc) {
		for (final MethodNode mn : methods) {
			if (mn.key().equals(this.name + "." + name + desc)) {
				return mn;
			}
		}
		return null;
	}

	public List<FieldNode> getInstanceFields() {
		return super.fields.stream().filter(f -> !Modifier.isStatic(f.access)).collect(Collectors.toList());
	}

	public List<MethodNode> getInstanceMethods() {
		return super.methods.stream().filter(f -> !Modifier.isStatic(f.access)).collect(Collectors.toList());
	}

	public int getLevel() {
		return supers.size();
	}

	public ClassStructure getSuperType() {
		return supers.size() > 0 ? supers.get(0) : null; // direct superclass is
															// always added
															// first
	}
}