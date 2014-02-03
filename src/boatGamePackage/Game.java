package boatGamePackage;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import edu.princeton.cs.introcs.Draw;

public class Game {

	public static ArrayList<Sprite> sprites;
	public static double zoom = 1.0;
	private Draw draw;
	public static ArrayList<Boat> myBoats;
	public static double FPS = 1;
	public static double targetFPS = 60;

	public Game() {
		draw = new Draw("Boat Game");

		draw.setCanvasSize(768, 768);

		// Close the window when the exit button is pressed
		draw.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		draw.frame.setJMenuBar(new JMenuBar());

		// Set the coordinate system
		draw.setXscale(-zoom, zoom);
		draw.setYscale(-zoom, zoom);
	}

	public void adjustZoom(ArrayList<Boat> boats) {
		for (Boat b : myBoats) {
			if (b.myZoomOut) {
				zoom += 0.015;
				System.out.println("Zoom Out");
				draw.setXscale(-zoom, zoom);
				draw.setYscale(-zoom, zoom);
			}
			else if (zoom >= 1.0) {	
				zoom -= 0.005;
				System.out.println("Zoom In");
				draw.setXscale(-zoom, zoom);
				draw.setYscale(-zoom, zoom);
			}
		}
	}

	public void init(int numPlayerBoats, int numAiBoats) {
		// Contains all sprites to be updated on loop
		sprites = new ArrayList<Sprite>();
		myBoats = new ArrayList<Boat>();

		zoom = 1.0;
		zoom = 1.0;

		int i = 0;

		// Create Player Boats
		for (i = 0; i < numPlayerBoats; i++) {
			Boat boat = new PlayerBoat(draw, i);
			myBoats.add(boat);
			sprites.add(boat);

		}

		// Create AI Boats
		for (; i < numAiBoats + numPlayerBoats; i++) {
			Boat boat = new AIBoat(draw, i);
			myBoats.add(boat);
			sprites.add(boat);
		}

	}

	public void loop() throws ClassNotFoundException {
		init(1, 1);

		FPS = 1;

		double oldTime = System.currentTimeMillis();
		double beginTime = System.currentTimeMillis();
		double changeTime = 1;


		while (true) {
			changeTime = System.currentTimeMillis() - oldTime;


			//System.out.println(changeTime + " since last update");

			if (changeTime > 0) {
				FPS = 1 / changeTime * 1000;

				oldTime = System.currentTimeMillis();
			}

			// Draw the blue background
			//			draw.setPenColor(new Color(25, 25, 255));
			//			draw.filledSquare(0.0, 0.0, 20);
			draw.picture(0, 0, "resources/Ocean.png", 10, 10);

			adjustZoom(myBoats);

			updateSprites();

			// FPS counter
			draw.setPenColor(Color.GREEN);
			draw.text(-zoom, zoom, "FPS: " + Integer.toString((int) FPS));

			if (draw.isKeyPressed(10)) { // Enter key (restart game button
				loop();
			}

			if (draw.isKeyPressed(79)) { // o key (pause button)
				onPause();
			}


			draw.show(0);

			while((System.currentTimeMillis() - beginTime) % 16 != 0) {

			}
		}		
	}

	// When the game is paused
	public void onPause() throws ClassNotFoundException {
		Button paused = new Button(draw, "Unpause Game", 0, 0, Color.RED);
		Button options = new Button(draw, "Options (Will End Game)", 0, -0.125, Color.RED);
		Button quit = new Button(draw, "Quit", 0, -0.250, Color.RED);
		while (true) {
			if (options.isClicked()) {
				draw.frame.setVisible(false);
				draw.frame.dispose();
				OptionsMenu om = new OptionsMenu();
				om.loop();
			}
			else if (quit.isClicked()) {
				System.out.println("Closing Program: quit button clicked");
				System.exit(0);
			}
			else if (paused.isClicked()) {
				System.out.println("Pause Clicked");
				break;
			}
			paused.render();
			options.render();
			quit.render();
			draw.show();

			if (draw.isKeyPressed(80)) {
				break;
			}
		}
	}

	public void updateSprites() {
		try {
			for (int i = 0; i < sprites.size(); i++) {
				Sprite s = sprites.get(i);
				s.updateSelf();
			}
		} catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Deleted object requested. Ignoring.");
		}
	}
}
