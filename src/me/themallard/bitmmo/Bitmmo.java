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

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import me.themallard.bitmmo.api.Context;
import me.themallard.bitmmo.api.Revision;
import me.themallard.bitmmo.api.analysis.AbstractAnalysisProvider;
import me.themallard.bitmmo.api.analysis.AnalysisProviderRegistry;
import me.themallard.bitmmo.api.transformer.AbstractTransformerRegistry;
import me.themallard.bitmmo.api.util.IRevisionHelper;
import me.themallard.bitmmo.impl.BitRevisionHelper;
import me.themallard.bitmmo.impl.analysis.provider.ProviderRegistry;
import me.themallard.bitmmo.impl.plugin.PluginLoader;
import me.themallard.bitmmo.impl.transformer.TransformerRegistryImpl;

public class Bitmmo {
	public static final String VERSION = "1.3.0";
	public static boolean copygamepack = true;
	public static final List<String> PLUGIN_BLACKLIST = new ArrayList<String>();

	public static void main(String[] args) {
		System.out.printf(
				"Bit+ %s Copyright (c) 2015 maaatts\n"
						+ "This program comes with ABSOLUTELY NO WARRANTY; for details check LICENSE.md.\n"
						+ "This is free software, and you are welcome to redistribute it under certain conditions.\n",
				VERSION);

		// TODO: Replace this with something nice
		ProviderRegistry.init();

		String revision = null;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-r")) {
				revision = args[i + 1];
			}

			if (args[i].equals("--nocopy")) {
				copygamepack = false;
			}

			if (args[i].equals("-b")) {
				PLUGIN_BLACKLIST.add(args[i + 1]);
			}

			if (args[i].equals("--help")) {
				System.out.println("-r <revision> = Select a different game version");
				System.out.println("--nocopy      = Don't create gamepack.jar");
				System.out.println("-b <plugin>   = Blacklist a plugin");
				System.exit(0);
			}
		}

		{
			new File("./resources/jars/").mkdirs();
		}

		IRevisionHelper helper = new BitRevisionHelper();

		if (revision == null) {
			revision = helper.getLatestRevision();
		}

		if (!Revision.getFile(revision).exists()) {
			System.out.printf("Could not locate revision %s on your disk, attempting to fetch from upload site...\n",
					revision);

			try {
				URL web = new URL(String.format("%s/HTMudWeb_%s.jar", helper.getUploadSite(), revision));
				Revision.getFile(revision).createNewFile();
				Files.copy(web.openStream(), Revision.getFile(revision).toPath(), StandardCopyOption.REPLACE_EXISTING);
				Thread.sleep(100);
			} catch (Exception e) {
				System.err.println("Failed to download revision.\nYou could try downloading it yourself.");
				e.printStackTrace();
				System.exit(1);
			}
		}

		Revision latest = Revision.create(revision);

		System.out.println("Loading 8BitMMO [" + revision + "]...");

		try {
			PluginLoader pl = new PluginLoader();

			AbstractAnalysisProvider analysis = AnalysisProviderRegistry.get(latest).create(latest);
			Context.bind(analysis);
			AbstractTransformerRegistry transformer = new TransformerRegistryImpl();
			pl.run(transformer.run(analysis.run()));
			analysis.getClassNodes().putAll(pl.getDependencies());
			analysis.dump();

			if (copygamepack) {
				Thread.sleep(50);
				Files.copy(new File(String.format("./out/refactor_%s.jar", revision, revision)).toPath(),
						new File("./gamepack.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
