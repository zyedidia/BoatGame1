package boatGamePackage;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import edu.princeton.cs.introcs.Draw;

public class Game {

	public static ArrayList<Sprite> sprites;

	public void loop() {
		Draw draw = new Draw();

		draw.setCanvasSize(768, 768);

		// Close the window when the exit button is pressed
		draw.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		draw.frame.setJMenuBar(new JMenuBar());

		// Set the coordinate system
		draw.setXscale(-1.0, 1.0);
		draw.setYscale(-1.0, 1.0);

		//Contains all sprites to be updated on loop
		sprites = new ArrayList<Sprite>();

		// Initialize the boats
		sprites.add(new Boat(draw, 0, 0, 0, 0));
		sprites.add(new Boat(draw, 1, 0, 0, 0));

		draw.setPenColor(Color.blue);
		while (true) {

			// Draw the blue background
			draw.filledSquare(0.0, 0.0, 1.5);
			try {
				for (int i = 0; i < sprites.size(); i++) {
					Sprite s = sprites.get(i);
					s.updateSelf();
					if(s instanceof CannonBall){
						//c.didCollideWithBoat(boat);
						//c.didCollideWithBoat(boat1);
					}
					sprites.set(i, s);
				}
			} catch(ArrayIndexOutOfBoundsException e){
				System.out.println("Deleted object requested. Ignoring.");
			}
			draw.show(20);
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.loop();
	}

}
