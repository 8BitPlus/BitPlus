package me.themallard.bitmmo.impl.analysis.payment;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.LdcContains;
import me.themallard.bitmmo.api.hook.MethodHook;

@SupportedHooks(fields = {}, methods = { "openWebpage&()V" })
public abstract class AbstractPaymentHandlerAnalyser extends ClassAnalyser {
	private String paymentUrl;

	public AbstractPaymentHandlerAnalyser(String name, String paymentUrl) {
		super("payment/" + name + "PaymentHandler");
		this.paymentUrl = paymentUrl;
	}

	@Override
	protected boolean matches(ClassNode cn) {
		return LdcContains.ClassContains(cn, paymentUrl);
	}

	public class OpenWebpageAnalyser implements IMethodAnalyser {

		@Override
		public List<MethodHook> find(ClassNode cn) {
			List<MethodHook> list = new ArrayList<MethodHook>();

			for (MethodNode mn : cn.methods) {
				for (AbstractInsnNode ain : mn.instructions.toArray()) {
					if (!(ain instanceof MethodInsnNode))
						continue;

					MethodInsnNode min = (MethodInsnNode) ain;

					if (min.name.equals("openURL")) {
						list.add(asMethodHook(mn, "openWebpage"));
						break;
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
		return new Builder<IMethodAnalyser>().add(new OpenWebpageAnalyser());
	}
}
