package me.themallard.bitmmo.impl.analysis.payment;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.util.LdcContains;
import me.themallard.bitmmo.api.hook.MethodHook;

public class SteamOverlayPaymentHandlerAnalyser extends AbstractPaymentHandlerAnalyser {
	public SteamOverlayPaymentHandlerAnalyser() {
		super("SteamOverlay", "https://8bitmmo.net/steam.php?overlay=true&do=igp&packID=");
	}

	// SteamOverlay handler does not have a call to open the web browser
	// therefore we use a different method here
	public class OpenWebpageAnalyser implements IMethodAnalyser {

		@Override
		public List<MethodHook> find(ClassNode cn) {
			List<MethodHook> list = new ArrayList<MethodHook>();

			for (MethodNode mn : cn.methods) {
				if (LdcContains.MethodContains(mn, "Response from kicking off steam purchase url:")) {
					list.add(asMethodHook(mn, "openWebpage"));
					break;
				}
			}

			return list;
		}

	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return new Builder<IMethodAnalyser>().add(new OpenWebpageAnalyser());
	}
}
