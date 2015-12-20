package ojman101.help;

import org.objectweb.asm.tree.ClassNode;

import me.themallard.bitmmo.impl.plugin.Plugin;
import me.themallard.bitmmo.impl.plugin.SimplePlugin;

@Plugin
public class HelpPlugin extends SimplePlugin {

	public HelpPlugin() {
		super("Help");
		registerDependency(HelpInject.class);
		registerDependency(HelpManager.class);
		registerInstanceCreation("ojman101/help/HelpInject");
	}

	@Override
	public void run(ClassNode cn) {}
}
