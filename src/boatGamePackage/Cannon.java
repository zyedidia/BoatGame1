package boatGamePackage;

import edu.princeton.cs.introcs.Draw;

public class Cannon extends Sprite {

	private Boat myBoat;
	private int myOffSet;
	private double myRelativeX;
	private double myRelativeY;
	private boolean myHasBoat;
	
	// Constructors \\
	public Cannon(Draw draw, int offSet, double relativeX, double relativeY, Boat boat) {
		super(draw, "Resources/tank0.png", boat.mySpeed, boat.myMaxSpeed, 
				(int) (boat.myAngle + offSet), boat.myX, boat.myY, 0.05, 0.05);
		
		myRelativeX = relativeX;
		myRelativeY = relativeY;
		
		myOffSet = offSet;
		myBoat = boat;
		
		myHasBoat = true;
	}
	
	public Cannon(Draw draw, Fort fort) {
		super(draw, "Resources/tank0.png", 0, fort.myX, fort.myY, 0.05, 0.05);
	}
	
	// Calculate the position of the cannon in real space based on the relative coordinates
	public void calcPosition() {
		double boatAngleInRadians = 3 * Math.PI / 2 + myBoat.myAngle * Math.PI / 180.;
		myY = myBoat.myY + myRelativeX * Math.sin(Math.PI/2 - boatAngleInRadians)
					     - myRelativeY * Math.cos(Math.PI/2 - boatAngleInRadians);
		myX = myBoat.myX - myRelativeX * Math.cos(Math.PI/2 - boatAngleInRadians)
						 - myRelativeY * Math.sin(Math.PI/2 - boatAngleInRadians);
	}
	
	
	
	// Put the cannon at the correct coordinates
	public void updateSelf() {
		if (myHasBoat) {
			calcPosition();
			myAngle = myBoat.myAngle + myOffSet;
		}
		updatePosition();
		visualize();
	}
	
	// Return a new CannonBall
	public CannonBall fire() {
		double angleInRadians = (myAngle + 90) * Math.PI / 180.;
		
		CannonBall cb = new CannonBall(myDraw, myX + (myWidth + 0.065) * 2 * Math.cos(angleInRadians), 
				myY + (myHeight + 0.065) * 2 * Math.sin(angleInRadians), 
				0.05 * 40/GameBattle.FPS, 0.05 * 40/GameBattle.FPS, (int) myAngle);
				
		return cb;
	}
	
	// Alternate fire method to fire at a specified angle
	public CannonBall fire(double angleInRadians) {
		CannonBall cb = new CannonBall(myDraw, myX + (myWidth + 0.065) * 2 * Math.cos(angleInRadians), 
				myY + (myHeight + 0.065) * 2 * Math.sin(angleInRadians), 
				0.05 * 40/GameBattle.FPS, 0.05 * 40/GameBattle.FPS, (int) myAngle);
				
		return cb;
	}
	
	// Make some smoke
	public Smoke fireSmoke() {
		double angleInRadians = (myAngle + 90) * Math.PI / 180.;
		
		Smoke smoke = new Smoke(myDraw, "ExplosionAtlasFolder", 0, myX + myWidth * 2 * Math.cos(angleInRadians), 
				myY + myHeight * 2 * Math.sin(angleInRadians));
		
		return smoke;
	}
}
