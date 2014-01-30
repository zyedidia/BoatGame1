package boatGamePackage;

import edu.princeton.cs.introcs.Draw;

public class Cannon extends Sprite {

	private Boat myBoat;
	private int myOffSet;
	private double myRelativeX;
	private double myRelativeY;
	
	// Contrsuctor \\
	public Cannon(Draw draw, int offSet, double relativeX, double relativeY, Boat boat) {
		super(draw, "Resources/tank0.png", boat.mySpeed, boat.myMaxSpeed, boat.myAngle + offSet, boat.myX, boat.myY, 0.05, 0.05);
		
		myRelativeX = relativeX;
		myRelativeY = relativeY;
		
		myOffSet = offSet;
		myBoat = boat;
	}
	
	public void calcPosition() {
		double boatAngleInRadians = 3 * Math.PI / 2 + myBoat.myAngle * Math.PI / 180.;
		myY = myBoat.myY + myRelativeX * Math.sin(Math.PI/2 - boatAngleInRadians)
					     - myRelativeY * Math.cos(Math.PI/2 - boatAngleInRadians);
		myX = myBoat.myX - myRelativeX * Math.cos(Math.PI/2 - boatAngleInRadians)
						 - myRelativeY * Math.sin(Math.PI/2 - boatAngleInRadians);
	}
	
	
	
	// Put the cannon at the correct coordinates
	public void updateSelf() {
		calcPosition();
		myAngle = myBoat.myAngle + myOffSet;
	}
	
	// Return a new CannonBall
	public CannonBall fire() {
		double angleInRadians = (myAngle + 90) * Math.PI / 180.;
		
		CannonBall cb = new CannonBall(myDraw, myX + (myWidth + 0.065) * 2 * Math.cos(angleInRadians), 
				myY + (myHeight + 0.065) * 2 * Math.sin(angleInRadians), 0.05, 0.05, myAngle);
				
		return cb;
	}
	
	public Smoke fireSmoke() {
		double angleInRadians = (myAngle + 90) * Math.PI / 180.;
		
		Smoke smoke = new Smoke(myDraw, "ExplosionAtlasFolder", 0, myX + myWidth * 2 * Math.cos(angleInRadians), 
				myY + myHeight * 2 * Math.sin(angleInRadians));
		
		return smoke;
	}
}
