package me.themallard.bitmmo.api.obfuscation.refactor;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class BytecodeRefactorer implements Opcodes {

	private final Collection<ClassNode> classes;
	private final IRemapper remapper;
	private final ClassTree classTree;
	private final DataCache<MethodNode> methodCache;
	private final InheritedMethodMap methodChain;
	private final Map<String, String> classMappings;
	private final Map<String, String> fieldMappings;
	private final Map<String, String> descMappings;
	private final Map<String, String> methodMappings;

	public BytecodeRefactorer(Collection<ClassNode> classes, IRemapper remapper) {
		this.classes = classes;
		this.remapper = remapper;
		classTree = new ClassTree(classes);
		methodCache = new MethodCache(classes);
		methodChain = new InheritedMethodMap(classTree);
		classMappings = new HashMap<String, String>();
		fieldMappings = new HashMap<String, String>();
		descMappings = new HashMap<String, String>();
		methodMappings = new HashMap<String, String>();
	}

	public void start() {
		startImpl();
	}

	private void startImpl() {

		// int fieldNodes = 0;
		// int methodNodes = 0;

		// int fieldCalls = 0;
		// int methodCalls = 0;

		// TypeInsnNode
		// int newCalls = 0;
		// int newArray = 0;
		// int checkcasts = 0;
		// int instances = 0;
		// int mArrray = 0;

		// int classc = 0;
		// int iface = 0;

		Map<ClassNode, String> classMappings = new HashMap<ClassNode, String>();
		Map<String, String> superMappings = new HashMap<String, String>();
		Map<String, String> interfaceMappings = new HashMap<String, String>();
		Map<FieldNode, Tuple3> fieldMappings = new HashMap<FieldNode, Tuple3>();
		Map<MethodNode, Tuple3> methodMappings = new HashMap<MethodNode, Tuple3>();

		Map<FieldInsnNode, Tuple3> finMappings = new HashMap<FieldInsnNode, Tuple3>();
		Map<MethodInsnNode, Tuple3> minMappings = new HashMap<MethodInsnNode, Tuple3>();
		Map<AbstractInsnNode, String> singAinMappings = new HashMap<AbstractInsnNode, String>();

		for (ClassNode cn : classes) {
			for (FieldNode fn : cn.fields) {
				Tuple3 t = new Tuple3(fn.owner.name, getMappedFieldName(fn), transformFieldDesc(fn.desc));
				fieldMappings.put(fn, t);
			}

			for (MethodNode mn : cn.methods) {
				Tuple3 t = new Tuple3(mn.owner.name, getMappedMethodName(mn), transformMethodDesc(mn.desc));
				methodMappings.put(mn, t);

				for (AbstractInsnNode ain : mn.instructions.toArray()) {
					if (ain instanceof FieldInsnNode) {
						FieldInsnNode fin = (FieldInsnNode) ain;
						String newOwner = getMappedClassName(fin.owner);
						String newName = getMappedFieldName(fin.owner, fin.name, fin.desc);
						String newDesc = transformFieldDesc(fin.desc);
						Tuple3 t2 = new Tuple3(newOwner, newName, newDesc);
						finMappings.put(fin, t2);
					} else if (ain instanceof MethodInsnNode) {
						MethodInsnNode min = (MethodInsnNode) ain;
						try {
							String newOwner = getMappedClassName(min.owner);
							String newName = getMappedMethodName(min.owner, min.name, min.desc,
									min.opcode() == INVOKESTATIC);
							String newDesc = transformMethodDesc(min.desc);
							Tuple3 t2 = new Tuple3(newOwner, newName, newDesc);
							minMappings.put(min, t2);
						} catch (RuntimeException e) {
							System.out.printf("Error in %s.%n", mn.key());
							System.out.printf("Looking for %s.%n", ain);
							ClassNode c = classTree.getClass(min.owner);
							System.out.println("sups: " + classTree.getSupers(c));
							throw e;
						}
					} else if (ain instanceof TypeInsnNode) {
						TypeInsnNode tin = (TypeInsnNode) ain;

						if (tin.opcode() == NEW || tin.opcode() == ANEWARRAY) {
							String desc = tin.desc;
							if (desc.startsWith("[") || desc.endsWith(";")) {
								singAinMappings.put(tin, transformFieldDesc(desc));
							} else {
								singAinMappings.put(tin, getMappedClassName(desc));
							}
						} else if (tin.opcode() == CHECKCAST || tin.opcode() == INSTANCEOF) {
							// ALOAD 1
							// CHECKCAST java/lang/Character
							// INVOKEVIRTUAL java/lang/Character.charValue ()C
							// Checkcasts are always object casts
							String desc = tin.desc;
							if (desc.startsWith("[") || desc.endsWith(";")) {
								singAinMappings.put(tin, transformFieldDesc(desc));
							} else {
								singAinMappings.put(tin, getMappedClassName(desc));
							}
						}
					} else if (ain instanceof MultiANewArrayInsnNode) {
						MultiANewArrayInsnNode main = (MultiANewArrayInsnNode) ain;
						singAinMappings.put(main, transformFieldDesc(main.desc));
					} else if (ain instanceof LdcInsnNode) {
						LdcInsnNode lin = (LdcInsnNode) ain;

						if (lin.cst instanceof Type) {
							singAinMappings.put(lin, transformFieldDesc(((Type) lin.cst).getDescriptor()));
						}
					}
				}
			}

			superMappings.put(cn.superName, getMappedClassName(cn.superName));
			classMappings.put(cn, getMappedClassName(cn.name));

			for (String oldIface : cn.interfaces) {
				interfaceMappings.put(oldIface, getMappedClassName(oldIface));
			}
		}

		// ======================= FIX ==================================

		for (ClassNode cn : classes) {
			cn.name = classMappings.get(cn);
			cn.superName = superMappings.get(cn.superName);

			List<String> newInterfaces = new ArrayList<String>();
			for (String oldIface : cn.interfaces) {
				newInterfaces.add(interfaceMappings.get(oldIface));
			}
			cn.interfaces = newInterfaces;

			for (FieldNode fn : cn.fields) {
				Tuple3 t = fieldMappings.get(fn);
				fn.name = t.name;
				fn.desc = t.desc;
			}

			for (MethodNode mn : cn.methods) {
				Tuple3 t = methodMappings.get(mn);
				mn.name = t.name;
				mn.desc = t.desc;
			}
		}

		for (Entry<FieldInsnNode, Tuple3> e : finMappings.entrySet()) {
			FieldInsnNode fin = e.getKey();
			Tuple3 t = e.getValue();
			fin.owner = t.owner;
			fin.name = t.name;
			fin.desc = t.desc;
		}

		for (Entry<MethodInsnNode, Tuple3> e : minMappings.entrySet()) {
			MethodInsnNode fin = e.getKey();
			Tuple3 t = e.getValue();
			fin.owner = t.owner;
			fin.name = t.name;
			fin.desc = t.desc;
		}

		for (Entry<AbstractInsnNode, String> e : singAinMappings.entrySet()) {
			AbstractInsnNode ain = e.getKey();
			String desc = e.getValue();
			if (ain instanceof TypeInsnNode) {
				((TypeInsnNode) ain).desc = desc;
			} else if (ain instanceof MultiANewArrayInsnNode) {
				((MultiANewArrayInsnNode) ain).desc = desc;
			} else if (ain instanceof LdcInsnNode) {
				((LdcInsnNode) ain).cst = Type.getType(desc);
			} else {
				throw new RuntimeException();
			}
		}

		// TODO: Uncomment
		// if(Context.current().getFlags().getOrDefault("basicout", true)) {
		// System.out.printf("Changed: %n");
		// System.out.printf(" %d classes and %d interfaces. %n", classc,
		// iface);
		// System.out.printf(" %d fields and %d field calls. %n", fieldNodes,
		// fieldCalls);
		// System.out.printf(" %d methods and %d method calls. %n", methodNodes,
		// methodCalls);
		// System.out.printf(" %d news, %d anewarrays, %d checkcasts, %d
		// instancofs, %d mnewarrays. %n", newCalls, newArray, checkcasts,
		// instances, mArrray);
		// }
	}

	public String transformMethodDesc(String desc) {
		if (descMappings.containsKey(desc))
			return descMappings.get(desc);

		Type[] args = Type.getArgumentTypes(desc);
		Type ret = Type.getReturnType(desc);

		StringBuilder sb = new StringBuilder("(");
		for (Type arg : args) {
			sb.append(transformFieldDesc(arg.getDescriptor()));
		}
		sb.append(")");

		String retD = ret.getDescriptor();
		if (retD.equals("V"))
			sb.append("V");
		else
			sb.append(transformFieldDesc(ret.getDescriptor()));

		String newDesc = sb.toString();
		descMappings.put(desc, newDesc);

		return newDesc;
	}

	public String transformFieldDesc(String desc) {
		String nonArrayDesc = desc.replace("[", "");

		if (isPrimitive(nonArrayDesc))
			return desc;

		if (descMappings.containsKey(desc))
			return descMappings.get(desc);

		// Type type = Type.getType(desc);
		// String oldClassName = type.getInternalName();
		// Remove the L and ; on the front and back of the desc

		int arraySize = desc.length() - nonArrayDesc.length();
		nonArrayDesc = nonArrayDesc.substring(1, nonArrayDesc.length() - 1);

		String newBaseDesc = String.format("L%s;", getMappedClassName(nonArrayDesc));

		String newDesc = createArrayDescriptor(arraySize) + newBaseDesc;
		descMappings.put(desc, newDesc);

		return newDesc;
	}

	public static boolean isPrimitive(String desc) {
		switch (desc) {
		case "I":
		case "B":
		case "S":
		case "J":
		case "D":
		case "F":
		case "Z":
		case "C":
			return true;
		default:
			return false;
		}
	}

	public static String createArrayDescriptor(int size) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append("[");
		}
		return sb.toString();
	}

	public String getMappedClassName(String oldName) {
		if (classMappings.containsKey(oldName))
			return classMappings.get(oldName);

		String newName = remapper.resolveClassName(oldName);

		if (newName == null)
			newName = oldName;
		else
			newName = newName.replace(".", "/");

		classMappings.put(oldName, newName);
		return newName;
	}

	public String getMappedFieldName(FieldNode f) {
		return getMappedFieldName(f.owner.name, f.name, f.desc);
	}

	public String getMappedFieldName(String owner, String name, String desc) {
		String fullKey = String.format("%s.%s %s", owner, name, desc);

		if (fieldMappings.containsKey(fullKey)) {
			// if(fullKey.equals("gm.ec J")){
			// System.out.println("BytecodeRefactorer.getMappedFieldName()");
			// }
			return fieldMappings.get(fullKey);
		}

		String newName = remapper.resolveFieldName(owner, name, desc);

		/*
		 * If the newName is null, it means that the remapper may not be doing
		 * deep mapping, ie. if we have a class gm which has a field ec and
		 * another class fe which extends gm, then accessing fe.ec may not be
		 * mapped, even though it is referring (albeit indirectly) to gm.ec. If
		 * this is the case, we should work downwards through the class
		 * hierarchy tree and poll the remapper for a new name. eg. if we have
		 * the class gm with a field ec and we have another class fe which
		 * extends gm and another fu which extends fe, we look at fu.ec then
		 * fe.ec and then gm.ec for a new name.
		 */
		if (newName == null) {
			ClassNode topKlass = classTree.getClass(owner);

			if (topKlass != null) {
				Set<ClassNode> tree = classTree.getSupers(topKlass);
				if (tree != null && tree.size() > 0) {
					Iterator<ClassNode> it = tree.iterator();
					while (it.hasNext()) {
						ClassNode next = it.next();
						if (next == null)
							break;

						newName = remapper.resolveFieldName(next.name, name, desc);
						if (newName != null)
							break;
					}
				}
			}
		}

		// If everything fails, we don't change the name.
		if (newName == null)
			newName = name;

		fieldMappings.put(fullKey, newName);
		return newName;
	}

	public String getMappedMethodName(String owner, String name, String desc, boolean isStatic) {
		MethodNode m = findMethod(owner, name, desc);
		if (m != null && Modifier.isStatic(m.access) == isStatic)
			return getMappedMethodName(m);

		if (isStatic && owner.lastIndexOf('/') == -1) {
			m = searchTree(owner, name, desc, isStatic);
			if (m == null)
				// return name;
				throw new RuntimeException(String.format("Can't find %s.%s %s %b.%n", owner, name, desc, isStatic));
			// System.out.printf("Can't find %s.%s %s %b.%n", owner, name, desc,
			// isStatic);
			else
				return getMappedMethodName(m);
		}

		return name;
	}

	/* Added 02/07/15, 20:16. */
	public MethodNode searchTree(String owner, String name, String desc, boolean isStatic) {
		ClassNode cn = classTree.getClasses().get(owner);
		if (cn == null)
			return null;

		MethodNode m = methodCache.get(owner, name, desc);
		if (m != null && Modifier.isStatic(m.access) == isStatic) {
			return m;
		}

		for (ClassNode _super : classTree.getSupers(cn)) {
			MethodNode _m = methodCache.get(_super.name, name, desc);
			if (_m != null && Modifier.isStatic(_m.access) == isStatic) {
				return _m;
			}
		}

		return null;
	}

	public MethodNode findMethod(String owner, String name, String desc) {
		ClassNode cn = classTree.getClasses().get(owner);
		if (cn == null)
			return null;
		// throw new IllegalStateException(String.format("Class %s is not
		// present in the cache. (%s.%s %s)", owner, owner, name, desc));
		// String halfKey = name + "." + desc;
		// for(MethodNode m : cn.methods){
		// if(m.halfKey().equals(halfKey))
		// return m;
		// }
		// return null;

		return methodCache.get(owner, name, desc);

		/*
		 * Data is cached and recalled when needed because of a runtime issue.
		 * Take class K with a method M. When the method K.M is renamed, if we
		 * attempt to do a deep search to find the MethodNode it will fail,
		 * since the key K.M will actually be be looking for Kn.M where Kn is
		 * the new name of class K.
		 */
		/*
		 * return methodCache.get(owner, name, desc);
		 */
	}

	public String getMappedMethodName(MethodNode m) {
		/* step 1. check already mapped ones */
		String fullKey = m.cachedKey();
		if (methodMappings.containsKey(fullKey))
			return methodMappings.get(fullKey);

		String newName = null;

		/*
		 * step 2. check the tree to see if any of the ones in the chain have
		 * already been changed (essentially looking it up
		 */

		/*
		 * If the method is static it won't be part of a chain since static
		 * methods can't be overridden.
		 */
		if (!Modifier.isStatic(m.access)) {
			ChainData cd = methodChain.getData(m);
			if (cd == null) {
				System.err.println(m.key() + " is null " + Modifier.isStatic(m.access) + " " + methodChain.getData(m));
				System.exit(1);
			}
			for (MethodNode mn : cd.getAggregates()) {
				if (!mn.name.equals(m.name)) {
					newName = mn.name;
					break;
					// methodMappings.put(fullKey, newName);
					// return newName;
				}
			}
		}

		/* step 3. ask the remapper */
		newName = remapper.resolveMethodName(m.owner.name, m.name, m.desc, Modifier.isStatic(m.access));
		methodMappings.put(fullKey, newName);

		return newName;
	}

	public Collection<ClassNode> getClasses() {
		return classes;
	}

	public IRemapper getRemapper() {
		return remapper;
	}

	public ClassTree getClassTree() {
		return classTree;
	}

	public DataCache<MethodNode> getMethodCache() {
		return methodCache;
	}

	public InheritedMethodMap getMethodChain() {
		return methodChain;
	}

	public Map<String, String> getClassMappings() {
		return classMappings;
	}

	public Map<String, String> getFieldMappings() {
		return fieldMappings;
	}

	public Map<String, String> getDescMappings() {
		return descMappings;
	}

	public Map<String, String> getMethodMappings() {
		return methodMappings;
	}

	public static class Tuple3 {
		public final String owner;
		public final String name;
		public final String desc;

		public Tuple3(String owner, String name, String desc) {
			this.owner = owner;
			this.name = name;
			this.desc = desc;
		}
	}
}