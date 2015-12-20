package ojman101.help;

import me.themallard.bitmmo.impl.plugin.chathook.ChatHookManager;
import me.themallard.bitmmo.impl.plugin.chathook.IChatCallback;
import me.themallard.bitmmo.impl.plugin.gamecontext.GameContext;

public class HelpInject implements IChatCallback {
	
	public HelpInject() {
		ChatHookManager.registerCallback(this);
		HelpManager.addHelp("Help", "Lists all of the commands or shows how to use a specific command. /bitHelp or /bitHelp command");
	}

	@Override
	public boolean onChatMessage(String message) {
		if(message.equalsIgnoreCase("/bitHelp")) {
			for(String key : HelpManager.getHelps().keySet()) {
				String value = HelpManager.getHelpDescriptionFor(key);
				GameContext.getChatWindow().addChatMessage(key + " - " + value);
			}
			return true;
		}
		if(message.startsWith("/bitHelp")) {
			String substring = message.substring("/bitHelp ".length());
			GameContext.getChatWindow().addChatMessage(HelpManager.getHelpDescriptionFor(substring));
			return true;
		}
		return false;
	}

	@Override
	public void onReceiveMessage(String message) {}
}
