package boatGamePackage;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;

import edu.princeton.cs.introcs.Draw;

public class Menu implements Runnable {
	private Button myPlayButton;
	private Button myTitle;
	private Draw myDraw;
	private Button myOptions;
	private Button myHostServer;
	private Button myJoinServer;
	
	// Constructor \\
	public Menu() {
		myDraw = new Draw("Boat Game Menu");
		myDraw.setXscale(-1.0, 1.0);
		myDraw.setYscale(-1.0, 1.0);
		myPlayButton = new Button(myDraw, "Play!", 0, 0, Color.BLUE);
		myOptions = new Button(myDraw, "Options", 0, -0.25, Color.BLUE);
		myTitle = new Button(myDraw, "The Boat Game", 0, 0.75, Color.BLUE);
		myHostServer = new Button(myDraw, "Host Server", 0, -.5, Color.BLUE);
		myJoinServer = new Button(myDraw, "Join Server", 0, -.75, Color.BLUE);
	}
	
	// Renders all the elements in the menu and draws a square to refresh the window
	public void render() throws ClassNotFoundException, IOException {
		myDraw.setPenColor(Color.white);
		myDraw.filledSquare(0, 0, 1.0);
		myTitle.render();
		myPlayButton.render();
		myOptions.render();
		myHostServer.render();
		myJoinServer.render();
		
		if (myPlayButton.isClicked()) {
			// Destroy the menu frame
			myDraw.frame.setVisible(false);
			myDraw.frame.dispose();
			// Create the game
			GameBattle game = new GameBattle(false, null, null, 0);
			//Thread t = new Thread(game);
			//t.start();
			game.loop();
		}
		
		// Open the options menu
		if (myOptions.isClicked()) {
			myDraw.frame.setVisible(false);
			myDraw.frame.dispose();
			
			OptionsMenu om = new OptionsMenu(false);
			om.loop();
		}
		
		// Start a server
		if (myHostServer.isClicked()) {
			myDraw.frame.setVisible(false);
			myDraw.frame.dispose();
			
			GameServer gs = new GameServer();
			gs.beginServer();
		}
		
		// Join a server
		if (myJoinServer.isClicked()) {
			myDraw.frame.setVisible(false);
			myDraw.frame.dispose();
			
			Client c = new Client();
			c.connect();
		}
		
		// Render all other elements too
		
		myDraw.show(50);
	}
	
	// The loop
	public void loop() throws ClassNotFoundException, IOException {
		myDraw.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		while (true) {
			render();
		}
	}
	
	@Override
	public void run() {
		try {
			loop();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Main function of the entire BoatGame
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Menu menu = new Menu();
		
		menu.loop();
	}
}
