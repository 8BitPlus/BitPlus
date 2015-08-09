package me.themallard.bitmmo.impl;

import java.io.IOException;

import me.themallard.bitmmo.api.Revision;
import me.themallard.bitmmo.api.analysis.AbstractAnalysisProvider;
import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.impl.analysis.*;
import me.themallard.bitmmo.impl.analysis.payment.AmazonPaymentHandlerAnalyser;
import me.themallard.bitmmo.impl.analysis.payment.BitcoinPaymentHandlerAnalyser;
import me.themallard.bitmmo.impl.analysis.payment.GooglePaymentHandlerAnalyser;
import me.themallard.bitmmo.impl.analysis.payment.HumblePaymentHandlerAnalyser;
import me.themallard.bitmmo.impl.analysis.payment.SteamOverlayPaymentHandlerAnalyser;
import me.themallard.bitmmo.impl.analysis.payment.SteamWebPaymentHandlerAnalyser;
import me.themallard.bitmmo.impl.analysis.ui.BuildToolsAnalyser;
import me.themallard.bitmmo.impl.analysis.ui.BuyGoldAnalyser;
import me.themallard.bitmmo.impl.analysis.ui.ChatWindowAnalyser;
import me.themallard.bitmmo.impl.analysis.ui.HUDAnalyser;
import me.themallard.bitmmo.impl.analysis.ui.KeybindMenuAnalyser;
import me.themallard.bitmmo.impl.analysis.ui.LeftMenuAnalyser;
import me.themallard.bitmmo.impl.analysis.ui.LevelupPopupAnalyser;
import me.themallard.bitmmo.impl.analysis.ui.OptionsAnalyser;
import me.themallard.bitmmo.impl.analysis.ui.PickupAnalyser;
import me.themallard.bitmmo.impl.analysis.ui.QuestPanelAnalyser;
import me.themallard.bitmmo.impl.analysis.ui.ReferFriendAnalyser;
import me.themallard.bitmmo.impl.analysis.ui.ShopAnalyser;
import me.themallard.bitmmo.impl.analysis.ui.TradeWindowAnalyser;

public class AnalysisProviderImpl extends AbstractAnalysisProvider {
	public AnalysisProviderImpl(Revision r) throws IOException {
		super(r);
	}

	@Override
	protected Builder<ClassAnalyser> registerAnalysers() {
		Builder<ClassAnalyser> builder = new Builder<ClassAnalyser>();

		builder.addAll(new ClassAnalyser[] { new PlayerAnalyser(), new LaserAnalyser(), new DebugOverlayAnalyser(),
				new MainMenuAnalyser(), new HUDAnalyser(), new OptionsAnalyser(), new AssetParserAnalyser(),
				new AmazonPaymentHandlerAnalyser(), new BitcoinPaymentHandlerAnalyser(), new OptionsAnalyser(),
				new GooglePaymentHandlerAnalyser(), new PickupAnalyser(), new SteamOverlayPaymentHandlerAnalyser(),
				new HumblePaymentHandlerAnalyser(), new SteamWebPaymentHandlerAnalyser(), new QuestPanelAnalyser(),
				new ReferFriendAnalyser(), new BuyGoldAnalyser(), new ShopAnalyser(), new SFXManagerAnalyser(),
				new KeybindMenuAnalyser(), new LevelupPopupAnalyser(), new LeftMenuAnalyser(),
				new BGMDownloaderAnalyser(), new TradeWindowAnalyser(), new BuildToolsAnalyser(),
				new RectangleAnalyser(), new ChatWindowAnalyser(), new DirectionAnalyser() });

		return builder;
	}
}
