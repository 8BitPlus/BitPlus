package me.themallard.bitmmo;

import me.themallard.bitmmo.api.Context;
import me.themallard.bitmmo.api.Revision;
import me.themallard.bitmmo.api.analysis.AbstractAnalysisProvider;
import me.themallard.bitmmo.api.util.RevisionHelper;
import me.themallard.bitmmo.impl.AnalysisProviderImpl;

public class Bitmmo {
	public static void main(String[] args) {
		System.out.println("Loading 8BitMMO [" + RevisionHelper.getLatestRevision() + "]...");

		try {
			Revision latest = Revision.create(RevisionHelper.getLatestRevision());
			
			AbstractAnalysisProvider provider = new AnalysisProviderImpl(latest);
			Context.bind(provider);
			provider.run();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
