package boatGamePackage;

import edu.princeton.cs.introcs.Draw;

public class Cannon extends Sprite {

	private Boat myBoat;
	private int myOffSet;
	
	// Contrsuctor \\
	public Cannon(Draw draw, String fileName, int offSet, Boat boat) {
		super(draw, fileName, boat.mySpeed, boat.myMaxSpeed, boat.myAngle + offSet, boat.myX, boat.myY, 0.05, 0.1);
		
		myOffSet = offSet;
		myBoat = boat;
	}
	
	// Put the cannon at the correct coordinates
	public void update() {
		myX = myBoat.myX;
		myY = myBoat.myY;
		myAngle = myBoat.myAngle + myOffSet;
	}
	
	// Return a new CannonBall
	public CannonBall fire() {
		
		CannonBall cb = new CannonBall(myDraw, "BlackDot.png", myX, 
				myY, 0.05, 0.05, myAngle);
				
		return cb;
	}
}
