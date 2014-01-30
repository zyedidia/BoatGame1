package boatGamePackage;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import edu.princeton.cs.introcs.Draw;

public class Game {

	public static ArrayList<Sprite> sprites;
	private Draw draw;
	public Game() {
		draw = new Draw();
		
		draw.setCanvasSize(768, 768);

		// Close the window when the exit button is pressed
		draw.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		draw.frame.setJMenuBar(new JMenuBar());

		// Set the coordinate system
		draw.setXscale(-1.0, 1.0);
		draw.setYscale(-1.0, 1.0);
	}
	
	public void loop() {
		//Contains all sprites to be updated on loop
		sprites = new ArrayList<Sprite>();

		// Initialize the boats
		sprites.add(new PlayerBoat(draw, 0, 0.75, 0.75, 180));
		sprites.add(new PlayerBoat(draw, 1, -0.75, -0.75, 0));

		int frameCount = 0;
		int framesPerSecond = 0;
		
		long oldTime = System.currentTimeMillis();
		
		int seconds = 0;
		
		while (true) {
			frameCount++;
			
			long changeTime = System.currentTimeMillis() - oldTime;
			
			if (changeTime >= 1000) {
				seconds++;
				
				framesPerSecond = frameCount;
				frameCount = 0;
				System.out.println(seconds);
				oldTime = System.currentTimeMillis();
			}
			
			// Draw the blue background
			draw.setPenColor(new Color(25, 25, 255));
			draw.filledSquare(0.0, 0.0, 1.5);
			
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
					if (s instanceof Boat) {
						if (((Boat) s).shouldFireLeft()) {
							sprites = ((Boat) s).fireLeftGuns(sprites); 
						}
						if (((Boat) s).shouldFireRight()) {
							sprites = ((Boat) s).fireRightGuns(sprites);
						}
					}

					if (s instanceof CannonBall) {
						if (((CannonBall) s).didCollideWithBoat((Boat) sprites.get(0))) {
							sprites.add(((CannonBall) s).onHit((Boat) sprites.get(0)));
						}
						if (((CannonBall) s).didCollideWithBoat((Boat) sprites.get(1))) {
							sprites.add(((CannonBall) s).onHit((Boat) sprites.get(1)));
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
