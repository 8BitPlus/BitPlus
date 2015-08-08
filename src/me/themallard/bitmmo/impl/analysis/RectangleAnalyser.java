package me.themallard.bitmmo.impl.analysis;

import org.objectweb.asm.tree.ClassNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.LdcContains;

@SupportedHooks(fields = {}, methods = {})
public class RectangleAnalyser extends ClassAnalyser {
	public RectangleAnalyser() {
		super("Rectangle");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		return LdcContains.ClassContains(cn, "Rectangle: ");
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
