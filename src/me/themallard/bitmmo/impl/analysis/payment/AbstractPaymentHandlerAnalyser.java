package me.themallard.bitmmo.impl.analysis.payment;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;

@SupportedHooks(fields = {}, methods = {})
public abstract class AbstractPaymentHandlerAnalyser extends ClassAnalyser {
	private String className;
	private String paymentUrl;

	public AbstractPaymentHandlerAnalyser(String name, String paymentUrl) {
		super("payment/" + name + "PaymentHandler");
		this.paymentUrl = paymentUrl;
	}

	@Override
	protected boolean matches(ClassNode cn) {
		if (className == null) {
			for (MethodNode mn : cn.methods) {
				for (AbstractInsnNode ain : mn.instructions.toArray()) {
					if (ain instanceof LdcInsnNode) {
						if (((LdcInsnNode) ain).cst.toString().equals(paymentUrl)) {
							className = cn.name;

							return true;
						}
					}
				}
			}
		}

		return false;
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
