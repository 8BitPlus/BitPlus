package me.themallard.bitmmo.impl.analysis;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.hook.MethodHook;

@SupportedHooks(fields = {}, methods = { "FireLaser&(LD;I)V" })
public class LaserAnalyser extends ClassAnalyser {
	private String className;

	public LaserAnalyser() {
		super("Laser");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		if (className == null) {
			boolean takeSelfDamage = false;
			boolean serverName = false;
			boolean playerNearbyTriggerObject = false;

			if (cn.name.toLowerCase().contains("networking"))
				return false;

			for (MethodNode mn : cn.methods) {
				for (AbstractInsnNode ain : mn.instructions.toArray()) {
					if (ain instanceof LdcInsnNode) {
						if (((LdcInsnNode) ain).cst.toString().contains("TakeSelfDamage")) {
							takeSelfDamage = true;
						}

						if (((LdcInsnNode) ain).cst.toString().contains("PlayerNearbyTriggerObject")) {
							playerNearbyTriggerObject = true;
						}

						if (((LdcInsnNode) ain).cst.toString().contains("serverName")) {
							serverName = true;
						}
					}
				}
			}

			if (takeSelfDamage && playerNearbyTriggerObject && !serverName) {
				className = cn.name;
				return true;
			}
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
				for (AbstractInsnNode ain : mn.instructions.toArray()) {
					if (ain instanceof LdcInsnNode) {
						if (((LdcInsnNode) ain).cst.toString().contains("TakeSelfDamage")) {
							list.add(asMethodHook(mn, "FireLaser"));
						}
					}
				}
			}

			return list;
		}

	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return new Builder<IMethodAnalyser>().add(new HurtPlayerAnalyser());
	}
}
