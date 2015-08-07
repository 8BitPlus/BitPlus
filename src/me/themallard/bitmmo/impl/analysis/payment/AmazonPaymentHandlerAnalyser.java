package me.themallard.bitmmo.impl.analysis.payment;

public class AmazonPaymentHandlerAnalyser extends AbstractPaymentHandlerAnalyser {
	public AmazonPaymentHandlerAnalyser() {
		super("Amazon", "https://8bitmmo.net/amazon/prod/mkPurchase.php?name=");
	}
}
