package gamePackage;

import edu.princeton.cs.introcs.Draw;

public class Cannon extends Sprite {

	public Cannon(Draw draw, String fileName, double x, double y, double speed, int angle, Boat boat) {
		super(draw, fileName, speed, boat.myMaxSpeed, angle, x, y, 0.05, 0.1);
	}
}
