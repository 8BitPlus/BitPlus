package me.themallard.bitmmo.impl.analysis;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;

import me.themallard.bitmmo.api.analysis.Builder;
import me.themallard.bitmmo.api.analysis.ClassAnalyser;
import me.themallard.bitmmo.api.analysis.IFieldAnalyser;
import me.themallard.bitmmo.api.analysis.IMethodAnalyser;
import me.themallard.bitmmo.api.analysis.SupportedHooks;
import me.themallard.bitmmo.api.analysis.util.pattern.PatternBuilder;
import me.themallard.bitmmo.api.analysis.util.pattern.element.LdcElement;
import me.themallard.bitmmo.api.hook.FieldHook;

@SupportedHooks(fields = { "socket&Ljava/net/Socket;", "outputStream&Ljava/io/OutputStream;",
		"inputStream&Ljava/io/InputStream;", "dataOutputStream&Ljava/io/DataOutputStream;",
		"dataInputStream&Ljava/io/DataInputStream;" }, methods = {})
public class NetworkManagerAnalyser extends ClassAnalyser {
	private static final String[] STRINGS = { "serverName", "serverPort", "serverID", "Server " };

	public NetworkManagerAnalyser() {
		super("NetworkManager");
	}

	@Override
	protected boolean matches(ClassNode cn) {
		for (String s : STRINGS) {
			if (!new PatternBuilder().add(new LdcElement(new LdcInsnNode(s))).build().contains(cn))
				return false;
		}

		return true;
	}

	public class FieldTypeAnalyser implements IFieldAnalyser {
		@Override
		public List<FieldHook> find(ClassNode cn) {
			List<FieldHook> list = new ArrayList<FieldHook>();

			for (FieldNode fn : cn.fields) {
				switch (fn.desc) {
				case "Ljava/net/Socket;":
					list.add(asFieldHook(fn, "socket"));
					break;
				case "Ljava/io/OutputStream;":
					list.add(asFieldHook(fn, "outputStream"));
					break;
				case "Ljava/io/InputStream;":
					list.add(asFieldHook(fn, "inputStream"));
					break;
				case "Ljava/io/DataOutputStream;":
					list.add(asFieldHook(fn, "dataOutputStream"));
					break;
				case "Ljava/io/DataInputStream;":
					list.add(asFieldHook(fn, "dataInputStream"));
					break;
				default:
					break;
				}
			}

			return list;
		}
	}

	@Override
	protected Builder<IFieldAnalyser> registerFieldAnalysers() {
		return new Builder<IFieldAnalyser>().add(new FieldTypeAnalyser());
	}

	@Override
	protected Builder<IMethodAnalyser> registerMethodAnalysers() {
		return null;
	}
}
