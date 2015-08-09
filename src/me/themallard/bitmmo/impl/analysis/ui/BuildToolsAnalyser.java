package me.themallard.bitmmo.impl.analysis.ui;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.LdcContains;
import me.themallard.bitmmo.api.hook.MethodHook;

@SupportedHooks(fields = {}, methods = { "decrementLock&()V", "incrementLock&()V" })
public class BuildToolsAnalyser extends ClassAnalyser implements Opcodes {

	public BuildToolsAnalyser() {
		super("ui/BuildTools");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		return LdcContains.ClassContains(cn, "Z Axis Lock:");
	}

	public class AxisLockModifierAnalyser implements IMethodAnalyser {

		// TODO: Use a Filter for this?

		// pattern
		// dup
		// getfield *
		// bipush 8
		// isub
		// putfield *

		@Override
		public List<MethodHook> find(ClassNode cn) {
			List<MethodHook> list = new ArrayList<MethodHook>();

			// i am a bad person
			for (MethodNode mn : cn.methods) {
				for (int i = 0; i < mn.instructions.size() - 5; i++) {
					if (mn.instructions.get(i).opcode() == DUP & mn.instructions.get(i + 1).opcode() == GETFIELD
							&& mn.instructions.get(i + 2).opcode() == BIPUSH
							&& mn.instructions.get(i + 3).opcode() == ISUB
							|| mn.instructions.get(i + 3).opcode() == IADD
									& mn.instructions.get(i + 4).opcode() == PUTFIELD) {
						if (mn.instructions.get(i + 3).opcode() == ISUB)
							list.add(asMethodHook(mn, "decrementLock"));
						else
							list.add(asMethodHook(mn, "incrementLock"));
					}
				}
			}

			return list;
		}

	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return null;
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return new Builder<IMethodAnalyser>().add(new AxisLockModifierAnalyser());
	}
}
