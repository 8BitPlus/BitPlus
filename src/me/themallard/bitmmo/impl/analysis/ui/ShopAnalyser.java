package me.themallard.bitmmo.impl.analysis.ui;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;

@SupportedHooks(fields = {}, methods = {})
public class ShopAnalyser extends ClassAnalyser {
	private String className;

	public ShopAnalyser() {
		super("ui/Shop");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		if (className == null) {
			boolean timesten = false;
			boolean buy = false;

			for (MethodNode mn : cn.methods) {
				for (AbstractInsnNode ain : mn.instructions.toArray()) {
					if (ain instanceof LdcInsnNode) {
						if (((LdcInsnNode) ain).cst.toString().equals("x10")) {
							timesten = true;
						}

						if (((LdcInsnNode) ain).cst.toString().equals("buy")) {
							buy = true;
						}
					}
				}
			}

			if (timesten && buy) {
				className = cn.name;
				return true;
			}
		}

		return false;
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return null;
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return null;
	}
}
