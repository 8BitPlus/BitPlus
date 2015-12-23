/* Copyright (C) 2015 maaatts

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package me.themallard.bitmmo.impl.analysis.provider;

import java.io.IOException;

import me.themallard.bitmmo.api.Revision;
import me.themallard.bitmmo.api.analysis.AbstractAnalysisProvider;
import me.themallard.bitmmo.api.analysis.AnalysisProviderRegistry;
import me.themallard.bitmmo.api.analysis.AnalysisProviderRegistry.RegistryEntry;
import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.impl.RevisionFilter;
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
	private static class Creator extends AnalysisProviderRegistry.ProviderCreator {
		@Override
		public AbstractAnalysisProvider create(Revision rev) throws Exception {
			return new AnalysisProviderImpl(rev);
		}
	}

	public static void init() {
		AnalysisProviderRegistry.register(new RegistryEntry(new Creator()).addFilter(new RevisionFilter(1)));
	}

	public AnalysisProviderImpl(Revision r) throws IOException {
		super(r);
	}

	@Override
	protected Builder<ClassAnalyser> registerAnalysers() {
		Builder<ClassAnalyser> builder = new Builder<ClassAnalyser>();

		builder.addAll(new ClassAnalyser[] { new PlayerAnalyser(), new DebugOverlayAnalyser(), new MainMenuAnalyser(),
				new HUDAnalyser(), new OptionsAnalyser(), new AssetParserAnalyser(), new AmazonPaymentHandlerAnalyser(),
				new BitcoinPaymentHandlerAnalyser(), new OptionsAnalyser(), new GooglePaymentHandlerAnalyser(),
				new PickupAnalyser(), new SteamOverlayPaymentHandlerAnalyser(), new HumblePaymentHandlerAnalyser(),
				new SteamWebPaymentHandlerAnalyser(), new QuestPanelAnalyser(), new ReferFriendAnalyser(),
				new BuyGoldAnalyser(), new ShopAnalyser(), new SFXManagerAnalyser(), new KeybindMenuAnalyser(),
				new LevelupPopupAnalyser(), new LeftMenuAnalyser(), new BGMDownloaderAnalyser(),
				new TradeWindowAnalyser(), new BuildToolsAnalyser(), new RectangleAnalyser(), new ChatWindowAnalyser(),
				new DirectionAnalyser(), new InputHandlerAnalyser(), new AudioClipAnalyser(), new LongWrapperAnalyser(),
				new FontRendererAnalyser(), new LaserWeapon(), new PositionAnalyser(), new EntityAnalyser(),
				new RifleAnalyser(), new NetworkManagerAnalyser(), new GodObjectAnalyser(),
				new InputActionTrackerAnalyser(), new GameThreadAnalyser(), new KeyAnalyser() });

		return builder;
	}
}
