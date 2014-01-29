package boatGamePackage;

import edu.princeton.cs.introcs.Draw;

public class Cannon extends Sprite {

	private Boat myBoat;
	private int myOffSet;
	
	// Contrsuctor \\
	public Cannon(Draw draw, int offSet, Boat boat) {
		super(draw, "Resources/tank0.png", boat.mySpeed, boat.myMaxSpeed, boat.myAngle + offSet, boat.myX, boat.myY, 0.05, 0.05);
		
		myOffSet = offSet;
		myBoat = boat;
	}
	
	// Put the cannon at the correct coordinates
	public void updateSelf() {
		myX = myBoat.myX;
		myY = myBoat.myY;
		myAngle = myBoat.myAngle + myOffSet;
	}
	
	// Return a new CannonBall
	public CannonBall fire() {
		double angleInRadians = (myAngle + 90) * Math.PI / 180.;
		
		CannonBall cb = new CannonBall(myDraw, myX + (myWidth + 0.075) * 2 * Math.cos(angleInRadians), 
				myY + (myHeight + 0.075) * 2 * Math.sin(angleInRadians), 0.05, 0.05, myAngle);
				
		return cb;
	}
	
	public Smoke fireSmoke() {
		double angleInRadians = (myAngle + 90) * Math.PI / 180.;
		
		Smoke smoke = new Smoke(myDraw, "ExplosionAtlasFolder", 0, myX + myWidth * 2 * Math.cos(angleInRadians), 
				myY + myHeight * 2 * Math.sin(angleInRadians));
		
		return smoke;
	}
}
