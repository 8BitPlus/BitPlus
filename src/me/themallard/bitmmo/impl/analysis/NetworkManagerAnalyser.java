package me.themallard.bitmmo.impl.analysis;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.LdcElement;

@SupportedHooks(fields = {}, methods = {})
public class NetworkManagerAnalyser extends ClassAnalyser {
	private static final String[] STRINGS = { "serverName", "serverPort", "serverID", "Server " };

	public NetworkManagerAnalyser() {
		super("NetworkManager");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		for (String s : STRINGS) {
			if (!new PatternBuilder().add(new LdcElement(new LdcInsnNode(s))).build().contains(cn))
				return false;
		}

		return true;
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
