package ojman101.help;

import java.util.HashMap;
import java.util.Map;

public class HelpManager {
	
	private static Map<String, String> lookup = new HashMap<String, String>();
	
	public static void addHelp(String name, String desc) {
		lookup.put(name.toLowerCase(), desc);
	}
	
	public static Map<String, String> getHelps() {
		return lookup;
	}
	
	public static String getHelpDescriptionFor(String pluginName) {
		return lookup.containsKey(pluginName.toLowerCase()) ? lookup.get(pluginName.toLowerCase()) : "Command not found.";
	}
}