package me.themallard.bitmmo.impl.analysis;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.LdcContains;

@SupportedHooks(fields = {}, methods = {})
public class AssetParserAnalyser extends ClassAnalyser {
	public AssetParserAnalyser() {
		super("AssetParser");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		for (MethodNode mn : cn.methods) {
			if (LdcContains.MethodContains(mn, "assets.xml") & LdcContains.MethodContains(mn, "tileset"))
				return true;
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
