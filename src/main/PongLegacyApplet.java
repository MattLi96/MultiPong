package main;
//Reference the required Java libraries
import java.applet.Applet; 
import java.awt.*; 
import javax.swing.*;


import menus.MainMenu;

//The applet code
public class PongLegacyApplet extends Applet {
	Button clickOnMe;
	public void init (){
		clickOnMe = new Button("Start Pong Legacy!");	
		add(clickOnMe);
	}
	public void paint(Graphics g) {
		g.drawRect(40,0,150,150); 
		g.setColor(Color.blue);
		g.drawString(" Multiplayer Pong Game.",50,50);
		g.drawString("have fun with your friends!", 45,60);
  }
  
	@SuppressWarnings("deprecation")
	public boolean action (Event e, Object args){ 
		String[] hoz = {};
		PongLegacy.main(hoz);
	   
	return true;
	}
} 
