# Setting up Bit+ Plugin Development Workspace
### Prerequisites
* The JDK (for 1.8)
* The Eclipse IDE, or similar
* Bit+ downloaded and working

### Method
1. Create a new Eclipse workspace<br>
![Creating a new workspace](http://i.imgur.com/yzrhqYC.png)
2. Create a new Java Project<br>
![Opening the frame](http://i.imgur.com/vOejHry.png)<br><br>
![Creating new project](http://i.imgur.com/Urv4Koc.png)
3. Add the Bit+ jar to the classpath
    - Right click the project and click on Properties<br>
      ![The properties window](http://i.imgur.com/uGxC10o.png)
    - Open the `Java Build Path` pane, and navigate to the `Libraries` tab<br>
      ![Java Build Path](http://i.imgur.com/Xrd9ATs.png)
    - Click on `Add External JARs...` and open the Bit+ jar.<br>
      ![Selecting JAR](http://i.imgur.com/YBLCz9B.png)
    - Click `Ok` to close the Properties panel
4. You are now ready to follow the other tutorials!<br>
`PLUGIN_CREATION.md` is a good starting point.

### Notes
* If you move the Bit+ jar, remember to edit the classpath again to
reflect the move.
* Always remember to keep Bit+ updated to ensure compatibility.
