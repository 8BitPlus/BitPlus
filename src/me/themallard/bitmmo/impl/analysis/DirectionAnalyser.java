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
public class DirectionAnalyser extends ClassAnalyser {
	public DirectionAnalyser() {
		super("Direction");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		if (!cn.superName.equals("java/lang/Enum"))
			return false;

		for (MethodNode mn : cn.methods) {
			if (!mn.name.equals("<clinit>"))
				continue;

			return LdcContains.MethodContains(mn, "NORTH") && LdcContains.MethodContains(mn, "EAST")
					&& LdcContains.MethodContains(mn, "SOUTH") && LdcContains.MethodContains(mn, "WEST");
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
