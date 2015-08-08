package me.themallard.bitmmo.impl.analysis;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.hook.MethodHook;

@SupportedHooks(fields = {}, methods = { "init&()V" })
public class MainMenuAnalyser extends ClassAnalyser {
	private String className;

	public MainMenuAnalyser() {
		// the main menu must go in the HTMud package because this is where
		// Pulpcore looks for it
		super("HTMud/MainMenu");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		if (className == null) {
			for (MethodNode mn : cn.methods) {
				for (AbstractInsnNode ain : mn.instructions.toArray()) {
					if (ain instanceof LdcInsnNode) {
						if (((LdcInsnNode) ain).cst.toString().equals("   ___  _ ___ _  _ _  _ ____")) {
							className = cn.name;

							return true;
						}
					}
				}
			}
		}

		return false;
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return null;
	}

	public class InitMethodAnalyser implements IMethodAnalyser {
		@Override
		public List<MethodHook> find(ClassNode cn) {
			List<MethodHook> list = new ArrayList<MethodHook>();

			for (MethodNode mn : cn.methods) {
				for (AbstractInsnNode ain : mn.instructions.toArray()) {
					if (ain instanceof LdcInsnNode) {
						if (((LdcInsnNode) ain).cst.toString().contains("   ___  _ ___ _  _ _  _ ____")) {
							list.add(asMethodHook(mn, "init"));
						}
					}
				}
			}

			return list;
		}
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return new Builder<IMethodAnalyser>().add(new InitMethodAnalyser());
	}
}
