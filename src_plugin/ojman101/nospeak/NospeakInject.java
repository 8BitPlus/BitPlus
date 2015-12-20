package ojman101.nospeak;

import me.themallard.bitmmo.impl.plugin.chathook.ChatHookManager;
import me.themallard.bitmmo.impl.plugin.chathook.IChatCallback;
import me.themallard.bitmmo.impl.plugin.gamecontext.GameContext;
import ojman101.help.HelpManager;

public class NospeakInject implements IChatCallback {
	
	private boolean nospeak;
	
	public NospeakInject() {
		ChatHookManager.registerCallback(this);
		HelpManager.addHelp("Nospeak", "Stops you from speaking in the chat. /nospeak");
	}

	@Override
	public boolean onChatMessage(String message) {
		if(message.startsWith("/nospeak")) {
			nospeak = !nospeak;
			StringBuilder sb = new StringBuilder();
			sb.append("[Bit+] Nospeak");
			sb.append(nospeak ? "Enabled" : "Disabled");
			GameContext.getChatWindow().addChatMessage(sb.toString());
			return true;
		}
		return nospeak;
	}

	@Override
	public void onReceiveMessage(String message) {
	}
}
