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
public class OptionsAnalyser extends ClassAnalyser {
	private String className;

	public OptionsAnalyser() {
		super("Options");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		if (className == null) {
			boolean closebutton = false;
			boolean menuoptions = false;

			for (MethodNode mn : cn.methods) {
				for (AbstractInsnNode ain : mn.instructions.toArray()) {
					if (ain instanceof LdcInsnNode) {
						if (((LdcInsnNode) ain).cst.toString().equals("closebutton.png")) {
							closebutton = true;
						}

						if (((LdcInsnNode) ain).cst.toString().equals("$(UI-MenuOptions)")) {
							menuoptions = true;
						}
					}
				}
			}

			if (closebutton && menuoptions) {
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
