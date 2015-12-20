package ojman101.nospeak;

import org.objectweb.asm.tree.ClassNode;

import me.themallard.bitmmo.impl.plugin.Plugin;
import me.themallard.bitmmo.impl.plugin.SimplePlugin;

@Plugin
public class NospeakPlugin extends SimplePlugin {

	public NospeakPlugin() {
		super("Nospeak");
		registerDependency(NospeakInject.class);
		registerInstanceCreation("ojman101/nospeak/NospeakInject");
	}

	@Override
	public void run(ClassNode cn) {}
}
