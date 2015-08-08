package me.themallard.bitmmo.impl.analysis;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.LdcContains;
import me.themallard.bitmmo.api.hook.MethodHook;

@SupportedHooks(fields = {}, methods = { "FireLaser&(LD;I)V" })
public class LaserAnalyser extends ClassAnalyser {
	public LaserAnalyser() {
		super("Laser");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		if (cn.name.toLowerCase().contains("networking"))
			return false;

		for (MethodNode mn : cn.methods) {
			if (LdcContains.MethodContains(mn, "TakeSelfDamage")
					& LdcContains.MethodContains(mn, "PlayerNearbyTriggerObject")
					& LdcContains.MethodContains(mn, "serverName"))
				return true;
		}

		return false;
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return null;
	}

	public class HurtPlayerAnalyser implements IMethodAnalyser {

		@Override
		public List<MethodHook> find(ClassNode cn) {
			List<MethodHook> list = new ArrayList<MethodHook>();

			for (MethodNode mn : cn.methods) {
				if (LdcContains.MethodContains(mn, "TakeSelfDamage"))
					list.add(asMethodHook(mn, "FireLaser"));
			}

			return list;
		}

	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return new Builder<IMethodAnalyser>().add(new HurtPlayerAnalyser());
	}
}
