package me.themallard.bitmmo.impl.analysis.ui;

import org.objectweb.asm.tree.ClassNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.LdcContains;

@SupportedHooks(fields = {}, methods = {})
public class BuildToolsAnalyser extends ClassAnalyser {

	public BuildToolsAnalyser() {
		super("ui/BuildTools");
	}

	// unique String "You realize you cannot breathe underwater. DEATH!"

	@Override
	protected boolean matches(ClassNode cn) {		
		return LdcContains.ClassContains(cn, "Z Axis Lock:");
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
