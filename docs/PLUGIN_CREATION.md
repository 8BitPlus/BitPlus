# Bit+ Plugin Creation Guide

### What are plugins?
* Plugins can run at creation time, or at run time.
	- You probably want both
* What can plugins do?
	- Modify game code at creation
		- Useful for patching things in the game
	- Listen to method calls, and call game methods
		- For interacting with the game as it runs
* How do plugins work?
	- At creation time, plugins are collected and added as transformers
	- They perform changes
		- Modify or inject code
		- Register callbacks
		- Register dependencies to be injected for runtime
	- The changes are exported automagically

### What are the components of a plugin?
* Main plugin file
	- Extends SimplePlugin
	- Has Plugin annotation
	- Manages everything for your plugin
* Injected files
	- Is injected by Main plugin file
	- Contains callbacks/things that actually interract with the
	  game whilst it runs.

### How do I write a plugin?
We will write a simple plugin that responds to "!ping" with "PONG" in the chat.

1. Create plugin package
	- in src\_plugin create a new package example.ping
	- The convention is to use username.plugin\_name
2. Create main plugin class
	- PingBot in example.ping package
	- Add the `@Plugin` annotation (you may have to import Plugin)
	- Extend SimplePlugin (you may have to import SimplePlugin)
	- Create a constructor, and place `super("Ping")` into it
		- This tells Bit+ that this plugin's name is Ping.
		- For now this name is only used internally, but in the future it may
		  be displayed to users.
	- Override the run method
		- Our plugin does not need this method, we can just leave it empty.
	- Your code should look something like this
		```
		package example.ping;

		import org.objectweb.asm.tree.ClassNode;

		import me.themallard.bitmmo.impl.plugin.Plugin;
		import me.themallard.bitmmo.impl.plugin.SimplePlugin;

		@Plugin
		public class PingBot extends SimplePlugin {
			public PingBot() {
				super("Ping");
			}

			@Override
			public void run(ClassNode cn) {
			}
		}
		```
3. Create injected class
	- PingInject in example.ping package
	- Implement the IChatCallback interace
		- We do this so that the Chat Hook Manager can find our methods.
	- Create a constructor
		- Call `ChatHookManager.registerCallback(this);` to tell the
		  Chat Hook Manager we want to be notified by message events.
	- Override `onChatMessage` and `onReceiveMessage`
		- We don't need onChatMessage, but it's a part of the interface
		  so we must implement it anyways.
	- Implement `onReceiveMessage`
		- If the message contains "!time", send a chat message
			- *Hint:* You can send a chat message with
			`GameContext.getChatWindow().sendChatMessage`
	- It should look like this
		```java
		package example.ping;

		import java.text.SimpleDateFormat;
		import java.util.Date;

		import me.themallard.bitmmo.impl.plugin.chathook.ChatHookManager;
		import me.themallard.bitmmo.impl.plugin.chathook.IChatCallback;
		import me.themallard.bitmmo.impl.plugin.gamecontext.GameContext;

		public class PingInject implements IChatCallback {
			public PingInject() {
				ChatHookManager.registerCallback(this);
			}

			@Override
			public void onChatMessage(String x) {
			}

			@Override
			public void onReceiveMessage(String message) {
				if (message.contains("!ping"))
					GameContext.getChatWindow().sendChatMessage("PONG");
			}
		}

		```
4. Register PingInject for injection and instansiation
	- Go back to the PingBot class's constructor
	- Register instansation with registerInstanceCreation
		- Remember that packages are really just directories, so
		  use slashes instead of periods
		- `registerInstanceCreation("example/ping/PingInject");`
	- Register PingInject as a dependency
		- registerDependency expects a ClassNode but we have a class object
			- We can use ClassStructure.create (sorry, expect this to
			  be cleaned up)
			- `registerDependency(ClassStructure.create(PingInject.class.getResourceAsStream("PingInject.class")));`
	- Your constructor should look like this
		```java
		public PingBot() {
			super("Ping");
			registerDependency(ClassStructure.create(PingInject.class.getResourceAsStream("PingInject.class")));
			registerInstanceCreation("maaatts/timebot/PingInject");
		}
		
		```
5. Run Bit+
	- If there are no errors, you win! ^^probably
6. Launch the new gamepack
	- It should be in `out/<version>/refactor_<version>.jar`
	- You can use the Bit+ launcher for this.
7. Test it out!
	- Go in game and type !ping
	- Be impressed
	- Profit