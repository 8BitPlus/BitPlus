package me.themallard.bitmmo.impl.analysis;

import org.objectweb.asm.tree.ClassNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;

@SupportedHooks(fields = {}, methods = {})
public class InputHandlerAnalyser extends ClassAnalyser {

	public InputHandlerAnalyser() {
		super("InputHandler");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		return cn.interfaces.contains("java/awt/event/FocusListener")
				&& cn.interfaces.contains("java/awt/event/KeyListener")
				&& cn.interfaces.contains("java/awt/event/MouseListener")
				&& cn.interfaces.contains("java/awt/event/MouseMotionListener")
				&& cn.interfaces.contains("java/awt/event/MouseWheelListener");
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return null;
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return null;
	}

}
