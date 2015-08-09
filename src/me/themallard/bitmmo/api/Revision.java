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

package me.themallard.bitmmo.api;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.jar.JarFile;

import org.nullbool.api.util.JarParser;
import org.objectweb.asm.tree.ClassNode;

import me.themallard.bitmmo.api.output.resource.OutputResourceList;

public class Revision {
	private final String name;
	private final File file;
	private JarParser parser;

	public Revision(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public File getFile() {
		return file;
	}

	public Map<String, ClassNode> parse() throws IOException {
		if (parser == null)
			parser = new JarParser(new JarFile(file));
		
		return parser.getParsedClasses();
	}
	
	public OutputResourceList parseResources() throws IOException {
		if (parser == null)
			parser = new JarParser(new JarFile(file));
		
		return parser.getParsedResources();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Revision other = (Revision) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public static Revision create(String revision) throws URISyntaxException {
		return new Revision(revision,
				new File(Revision.class.getResource("/jars/HTMudWeb_" + revision + ".jar").toURI()));
	}
}
