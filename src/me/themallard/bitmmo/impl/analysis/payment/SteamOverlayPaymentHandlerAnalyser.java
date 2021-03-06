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

package me.themallard.bitmmo.impl.analysis.payment;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.LdcElement;
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
				if (new PatternBuilder()
						.add(new LdcElement(new LdcInsnNode("Response from kicking off steam purchase url:"))).build()
						.contains(cn)) {
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
