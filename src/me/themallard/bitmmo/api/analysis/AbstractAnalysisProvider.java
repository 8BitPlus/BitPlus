/* Copyright (C) 2015 maaatts

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package me.themallard.bitmmo.api.analysis;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nullbool.pi.core.hook.api.Constants;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.topdank.byteengineer.commons.data.JarContents;
import org.topdank.byteengineer.commons.data.JarResource;
import org.topdank.byteengineer.commons.data.LocateableJarContents;
import org.topdank.byteio.out.CompleteJarDumper;

import me.themallard.bitmmo.api.Context;
import me.themallard.bitmmo.api.Revision;
import me.themallard.bitmmo.api.hook.ClassHook;
import me.themallard.bitmmo.api.hook.FieldHook;
import me.themallard.bitmmo.api.hook.HookMap;
import me.themallard.bitmmo.api.hook.MethodHook;
import me.themallard.bitmmo.api.obfuscation.refactor.BytecodeRefactorer;
import me.themallard.bitmmo.api.obfuscation.refactor.ClassTree;
import me.themallard.bitmmo.api.obfuscation.refactor.IRemapper;
import me.themallard.bitmmo.api.obfuscation.refactor.MethodCache;
import me.themallard.bitmmo.api.output.OutputLogger;
import me.themallard.bitmmo.api.output.resource.OutputResource;
import me.themallard.bitmmo.api.output.resource.OutputResourceList;
import me.themallard.bitmmo.api.util.NodedContainer;

@SuppressWarnings(value = { "all" })
public abstract class AbstractAnalysisProvider {
	private final Revision revision;
	private final LocateableJarContents<ClassNode> contents;
	private List<ClassAnalyser> analysers;
	private ClassTree classTree;
	private OutputResourceList resourceList;

	public AbstractAnalysisProvider(Revision revision) throws IOException {
		this.revision = revision;
		this.contents = new LocateableJarContents<ClassNode>(new NodedContainer<ClassNode>(revision.parse().values()),
				null, null);
		this.analysers = new ArrayList<ClassAnalyser>();
		this.resourceList = revision.parseResources();
	}

	public Map<String, ClassNode> run() {
		classTree = new ClassTree(contents.getClassContents());
		classTree.output();

		deobfuscate();

		analysers = registerAnalysers().asList();
		analyse();
		
		return contents.getClassContents().namedMap();
	}
	
	public void dump() {
		HookMap hm = OutputLogger.output();

		dumpJar(hm);

		Context.unbind();
	}

	private void analyse() {
		Map<String, ClassNode> classNodes = contents.getClassContents().namedMap();

		// run class analysers first, and then other analysers
		// because some of the field/method analysers depend on the results of
		// class analysers

		for (ClassAnalyser a : analysers)
			a.preRun(classNodes);

		for (ClassAnalyser a : analysers)
			a.runSubs();
	}

	private void deobfuscate() {
		return;
	}

	private void dumpJar(HookMap hookMap) {
		Map<String, ClassHook> classes = new HashMap<String, ClassHook>();
		Map<String, FieldHook> fields = new HashMap<String, FieldHook>();
		Map<String, MethodHook> methods = new HashMap<String, MethodHook>();

		for (ClassHook h : hookMap.classes()) {
			classes.put(h.obfuscated(), h);
			for (FieldHook f : h.fields()) {
				fields.put(f.val(Constants.REAL_OWNER) + "." + f.obfuscated() + " " + f.val(Constants.DESC), f);
			}

			for (MethodHook m : h.methods()) {
				methods.put(m.val(Constants.REAL_OWNER) + "." + m.obfuscated() + m.val(Constants.DESC), m);
			}
		}

		JarContents<ClassNode> contents = new JarContents<ClassNode>();
		contents.getClassContents().addAll(getClassNodes().values());
		Map<String, ClassNode> nodes = contents.getClassContents().namedMap();
		MethodCache cache = new MethodCache(contents.getClassContents());

		IRemapper remapper = new IRemapper() {
			@Override
			public String resolveMethodName(String owner, String name, String desc, boolean isStatic) {
				if (name.length() > 2)
					return name;

				if (owner.lastIndexOf('/') == -1 && isStatic) {
					ClassNode cn = nodes.get(owner);
					if (cn != null) {
						Set<MethodNode> matches = new HashSet<MethodNode>();

						MethodNode mn = null;
						for (MethodNode m : cn.methods) {
							if (m.name.equals(name) && paramsMatch(desc, m.desc)) {
								if (m.desc.equals(desc) && mn == null && Modifier.isStatic(m.access) == isStatic) {
									mn = m;
								} else {
									matches.add(m);
								}
							}
						}

						for (ClassNode _cn : classTree.getSupers(cn)) {
							for (MethodNode m : _cn.methods) {
								if (m.name.equals(name) && paramsMatch(desc, m.desc)) {
									if (m.desc.equals(desc) && mn == null && Modifier.isStatic(m.access) == isStatic) {
										mn = m;
									} else {
										matches.add(m);
									}
								}
							}
						}
						for (ClassNode _cn : classTree.getDelegates(cn)) {
							for (MethodNode m : _cn.methods) {
								if (m.name.equals(name) && paramsMatch(desc, m.desc)) {
									if (m.desc.equals(desc) && mn == null && Modifier.isStatic(m.access) == isStatic) {
										mn = m;
									} else {
										matches.add(m);
									}
								}
							}
						}

						if (mn == null)
							throw new RuntimeException(
									String.format("%s.%s %s : ", owner, name, desc) + matches.toString());

						Type ret = Type.getReturnType(mn.desc);
						for (MethodNode m : matches) {
							Type ret1 = Type.getReturnType(m.desc);
							if (!ret.getDescriptor().equals(ret1.getDescriptor())) {
								// System.out.println(String.format("Renaming
								// %s.%s %s (%b) : ", owner, name, desc,
								// Modifier.isStatic(mn.access)) +
								// matches.toString());
								return "m_" + cn.methods.indexOf(mn);
							}
						}
					}
				}

				if (name.equals("if") || name.equals("do")) {
					MethodNode m = cache.get(owner, name, desc);
					if (Modifier.isStatic(m.access)) {
						// System.out.println(m.key() + " is static.");
					} else {
						// System.out.println(m.key() + " isn't static.");
					}
					return "m1_" + name;
				}

				return name;
			}

			@Override
			public String resolveFieldName(String owner, String name, String desc) {
				String key = owner + "." + name + " " + desc;
				if (fields.containsKey(key)) {
					return fields.get(key).refactored();
				}

				if (name.equals("do") || name.equals("if")) {
					return "f_" + name;
				}
				// let the refactorer do it's own thang if we can't quick-find
				// it
				// ie. it will do a deep search.
				return null;
			}

			@Override
			public String resolveClassName(String owner) {
				ClassHook ref = classes.get(owner);
				if (ref != null)
					return ref.refactored();

				if (owner.indexOf('/') == -1) {
					if (owner.equals("do") || owner.equals("if")) {
						owner = "klass_" + owner;
					}

					return owner;
				} else {
					return owner;
				}
			}
		};

		BytecodeRefactorer refactorer = new BytecodeRefactorer((Collection<ClassNode>) contents.getClassContents(),
				remapper);
		refactorer.start();

		for (OutputResource or : resourceList) {
			contents.getResourceContents().add(new JarResource(or.getName(), or.getData()));
		}

		CompleteJarDumper dumper = new CompleteJarDumper(contents);
		String name = getRevision().getName();
		File file = new File("out/" + name + "/refactor_" + name + ".jar");
		if (file.exists())
			file.delete();
		file.mkdirs();
		try {
			dumper.dump(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean paramsMatch(String d1, String d2) {
		Type[] args1 = Type.getArgumentTypes(d1);
		Type[] args2 = Type.getArgumentTypes(d2);
		if (args1.length == args2.length) {
			for (int i = 0; i < args1.length; i++) {
				if (!args1[i].getDescriptor().equals(args2[i].getDescriptor()))
					return false;
			}
			return true;
		}
		return false;
	}

	protected abstract Builder<ClassAnalyser> registerAnalysers();

	public Map<String, ClassNode> getClassNodes() {
		return contents.getClassContents().namedMap();
	}

	public List<ClassAnalyser> getAnalysers() {
		return analysers;
	}

	public Revision getRevision() {
		return revision;
	}

	public void setOutputResourceList(OutputResourceList ol) {
		this.resourceList = ol;
	}
}
