package ojman101.help;

import me.themallard.bitmmo.impl.plugin.chathook.ChatHookManager;
import me.themallard.bitmmo.impl.plugin.chathook.IChatCallback;
import me.themallard.bitmmo.impl.plugin.gamecontext.GameContext;

public class HelpInject implements IChatCallback {
	public HelpInject() {
		ChatHookManager.registerCallback(this);
		HelpManager.addHelp("bithelp", "List all of the commands.");
		HelpManager.addHelp("bithelp", "<cmd> | List a description of a command.");
	}

	@Override
	public boolean onChatMessage(String message) {
		if (message.equalsIgnoreCase("/bithelp")) {
			GameContext.getChatWindow().addChatMessage("[Bit+] Listing all commands...");

			StringBuilder sb = new StringBuilder();

			int i = 1;
			for (String key : HelpManager.getHelps().keySet()) {
				sb.append(padString(key, 25));
				if (i++ % 2 == 0) sb.append('\n');
			}

			GameContext.getChatWindow().addChatMessage(sb.toString());

			return true;
		}

		if (message.toLowerCase().startsWith("/bithelp ")) {
			String substring = message.substring("/bitHelp ".length());

			GameContext.getChatWindow().addChatMessage("[Bit+] Showing help for /" + substring + "...");
			GameContext.getChatWindow().addChatMessage(HelpManager.getHelpDescriptionFor(substring));
			return true;
		}
		return false;
	}

	private static String padString(String s, int l) {
		StringBuilder sb = new StringBuilder(s);
		int a = l - s.length();
		for (int i = 0; i <= a; i++)
			sb.append(' ');
		return sb.toString();
	}

	@Override
	public void onReceiveMessage(String message) {
	}
}