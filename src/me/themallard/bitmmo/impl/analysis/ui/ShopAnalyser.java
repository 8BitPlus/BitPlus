package me.themallard.bitmmo.impl.analysis.ui;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.LdcContains;

@SupportedHooks(fields = {}, methods = {})
public class ShopAnalyser extends ClassAnalyser {
	public ShopAnalyser() {
		super("ui/Shop");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		for (MethodNode mn : cn.methods) {
			if (LdcContains.MethodContains(mn, "x10") & LdcContains.MethodContains(mn, "buy"))
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
