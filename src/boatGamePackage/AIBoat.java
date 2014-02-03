package boatGamePackage;

import edu.princeton.cs.introcs.Draw;

public class AIBoat extends Boat {
		
	public AIBoat(Draw draw, int numGuns, int numCrew, int health, double x,
			double y, int angle, double accel, int PID) {
		super(draw, numGuns, numCrew, health, x, y, angle, accel, PID);
	}
	
	public AIBoat(Draw draw, int PID) {
		super(draw, PID);
	}
	
	public void computeMove(Boat targetBoat) {
		if (!targetBoat.isDead) {
		
			boolean faceRight = true;
			boolean faceLeft = true;
			
			myForward = true;
			
			//Point directly at target with offset depending on if its right side or left side
			double directRight = Math.atan2(targetBoat.myY - myY, targetBoat.myX - myX);
			directRight += Math.PI / 2;
			
			double directLeft = Math.atan2(targetBoat.myY - myY, targetBoat.myX - myX);
			directLeft -= Math.PI / 2;
			if (myRightGuns.size() - myLeftGuns.size() >= 2) {
				faceRight = true;
			} else if (myRightGuns.size() - myLeftGuns.size() >= 2) {
				faceLeft = true;
			} else {
				faceLeft = true;
				faceRight = true;
			}
			
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
	
	public boolean computeAngle(double formula, boolean goRight, boolean goLeft) {
		boolean finishedComputing = false;
		double theta = formula;
		if (theta < 0) theta += 2*Math.PI;
		theta *= 180 / Math.PI;
		if (myAngle > (int) theta && goLeft) {
			myAngle -= 1;
		} 
		if (myAngle < (int) theta && goRight) {
			System.out.println(myAngle + " < " + (int) theta);
			myAngle += 1;
		}
		if (myAngle == (int) theta) {
			System.out.println("Done: " + myAngle + " = " + (int) theta);
			finishedComputing = true;
		}
		
		return finishedComputing;
	}
	
	public void updateSelf(Boat targetBoat) {
		computeMove(targetBoat);
		super.updateSelf();
	}

}
