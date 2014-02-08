package boatGamePackage;

import edu.princeton.cs.introcs.Draw;

public class AIBoat extends Boat {
		
	// Constructors \\
	public AIBoat(Draw draw, int numGuns, int numCrew, int health, double x,
			double y, int angle, double accel, int PID) {
		super(draw, numGuns, numCrew, health, x, y, angle, accel, PID);
	}
	
	public AIBoat(Draw draw, int PID) {
		super(draw, PID);
	}
	
	public AIBoat(Draw draw, int PID, String filename) {
		super(draw, PID, filename);
	}
	
	// Compute what the computer should do this frame
	public void computeMove(Boat targetBoat) {
		// Only do anything if the target is still alive
		if (!targetBoat.isDead) {
		
			boolean faceRight = true;
			boolean faceLeft = true;
			
			// Always drive forward (makes it harder to hit)
			myForward = true;
			
			//Point directly at target with offset depending on if its right side or left side
			double directRight = Math.atan2(targetBoat.myY - myY, targetBoat.myX - myX);
			directRight += Math.PI / 2;
			
			double directLeft = Math.atan2(targetBoat.myY - myY, targetBoat.myX - myX);
			directLeft -= Math.PI / 2;
			
			// Use the other side if the current side runs out of cannons
			if (myRightGuns.size() - myLeftGuns.size() >= 2) {
				faceRight = true;
			} else if (myRightGuns.size() - myLeftGuns.size() >= 2) {
				faceLeft = true;
			} else {
				faceLeft = true;
				faceRight = true;
			}
			
			// Compute the angle (turn the boat to face the given angle)
			if (computeAngle(directRight + 1.57, faceRight, faceLeft)) {
				System.out.println("DirectRight");
				myRightFire = true;
			}
			if (computeAngle(directLeft - 1.57, faceRight, faceLeft)) {
				System.out.println("DirectLeft");
				myLeftFire = true;
			}
		} else {
			myRightFire = false;
			myLeftFire = false;
		}
	}
	
	// Turn the boat to face a given angle
	public boolean computeAngle(double formula, boolean goRight, boolean goLeft) {
		boolean finishedComputing = false;
		double theta = formula;
		if (theta < 0) theta += 2*Math.PI;
		theta *= 180 / Math.PI;
		
		// Rotate left
		if (myAngle > (int) theta && goLeft) {
			myAngle -= 1;
		} 
		
		// Rotate right
		if (myAngle < (int) theta && goRight) {
			System.out.println(myAngle + " < " + (int) theta);
			myAngle += 1;
		}
		
		// Finished turning
		if (myAngle == (int) theta) {
			System.out.println("Done: " + myAngle + " = " + (int) theta);
			finishedComputing = true;
		}
		
		return finishedComputing;
	}
	
	// Update the boat
	public void updateSelf() {
		System.out.println("Instance of");
		Boat targetBoat  = Game.myBoats.get(0);
		
		if (myLeftFire) {
			fireLeftGuns(); 
		}
		if (myRightFire) {
			fireRightGuns();
		}
		
		computeMove(targetBoat);
		super.updateSelf();
	}

}
