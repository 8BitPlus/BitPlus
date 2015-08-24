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

package me.themallard.bitmmo.impl.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nullbool.api.util.NodeTable;
import org.objectweb.asm.tree.ClassNode;

import eu.infomas.annotation.AnnotationDetector;
import eu.infomas.annotation.Cursor;
import eu.infomas.annotation.ReporterFunction;
import me.themallard.bitmmo.api.transformer.Transformer;

public class PluginLoader {
	private List<SimplePlugin> plugins;

	public PluginLoader() {
		plugins = getPlugins();

		for (SimplePlugin p : plugins) {
			System.out.println("Found plugin " + p.getName());
		}
	}

	public void run(Map<String, ClassNode> classNodes) {
		for (Transformer t : plugins) {
			for (ClassNode cn : classNodes.values()) {
				if (t.accept(cn))
					t.run(cn);
			}
		}
	}

	public NodeTable<ClassNode> getDependencies() {
		NodeTable<ClassNode> ret = new NodeTable<ClassNode>();

		for (SimplePlugin p : plugins) {
			ret.putAll(p.getDependencies());
		}

		return ret;
	}

	private List<SimplePlugin> getPlugins() {
		try {
			@SuppressWarnings("unchecked")
			List<Class<?>> types = AnnotationDetector.scanClassPath().forAnnotations(Plugin.class)
					.collect(new ReporterFunction<Class<?>>() {
						@Override
						public Class<?> report(Cursor cursor) {
							return AnnotationDetector.loadClass(Thread.currentThread().getContextClassLoader(),
									cursor.getTypeName());
						}
					});

			List<SimplePlugin> list = new ArrayList<SimplePlugin>();

			for (Class<?> clazz : types) {
				list.add((SimplePlugin) clazz.newInstance());
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		return null;
	}
}
