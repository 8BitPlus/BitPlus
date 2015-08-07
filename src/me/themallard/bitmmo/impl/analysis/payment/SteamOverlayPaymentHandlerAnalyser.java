package me.themallard.bitmmo.impl.analysis.payment;

public class SteamOverlayPaymentHandlerAnalyser extends AbstractPaymentHandlerAnalyser {
	public SteamOverlayPaymentHandlerAnalyser() {
		super("SteamOverlay", "https://8bitmmo.net/steam.php?overlay=true&do=igp&packID=");
	}
}
