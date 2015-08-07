package org.nullbool.pi.core.hook.api;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.Type;

import me.themallard.bitmmo.api.hook.ClassHook;

public class DynamicDesc implements Serializable {

	private static final long serialVersionUID = 5520906934192018290L;

	private final boolean method;
	private String obfuscated;

	public DynamicDesc(boolean method) {
		this.method = method;
	}

	public DynamicDesc(String obfuscated, boolean method) {
		this.obfuscated = obfuscated;
		this.method = method;
	}

	public boolean isMethod() {
		return method;
	}

	public String getObfuscated() {
		return obfuscated;
	}

	public void setObfuscated(String obfuscated) {
		this.obfuscated = obfuscated;
	}

	public String getRefactoredDesc(List<ClassHook> classes) {
		if (method) {
			return convertMultiJavaStyle(classes, obfuscated);
		} else {
			return convertSingleJavaStyle(classes, obfuscated);
		}
	}

	private static int array(String desc) {
		int c = 0;
		char[] chars = desc.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c1 = chars[i];
			if (c1 == '[') {
				c++;
			} else {
				break;
			}
		}
		return c;
	}

	private static String makeArray(int j, String desc) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < j; i++) {
			sb.append(desc);
		}
		return sb.toString();
	}

	public static String convertSingleJavaStyle(List<ClassHook> classes, String desc) {
		return convertSingleJavaStyle(classes, desc.replace("[", ""), array(desc));
	}

	public static String convertSingleJavaStyle(List<ClassHook> classes, String desc, int arr) {
		StringBuilder sb = new StringBuilder();
		if (isPrimitive(desc))
			sb.append(Type.getType(desc).getClassName());
		else if (desc.startsWith("L") && desc.endsWith(";"))
			sb.append(getNeatClassName(classes, Type.getType(desc).getClassName()));
		sb.append(makeArray(arr, "[]"));
		return sb.toString();
	}

	public static String convertMultiJavaStyle(List<ClassHook> classes, String desc) {
		if (desc == null)
			return null;
		List<String> args = parseArgs(desc);
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		Iterator<String> it = args.iterator();
		while (it.hasNext()) {
			String arg = it.next();
			sb.append(convertSingleJavaStyle(classes, arg));
			if (it.hasNext())
				sb.append(", ");
		}
		sb.append(")");
		Type ret = Type.getReturnType(desc);
		String retVal = "";
		if (isVoid(ret)) {
			retVal = "void";
		} else {
			retVal = convertSingleJavaStyle(classes, ret.getDescriptor());
		}
		return String.format("%s %s", retVal, sb.toString());
	}

	public static String convertSingleBytecodeStyle(List<ClassHook> classes, String desc) {
		return convertSingleBytecodeStyle(classes, desc.replace("[", ""), array(desc));
	}

	public static String convertSingleBytecodeStyle(List<ClassHook> classes, String desc, int arr) {
		StringBuilder sb = new StringBuilder();
		sb.append(makeArray(arr, "["));
		if (isPrimitive(desc))
			sb.append(desc);
		else if (desc.startsWith("L") && desc.endsWith(";"))
			sb.append(getBytecodeDesc(classes, desc));
		return sb.toString();
	}

	public static String convertMultiBytecodeStyle(List<ClassHook> classes, String desc) {
		if (desc == null)
			return null;

		Type ret = Type.getReturnType(desc);
		String retVal = "";
		if (isVoid(ret)) {
			retVal = "V";
		} else {
			retVal = convertSingleBytecodeStyle(classes, ret.getDescriptor());
		}

		List<String> args = parseArgs(desc);
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		Iterator<String> it = args.iterator();
		while (it.hasNext()) {
			String arg = it.next();
			sb.append(convertSingleBytecodeStyle(classes, arg));
		}
		sb.append(")");
		return String.format("%s%s", sb.toString(), retVal);
	}

	public static String getBytecodeDesc(List<ClassHook> classes, String name) {
		if (name == null)
			return null;
		name = standardise(name);

		String className = name.substring(1, name.length() - 1);

		for (ClassHook c : classes) {
			String obf = c.obfuscated();
			String ref = c.refactored();
			if (obf != null && ref != null) {
				if (standardise(obf).equals(className))
					return String.format("L%s;", ref);
			}
		}

		return name;
	}

	public static boolean isVoid(Type t) {
		return isVoid(t.getDescriptor());
	}

	public static boolean isVoid(String desc) {
		return desc.equals("V");
	}

	public static List<String> parseArgs(String desc) {
		if (desc == null)
			return null;
		Type[] args = Type.getArgumentTypes(desc);
		// needs to be ordered and retain duplicates, can't be a set
		List<String> list = new LinkedList<String>();
		for (Type arg : args) {
			list.add(arg.getDescriptor());
		}
		return list;
	}

	public static String getNeatClassName(List<ClassHook> classes, String name) {
		if (name == null)
			return null;
		name = standardise(name);

		for (ClassHook c : classes) {
			String obf = c.obfuscated();
			String ref = c.refactored();
			if (obf != null && ref != null) {
				if (standardise(obf).equals(name))
					return ref;
			}
		}

		int splitIndex = name.lastIndexOf('/');
		name = name.substring(splitIndex + 1);

		return name;
	}

	public static String standardise(String s) {
		return s.replace(".", "/");
	}

	public static boolean isPrimitive(String desc) {
		switch (desc) {
		case "I":
		case "D":
		case "F":
		case "B":
		case "S":
		case "J":
		case "Z":
			return true;
		default:
			return false;
		}
	}

	// public static void main(String[] args) {
	// List<ClassHook> classes = new ArrayList<ClassHook>();
	// ClassHook hook = new ClassHook("java/lang/String", "Strang");
	// classes.add(hook);
	// System.out.println(convertSingleJavaStyle(classes, "I"));
	// System.out.println(convertSingleJavaStyle(classes, "[I"));
	// System.out.println(convertSingleJavaStyle(classes, "[[I"));
	// System.out.println(convertSingleJavaStyle(classes, "[[[I"));
	// System.out.println(convertSingleJavaStyle(classes,
	// "Ljava/lang/String;"));
	// System.out.println(convertSingleJavaStyle(classes,
	// "[Ljava/lang/String;"));
	//
	// System.out.println(convertMultiJavaStyle(classes,
	// "(IBLjava/lang/String;)Ljava/lang/Throwable;"));
	// System.out.println(convertMultiJavaStyle(classes,
	// "([Ljava/lang/Throwable;Ljava/lang/String;I[IBS[Ljava/io/IOException;)V"));
	//
	// System.out.println("-------------------------------------------");
	//
	// hook.setRefactored("java/lang/Strang");
	//
	// System.out.println(convertSingleBytecodeStyle(classes, "I"));
	// System.out.println(convertSingleBytecodeStyle(classes, "[I"));
	// System.out.println(convertSingleBytecodeStyle(classes, "[[I"));
	// System.out.println(convertSingleBytecodeStyle(classes, "[[[I"));
	// System.out.println(convertSingleBytecodeStyle(classes,
	// "Ljava/lang/String;"));
	// System.out.println(convertSingleBytecodeStyle(classes,
	// "[Ljava/lang/String;"));
	//
	// System.out.println(convertMultiBytecodeStyle(classes,
	// "(IBLjava/lang/String;)Ljava/lang/Throwable;"));
	// System.out.println(convertMultiBytecodeStyle(classes,
	// "([Ljava/lang/Throwable;Ljava/lang/String;I[IBS[Ljava
	// /io/IOException;)V"));
	// }

	@Override
	public String toString() {
		return "DynamicDesc [method=" + method + ", obfuscated=" + obfuscated + "]";
	}
}