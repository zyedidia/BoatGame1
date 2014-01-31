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
	
	public Game() {
		draw = new Draw();
		
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
		
		// Create Player Boats
		for (int i = 0; i < numPlayerBoats; i++) {
			Boat boat = new PlayerBoat(draw, i);
			myBoats.add(boat);
			sprites.add(boat);
			
		}
		
		// Create AI Boats here
		
	}
	
	public void loop() {
		init(2, 0);
		
		int frameCount = 0;
		int framesPerSecond = 0;
		
		long oldTime = System.currentTimeMillis();
		long changeTime = 0;
		
		int seconds = 0;
		
		while (true) {
			
			System.out.println(myBoats.get(0).myX);
			System.out.println(sprites.size());
			
			frameCount++;
			
			changeTime = System.currentTimeMillis() - oldTime;
			
			if (changeTime >= 1000) {
				seconds++;
				
				framesPerSecond = frameCount;
				frameCount = 0;
				System.out.println(seconds);
				oldTime = System.currentTimeMillis();
			}
			
			adjustZoom(myBoats);
			
			// Draw the blue background
			draw.setPenColor(new Color(25, 25, 255));
			draw.filledSquare(0.0, 0.0, 20);
			
			// FPS counter
			draw.setPenColor(Color.GREEN);
			draw.text(-zoom, zoom, "FPS: " + Integer.toString(framesPerSecond));
			
			if (draw.isKeyPressed(10)) {
				loop();
			}
			
			try {
				for (int i = 0; i < sprites.size(); i++) {
					Sprite s = sprites.get(i);
					s.updateSelf();
					if (s instanceof Boat) {
						if (((Boat) s).shouldFireLeft()) {
							sprites = ((Boat) s).fireLeftGuns(sprites); 
						}
						if (((Boat) s).shouldFireRight()) {
							sprites = ((Boat) s).fireRightGuns(sprites);
						}
					}

					if (s instanceof CannonBall) {
						for (Boat b : myBoats) {
							if (((CannonBall) s).didCollideWithBoat(b)) {
								sprites.add(((CannonBall) s).onHit(b));
							}
							if (((CannonBall) s).didCollideWithBoat(b)) {
								sprites.add(((CannonBall) s).onHit(b));
							}
						}
					}

					if (s instanceof Smoke) {
						//System.out.println(((Smoke) s).iteration);
						if (((Smoke) s).isFinished) {
							sprites.remove(s);
							//System.out.println("Removed");
						}
					}

					//sprites.set(i, s);

				}
			} catch(ArrayIndexOutOfBoundsException e){
				System.out.println("Deleted object requested. Ignoring.");
			}
			
			if (draw.isKeyPressed(79)) {
				while (true) {
					draw.setPenColor(Color.RED);
					draw.text(0, 0, "Game Paused");
					draw.text(0, -0.125, "Options");
					draw.text(0, -0.250, "Quit");					
					draw.show();
					if (draw.isKeyPressed(80)) {
						break;
					}
				}
			}
			
			draw.show(20);
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.loop();
	}

}
