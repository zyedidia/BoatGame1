package boatGamePackage;

import java.awt.event.KeyEvent;

import edu.princeton.cs.introcs.Draw;

public class PlayerBoat extends Boat {
	public boolean myReceivesKeyInputs = true;
	
	public PlayerBoat(Draw draw, int pID) {
		super(draw, pID);
	}

	public PlayerBoat(Draw draw, int numGuns, int numCrew, int health, int pID, 
			double x, double y, int angle, double accel) {
		super(draw, numGuns, numCrew, health, x, y, angle, accel, pID);
	}

	public void getKeyInputs() {
		//Initialize keyPresses based on player
		myForward = getPress(KeyEvent.VK_UP, KeyEvent.VK_W); // Up Arrow and W
		myBack = getPress(KeyEvent.VK_DOWN, KeyEvent.VK_S); // Down Arrow and S
		myLeft = getPress(KeyEvent.VK_LEFT, KeyEvent.VK_A); // Left Arrow and A
		myRight = getPress(KeyEvent.VK_RIGHT, KeyEvent.VK_D); // Right Array and D
		myRightFire = getPress(KeyEvent.VK_M, KeyEvent.VK_Z); // M and Z
		myLeftFire = getPress(KeyEvent.VK_N, KeyEvent.VK_SHIFT); // N and Shift
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

		if (myRightFire) {
			shouldFire = true;
			System.out.println("Right fire true");
		}

		return shouldFire;
	}

	// Check if the left fire button is pressed
	public boolean shouldFireLeft() {
		boolean shouldFire = false;

		if (myLeftFire) {
			shouldFire = true;
			System.out.println("Left fire true");
		}

		return shouldFire;
	}
	
	public void updateSelf(){
		if (myReceivesKeyInputs) {
			getKeyInputs();
		}
		super.updateSelf();
	}
}
