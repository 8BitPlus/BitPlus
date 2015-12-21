package ojman101.walkspeed;

import me.themallard.bitmmo.impl.plugin.chathook.ChatHookManager;
import me.themallard.bitmmo.impl.plugin.chathook.IChatCallback;
import me.themallard.bitmmo.impl.plugin.gamecontext.GameContext;
import ojman101.help.HelpManager;

public class WalkspeedInject implements IChatCallback {
	
	private static double masterWalkSpeed = 0.08D;
	private static double roadWalkSpeed = 2D;
	private static double sandWalkSpeed = 0.5D;
	
	public WalkspeedInject() {
		ChatHookManager.registerCallback(this);
		HelpManager.addHelp("Walkspeed", "Changes the walk speed /masterwalkspeed walkspeed, /roadwalkspeed walkspeed and /sandwalkspeed walkspeed");
	}
	
	public static double getWalkSpeed() {
		return masterWalkSpeed;
	}
	
	public static double getRoadWalkSpeed() {
		return roadWalkSpeed;
	}
	
	public static double getSandWalkSpeed() {
		return sandWalkSpeed;
	}

	@Override
	public boolean onChatMessage(String message) {
		if(message.startsWith("/masterwalkspeed")) {
			String substring = message.substring("/masterwalkspeed ".length());
			try {
				double d = Double.parseDouble(substring);
				masterWalkSpeed = d;
				GameContext.getChatWindow().addChatMessage("[Bit+] Walkspeed set to " + d);
			} catch(NumberFormatException e) {
				GameContext.getChatWindow().addChatMessage("[Bit+] Invalid command usage!\nTry /masterwalkspeed walkspeed");
			}
			return true;
		}
		if(message.startsWith("/roadwalkspeed")) {
			String substring = message.substring("/roadwalkspeed ".length());
			try {
				double d = Double.parseDouble(substring);
				roadWalkSpeed = d;
				GameContext.getChatWindow().addChatMessage("[Bit+] Road walkspeed set to " + d);
			} catch(NumberFormatException e) {
				GameContext.getChatWindow().addChatMessage("[Bit+] Invalid command usage!\nTry /roadwalkspeed walkspeed");
			}
			return true;
		}
		if(message.startsWith("/sandwalkspeed")) {
			String substring = message.substring("/sandwalkspeed ".length());
			try {
				double d = Double.parseDouble(substring);
				sandWalkSpeed = d;
				GameContext.getChatWindow().addChatMessage("[Bit+] Sand walkspeed set to " + d);
			} catch(NumberFormatException e) {
				GameContext.getChatWindow().addChatMessage("[Bit+] Invalid command usage!\nTry /sandwalkspeed walkspeed");
			}
			return true;
		}
		return false;
	}

	@Override
	public void onReceiveMessage(String message) {}
}
