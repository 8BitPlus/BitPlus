package me.themallard.bitmmo.api.analysis;

import java.util.ArrayList;
import java.util.List;

import me.themallard.bitmmo.api.Revision;
import me.themallard.bitmmo.api.util.Filter;

public class AnalysisProviderRegistry {
	private static final List<RegistryEntry> entries = new ArrayList<RegistryEntry>();

	public static void register(RegistryEntry e) {
		entries.add(0, e);
	}

	public static ProviderCreator get(Revision r) {
		for (RegistryEntry e : entries) {
			if (e.accept(r))
				return e.creator();
		}

		throw new UnsupportedOperationException("Unsupported revision " + r.getName());
	}

	public static class RegistryEntry {
		private final ProviderCreator creator;
		private final List<Filter<Revision>> filters;

		public RegistryEntry(ProviderCreator creator) {
			this.creator = creator;
			filters = new ArrayList<Filter<Revision>>();
		}

		public RegistryEntry addFilter(Filter<Revision> f) {
			filters.add(f);
			return this;
		}

		public boolean accept(Revision r) {
			for (Filter<Revision> f : filters) {
				if (f.accept(r))
					return true;
			}

			return false;
		}

		public ProviderCreator creator() {
			return creator;
		}
	}

	public static abstract class ProviderCreator {
		public abstract AbstractAnalysisProvider create(Revision rev) throws Exception;
	}
}
