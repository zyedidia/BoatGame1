package boatGamePackage;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;

import edu.princeton.cs.introcs.Draw;

public class Game {
	public static void main(String[] args) {
		Draw draw = new Draw();
		
		draw.setCanvasSize(768, 768);
		
		// Close the window when the exit button is pressed
		draw.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Initialize the boats
		Boat boat = new Boat(draw, 0, 0, 0, 0);
		Boat boat1 = new Boat(draw, 1, 0, 0, 0);
		
		// Set the coordinate system
		draw.setXscale(-1.0, 1.0);
		draw.setYscale(-1.0, 1.0);
		
		ArrayList<CannonBall> cbs = new ArrayList<CannonBall>();
				
		while (true) {
			
			// Draw the blue background
			draw.setPenColor(Color.blue);
			draw.filledSquare(0.0, 0.0, 1.5);
			
			
			// Update the boat
			boat.updateSelf();
			boat1.updateSelf();
			
			if (boat.shouldFireRight()) {
				cbs = boat.fireRightGuns(cbs);
			}
			if (boat.shouldFireLeft()) {
				cbs = boat.fireLeftGuns(cbs);
			}
			
			if (boat1.shouldFireRight()) {
				cbs = boat1.fireRightGuns(cbs);
			}
			if (boat1.shouldFireLeft()) {
				cbs = boat1.fireLeftGuns(cbs);
			}
			
			//cbs = boat.fire(cbs);
			
			for (CannonBall c : cbs) {
				c.updateSelf();
				//c.didCollideWithBoat(boat);
				//c.didCollideWithBoat(boat1);
			}
			
			draw.show(20);
		}
	}
}
