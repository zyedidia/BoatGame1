package boatGamePackage;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import edu.princeton.cs.introcs.Draw;

public class Game {

	public static ArrayList<Sprite> sprites;
	public static double zoomX = 1.0;
	public static double zoomY = 1.0;
	private Draw draw;
	
	public Game() {
		draw = new Draw();
		
		draw.setCanvasSize(768, 768);

		// Close the window when the exit button is pressed
		draw.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		draw.frame.setJMenuBar(new JMenuBar());

		// Set the coordinate system
		draw.setXscale(-zoomX, zoomX);
		draw.setYscale(-zoomY, zoomY);
	}
	
	public void adjustZoom(ArrayList<Boat> boats) {
		
	}
	
	public void init(int numPlayerBoats, int numAiBoats) {
		//Contains all sprites to be updated on loop
		sprites = new ArrayList<Sprite>();

		// Initialize the boats
		Boat boat = new PlayerBoat(draw, 0, 0.75, 0.75, 180);
		Boat boat1 = new PlayerBoat(draw, 1, -0.75, -0.75, 0);
		
		sprites.add(boat);
		sprites.add(boat1);
		
		ArrayList<Boat> boats = new ArrayList<Boat>();
		
		boats.add(boat);
		boats.add(boat1);
		
		/*for (int i = 0; i < numAiBoats; i++) {
			// Create AI boats here
		}*/
	}
	
	public void loop() {
		init(0, 0);
		
		int frameCount = 0;
		int framesPerSecond = 0;
		
		long oldTime = System.currentTimeMillis();
		long changeTime = 0;
		
		int seconds = 0;
		
		while (true) {
			frameCount++;
			
			if (((Boat) sprites.get(0)).myZoomOut) {
				zoomX += 0.01;
				zoomY += 0.01;
				System.out.println("Zoom");
				draw.setXscale(-zoomX, zoomX);
				draw.setYscale(-zoomY, zoomY);
			} else if (!((Boat) sprites.get(0)).myZoomOut && zoomX > 1.0 && zoomY > 1.0) {
				zoomX -= 0.005;
				zoomY -= 0.005;
				System.out.println("Unzoom");
				draw.setXscale(-zoomX, zoomX);
				draw.setYscale(-zoomY, zoomY);
			}
			
			changeTime = System.currentTimeMillis() - oldTime;
			
			if (changeTime >= 1000) {
				seconds++;
				
				framesPerSecond = frameCount;
				frameCount = 0;
				System.out.println(seconds);
				oldTime = System.currentTimeMillis();
			}
			
			// Draw the blue background
			draw.setPenColor(new Color(25, 25, 255));
			draw.filledSquare(0.0, 0.0, 20);
			
			// FPS counter
			draw.setPenColor(Color.GREEN);
			draw.text(-1, 1.05, "FPS: " + Integer.toString(framesPerSecond));
			
			if (draw.isKeyPressed(10)) {
				loop();
			}
			
			try {
				for (int i = 0; i < sprites.size(); i++) {
					Sprite s = sprites.get(i);
					s.updateSelf();
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
