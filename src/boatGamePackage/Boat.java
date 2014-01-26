package boatGamePackage;

import java.awt.Color;
import java.util.ArrayList;

import edu.princeton.cs.introcs.Draw;

public class Boat extends Sprite {
	private int myNumGuns;
	private int myNumCrew;
	private int myHealth;
	private int myPID;
	private double myAccel;
	
	private ArrayList<Cannon> myGuns = new ArrayList<Cannon>();
	
	private boolean keyUp = false;
	private boolean keyDown = false;
	private boolean keyLeft = false;
	private boolean keyRight = false;

	// Constructors \\
	public Boat(Draw draw, int numGuns, int numCrew, int health, int pID, double x, double y, int angle, double accel) {
		super(draw, "boat.png", angle, x, y, 0.2, 0.5);
		myMaxSpeed = 0.0075;
		myNumCrew = numCrew;
		myNumGuns = numGuns;
		myHealth = health;
		myPID = pID;
		myAccel = accel;
		initGuns();
	}
	
	public Boat(Draw draw, int pID, double x, double y, int angle) {
		super(draw, "boat.png", angle, x, y, 0.2, 0.5);
		myMaxSpeed = 0.0075;
		myNumCrew = 100;
		myNumGuns = 5; // Guns per side
		myHealth = 100;
		myPID = pID;
		myAccel = 0.001;
		initGuns();
	}
	
	public void initGuns() {
		
		for (int i = 0; i < myNumGuns; i++) {
			Cannon cannon = new Cannon(myDraw, "tank0.png", myX, myY, mySpeed, myAngle+90, this);
			
			myGuns.add(cannon);
		}
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
	
	public void getKeyInputs() {
		//Initialize keyPresses based on player
		keyUp = getPress(38, 87);
   		keyDown = getPress(40, 83);
   		keyLeft = getPress(37, 65);
   		keyRight = getPress(39, 68);
	}
	
	//Take in key input booleans and change tank angle accordingly
	public int changeAngle(boolean right, boolean left) {
		if (left == true && myAngle >= 360) myAngle = 2;
    	if (left == true) myAngle += 2;
    	if (right == true && myAngle <= 2) myAngle = 360;
    	if (right == true) myAngle -= 2;
    	
    	return myAngle;
	}
	
	// Move the boat based on keyPresses
	public void move() {
		for (Cannon c : myGuns) {
			c.myAngle = myAngle + 90;
			c.myX = myX;
			c.myY = myY;
			c.visualize();
		}
		
		getKeyInputs();
		
		//Update angle
    	myAngle = changeAngle(keyRight, keyLeft);
    	
    	//Forward Driving
    	if (keyUp == true) {
    		mySpeed += myAccel;
    	}
    	
    	//Backward Driving: boats don't go backwards
    	if (keyDown == true) {
    		//mySpeed -= myAccel;
    		
    	}
    	
    	//If no keypress, slow down the boat
    	if (keyUp == false && keyDown == false) {
    		if (mySpeed > 0) {
    			mySpeed -= mySpeed/60;
    		} else {
    			mySpeed = 0;
    		}
    	}
	}
	
	// Update the boat
	public void updateSelf() {
		
		
		if (myHealth == 0) die();
		if (myNumCrew == 0) mySpeed = 0;
		
		updatePosition();
		visualize();
		move();
	}
	
	// Local main function used for testing and debugging
	public static void main(String[] args) {
		Draw draw = new Draw();
		
		Boat boat = new Boat(draw, 0, 0, 0, 0);
		
		draw.setXscale(-1.0, 1.0);
		draw.setYscale(-1.0, 1.0);
				
		while (true) {
			// Draw the blue background
			draw.setPenColor(Color.blue);
			draw.filledSquare(0.0, 0.0, 1.5);
			
			// Update the boat
			boat.updateSelf();
			
			draw.show(20);
		}
	}
}