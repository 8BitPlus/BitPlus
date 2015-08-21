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

package me.themallard.bitmmo.impl.analysis;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.LdcContains;
import me.themallard.bitmmo.api.hook.MethodHook;

@SupportedHooks(fields = {}, methods = { "update&(I)V", "init&()V" })
public class DebugOverlayAnalyser extends ClassAnalyser {
	public DebugOverlayAnalyser() {
		super("DebugOverlay");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		return LdcContains.ClassContains(cn, " / Ping: ");
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return null;
	}

	public class UpdateMethodAnalyser implements IMethodAnalyser {
		@Override
		public List<MethodHook> find(ClassNode cn) {
			List<MethodHook> list = new ArrayList<MethodHook>();

			for (MethodNode mn : cn.methods) {
				if (LdcContains.MethodContains(mn, " / Ping: "))
					list.add(asMethodHook(mn, "update"));
			}

			return list;
		}
	}

	public class InitMethodAnalyser implements IMethodAnalyser {
		@Override
		public List<MethodHook> find(ClassNode cn) {
			List<MethodHook> list = new ArrayList<MethodHook>();

			for (MethodNode mn : cn.methods) {
				if (LdcContains.MethodContains(mn, "Stats go here."))
					list.add(asMethodHook(mn, "init"));
			}

			return list;
		}
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return new Builder<IMethodAnalyser>().addAll(new UpdateMethodAnalyser(), new InitMethodAnalyser());
	}
}
