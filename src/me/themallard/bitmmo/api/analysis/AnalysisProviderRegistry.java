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
