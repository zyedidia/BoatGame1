package boatGamePackage;

import edu.princeton.cs.introcs.Draw;


public class CannonBall extends Sprite {
	
	// Constructor \\
	public CannonBall(Draw draw, String fileName, double x, double y, double speed, double maxSpeed, int angle) {
		
		super(draw, fileName, speed, maxSpeed, angle, x, y, 0.05, 0.05);
	}
	
}
