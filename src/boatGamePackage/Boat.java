package boatGamePackage;

import java.util.ArrayList;

import edu.princeton.cs.introcs.Draw;

public class Boat extends Sprite {
	private int myNumGuns;
	private int myNumCrew;
	protected int myHealth;
	private int myPID;
	private double myAccel;
	
	private int reloadRightProgress = 0; // 0 is ready to shoot
	private int reloadLeftProgress = 0;
	private final int reloadTime = 5;
	
	private ArrayList<Cannon> myRightGuns = new ArrayList<Cannon>();
	private ArrayList<Cannon> myLeftGuns = new ArrayList<Cannon>();
	
	private boolean keyUp = false;
	private boolean keyDown = false;
	private boolean keyLeft = false;
	private boolean keyRight = false;
	private boolean keyRightFire = false;
	private boolean keyLeftFire = false;

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
	
	// Add the guns to the ArrayLists
	public void initGuns() {
		
		for (int i = 0; i < myNumGuns; i++) {
			Cannon cannon = new Cannon(myDraw, "tank0.png", -90, this);
			
			myRightGuns.add(cannon);
		}
		
		for (int i = 0; i < myNumGuns; i++) {
			Cannon cannon = new Cannon(myDraw, "tank0.png", 90, this);
			
			myLeftGuns.add(cannon);
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
		keyUp = getPress(38, 87); // Up Arrow and W
   		keyDown = getPress(40, 83); // Down Arrow and S
   		keyLeft = getPress(37, 65); // Left Arrow and A
   		keyRight = getPress(39, 68); // Right Array and D
   		keyRightFire = getPress(77, 86); // M and V
   		keyLeftFire = getPress(78, 67); // N and C
	}
	
	// Take in key input booleans and change tank angle accordingly
	public int changeAngle(boolean right, boolean left) {
		if (left == true && myAngle >= 360) myAngle = 2;
    	if (left == true) myAngle += 2;
    	if (right == true && myAngle <= 2) myAngle = 360;
    	if (right == true) myAngle -= 2;
    	
    	return myAngle;
	}
	
	// Move the boat based on keyPresses
	public void move() {
		
		// Visualize and update the guns
		for (Cannon c : myRightGuns) {
			c.updateSelf();
			c.visualize();
		}
		
		for (Cannon c : myLeftGuns) {
			c.updateSelf();
			c.visualize();
		}
		
		// Get the key inputs
		getKeyInputs();
		
		// Update angle
    	myAngle = changeAngle(keyRight, keyLeft);
    	
    	// Forward Driving
    	if (keyUp == true) {
    		mySpeed += myAccel;
    	}
    	
    	// Backward Driving: boats don't go backwards
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
	
	// Fire the guns on the right side
	public ArrayList<Sprite> fireRightGuns(ArrayList<Sprite> sprites) {
		// Takes an ArrayList of CannonBalls and adds as many CannonBalls as guns
		// Returns the ArrayList with the new CannonBalls
		if (reloadRightProgress == 0) {
			for (Cannon c : myRightGuns) {
				sprites.add(c.fire());
				reloadRightProgress = reloadTime;
			}
		}
		
		return sprites;
	}
	
	// Fire the guns on the left side
	public ArrayList<Sprite> fireLeftGuns(ArrayList<Sprite> sprites) {
		// Takes an ArrayList of CannonBalls and adds as many CannonBalls as guns
		// Returns the ArrayList with the new CannonBalls
		
		if (reloadLeftProgress == 0) {
			for (Cannon c : myLeftGuns) {
				sprites.add(c.fire());
				reloadLeftProgress = reloadTime;
			}
		}
		
		return sprites;
	}
	
	// Check if the right fire button is pressed
	public boolean shouldFireRight() {
		boolean shouldFire = false;
		
		if (keyRightFire) {
			shouldFire = true;
			System.out.println("Right fire true");
		}
		
		return shouldFire;
	}
	
	// Check if the left fire button is pressed
	public boolean shouldFireLeft() {
		boolean shouldFire = false;
		
		if (keyLeftFire) {
			shouldFire = true;
			System.out.println("Left fire true");
		}
		
		return shouldFire;
	}
	
	// Update the boat
	public void updateSelf() {
		
		if (myHealth == 0) die();
		if (myNumCrew == 0) mySpeed = 0;
		
		if (reloadRightProgress > 0) {
			reloadRightProgress--;
			System.out.println("Reload progress right: " + reloadRightProgress);
		}
		if (reloadLeftProgress > 0) {
			reloadLeftProgress--;
			System.out.println("Reload progress left: " + reloadLeftProgress);
		}
		
		updatePosition();
		visualize();
		move();
	}
		
	
}