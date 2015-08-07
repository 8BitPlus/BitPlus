package me.themallard.bitmmo.impl.analysis.payment;

public class SteamWebPaymentHandlerAnalyser extends AbstractPaymentHandlerAnalyser {
	public SteamWebPaymentHandlerAnalyser() {
		super("SteamWeb", "https://8bitmmo.net/steam.php?do=igp&packID=");
	}
}
