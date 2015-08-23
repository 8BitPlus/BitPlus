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

package me.themallard.bitmmo;

import me.themallard.bitmmo.api.Context;
import me.themallard.bitmmo.api.Revision;
import me.themallard.bitmmo.api.analysis.AbstractAnalysisProvider;
import me.themallard.bitmmo.api.analysis.AnalysisProviderRegistry;
import me.themallard.bitmmo.api.transformer.AbstractTransformerRegistry;
import me.themallard.bitmmo.api.util.IRevisionHelper;
import me.themallard.bitmmo.impl.BitRevisionHelper;
import me.themallard.bitmmo.impl.analysis.provider.ProviderRegistry;
import me.themallard.bitmmo.impl.transformer.TransformerRegistryImpl;

public class Bitmmo {
	public static void main(String[] args) {
		// TODO: Replace this with something nice
		ProviderRegistry.init();

		IRevisionHelper revisionHelper = new BitRevisionHelper();

		System.out.println("Bit+ Copyright (c) 2015 maaatts\n"
				+ "This program comes with ABSOLUTELY NO WARRANTY; for details check LICENSE.md.\n"
				+ "This is free software, and you are welcome to redistribute it under certain conditions.\n");

		System.out.println("Loading 8BitMMO [" + revisionHelper.getLatestRevision() + "]...");

		try {
			Revision latest = Revision.create(revisionHelper.getLatestRevision());

			AbstractAnalysisProvider analysis = AnalysisProviderRegistry.get(latest).create(latest);
			Context.bind(analysis);
			AbstractTransformerRegistry transformer = new TransformerRegistryImpl();
			transformer.run(analysis.run());
			analysis.dump();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
