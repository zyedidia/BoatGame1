package boatGamePackage;

import edu.princeton.cs.introcs.Draw;

public class PlayerBoat extends Boat {

	private int myPID;

	public PlayerBoat(Draw draw, int pID, double x, double y, int angle) {
		super(draw, x, y, angle);
		myPID = pID;
	}

	public PlayerBoat(Draw draw, int numGuns, int numCrew, int health, int pID, double x, double y, int angle, double accel) {
		super(draw, numGuns, numCrew, health, x, y, angle, accel);
		myPID = pID;
	}

	public void getKeyInputs() {
		//Initialize keyPresses based on player
		forward = getPress(38, 87); // Up Arrow and W
		back = getPress(40, 83); // Down Arrow and S
		left = getPress(37, 65); // Left Arrow and A
		right = getPress(39, 68); // Right Array and D
		rightFire = getPress(77, 86); // M and V
		leftFire = getPress(78, 67); // N and C
	}

	//Parameters: keyPress for player1 and keyPress for player2 Returns: whether the correct 
	//keyPress based on current tank player has been pressed
	public boolean getPress(int firstPlayer, int alternate) {
		boolean keyPress = false;
		if (myPID == 0) {
			keyPress = myDraw.isKeyPressed(firstPlayer);
		} else {
			keyPress = myDraw.isKeyPressed(alternate);
		}

		return keyPress;
	}

	// Check if the right fire button is pressed
	public boolean shouldFireRight() {
		boolean shouldFire = false;

		if (rightFire) {
			shouldFire = true;
			System.out.println("Right fire true");
		}

		return shouldFire;
	}

	// Check if the left fire button is pressed
	public boolean shouldFireLeft() {
		boolean shouldFire = false;

		if (leftFire) {
			shouldFire = true;
			System.out.println("Left fire true");
		}

		return shouldFire;
	}
	
	public void updateSelf(){
		getKeyInputs();
		super.updateSelf();
	}
}
