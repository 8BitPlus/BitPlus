package me.themallard.bitmmo.impl.analysis;

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
public class AssetParserAnalyser extends ClassAnalyser {
	private String className;

	public AssetParserAnalyser() {
		super("AssetParser");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		if (className == null) {
			boolean uiactive = false;
			boolean uidrop = false;

			for (MethodNode mn : cn.methods) {
				for (AbstractInsnNode ain : mn.instructions.toArray()) {
					if (ain instanceof LdcInsnNode) {
						if (((LdcInsnNode) ain).cst.toString().equals("assets.xml")) {
							uiactive = true;
						}

						if (((LdcInsnNode) ain).cst.toString().equals("tileset")) {
							uidrop = true;
						}
					}
				}
			}

			if (uiactive && uidrop) {
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
