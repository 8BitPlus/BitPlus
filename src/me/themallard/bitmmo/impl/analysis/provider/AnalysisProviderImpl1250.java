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
import me.themallard.bitmmo.impl.analysis.StatsManagerAnalyser;

public class AnalysisProviderImpl1250 extends AnalysisProviderImpl1245 {
	private static class Creator extends AnalysisProviderRegistry.ProviderCreator {
		@Override
		public AbstractAnalysisProvider create(Revision rev) throws Exception {
			return new AnalysisProviderImpl1250(rev);
		}
	}

	public static void init() {
		AnalysisProviderRegistry.register(new RegistryEntry(new Creator()).addFilter(new RevisionFilter(1250)));
	}

	public AnalysisProviderImpl1250(Revision r) throws IOException {
		super(r);
	}

	@Override
	protected Builder<ClassAnalyser> registerAnalysers() {
		return super.registerAnalysers().add(new StatsManagerAnalyser());
	}
}
