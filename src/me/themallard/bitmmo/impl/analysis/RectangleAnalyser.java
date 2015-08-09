package me.themallard.bitmmo.impl.analysis;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.LdcContains;
import me.themallard.bitmmo.api.hook.FieldHook;

@SupportedHooks(fields = { "x&I", "y&I", "width&I", "height&I" }, methods = {})
public class RectangleAnalyser extends ClassAnalyser {
	public RectangleAnalyser() {
		super("Rectangle");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		return LdcContains.ClassContains(cn, "Rectangle: ");
	}

	public class XYWHAnalyser implements IFieldAnalyser {
		@Override
		public List<FieldHook> find(ClassNode cn) {
			List<FieldHook> list = new ArrayList<FieldHook>();

			// Rectangle.class never changes so it is probably safe to do this
			list.add(asFieldHook(cn.fields.get(0), "x"));
			list.add(asFieldHook(cn.fields.get(1), "y"));
			list.add(asFieldHook(cn.fields.get(2), "width"));
			list.add(asFieldHook(cn.fields.get(3), "height"));

			return list;
		}
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return new Builder<IFieldAnalyser>().add(new XYWHAnalyser());
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return null;
	}
}
