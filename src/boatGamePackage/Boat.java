package boatGamePackage;

import java.awt.Color;
import java.util.ArrayList;

import edu.princeton.cs.introcs.Draw;

public class Boat extends Sprite {
	protected boolean myForward = false;
	protected boolean myBack = false;
	protected boolean myLeft = false;
	protected boolean myRight = false;
	protected boolean myRightFire = false;
	protected boolean myLeftFire = false;

	protected int myNumGuns = 5;	// Guns per side
	protected int myNumCrew = 100;	//Crew
	protected int myHealth = 100;
	protected double myAccel = 0.0005;
	
	private final double myCannonDeadZone = 0.15;

	private int myReloadRightProgress = 0; // 0 is ready to shoot
	private int myReloadLeftProgress = 0;
	private final int myReloadTime = myNumCrew / myNumGuns * 4;

	protected ArrayList<Cannon> myRightGuns = new ArrayList<Cannon>();
	protected ArrayList<Cannon> myLeftGuns = new ArrayList<Cannon>();

	// Constructors \\
	public Boat(Draw draw, int numGuns, int numCrew, int health, double x, double y, int angle, double accel) {
		super(draw, "Resources/boat.png", angle, x, y, 0.2, 0.5);
		myMaxSpeed = 0.0075;
		myNumCrew = numCrew;
		myNumGuns = numGuns;
		myHealth = health;
		myAccel = accel;
		initGuns();
	}

	public Boat(Draw draw, double x, double y, int angle) {
		super(draw, "Resources/boat.png", angle, x, y, 0.2, 0.5);
		myMaxSpeed = 0.0075;
		initGuns();
		
	}

	// Add the guns to the ArrayLists
	public void initGuns() {

		for (int i = 0; i < myNumGuns; i++) {
			double deltaY = (myHeight - 2 * myCannonDeadZone) / 2;
			if (myNumGuns > 1) {
				deltaY = (myHeight - 2 * myCannonDeadZone) / (myNumGuns - 1);
			}
			
			double y = (myHeight - 2 * myCannonDeadZone) / 2 - i * deltaY;
			
			Cannon cannon = new Cannon(myDraw, -90, 0.05, y, this);

			myRightGuns.add(cannon);
		}

		for (int i = 0; i < myNumGuns; i++) {
			double deltaY = (myHeight - 2 * myCannonDeadZone) / 2;
			if (myNumGuns > 1) {
				deltaY = (myHeight - 2 * myCannonDeadZone) / (myNumGuns - 1);
			}
			
			double y = (myHeight - 2 * myCannonDeadZone) / 2 - i * deltaY;
			
			Cannon cannon = new Cannon(myDraw, 90, -0.05, y, this);

			myLeftGuns.add(cannon);
		}
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
		//getKeyInputs();

		// Update angle
		myAngle = changeAngle(myRight, myLeft);
		mySpeed = 0;
		// Forward Driving
		if (myForward == true) {
			mySpeed += myAccel;
		}

		// Backward Driving: boats don't go backwards
		if (myBack == true) {
			//mySpeed -= myAccel;
		}

		//If no keypress, slow down the boat
		if (myForward == false && myBack == false) {
			if (mySpeed > 0) {
				mySpeed -= mySpeed/60;
			} else {
				mySpeed = 0;
			}
		}
		myVx -= myVx/60;
		myVy -= myVy/60;
	}

	// Fire the guns on the right side
	public ArrayList<Sprite> fireRightGuns(ArrayList<Sprite> sprites) {
		// Takes an ArrayList of CannonBalls and adds as many CannonBalls as guns
		// Returns the ArrayList with the new CannonBalls
		if (myReloadRightProgress == 0) {
			for (Cannon c : myRightGuns) {
				sprites.add(c.fire());
				sprites.add(c.fireSmoke());
				myReloadRightProgress = myReloadTime;
			}
		}

		return sprites;
	}

	// Fire the guns on the left side
	public ArrayList<Sprite> fireLeftGuns(ArrayList<Sprite> sprites) {
		// Takes an ArrayList of CannonBalls and adds as many CannonBalls as guns
		// Returns the ArrayList with the new CannonBalls

		if (myReloadLeftProgress == 0) {
			for (Cannon c : myLeftGuns) {
				sprites.add(c.fire());
				sprites.add(c.fireSmoke());
				myReloadLeftProgress = myReloadTime;
			}
		}

		return sprites;
	}

	// Should be overridden
	public boolean shouldFireRight() {
		return false;
	}

	// Should be overridden
	public boolean shouldFireLeft() {
		return false;
	}

	// Update the boat
	public void updateSelf() {

		if (myHealth == 0) die();
		if (myNumCrew == 0) mySpeed = 0;

		if (myReloadRightProgress > 0) {
			myReloadRightProgress--;
			//System.out.println("Reload progress right: " + reloadRightProgress);
		}
		if (myReloadLeftProgress > 0) {
			myReloadLeftProgress--;
			//System.out.println("Reload progress left: " + reloadLeftProgress);
		}
		
		updatePosition();
		
		visualize();
		move();
		
		updateHUD();
	}
	
	public void updateHUD() {
		myDraw.setPenColor(Color.GREEN);
		if (myHealth < 0) {
			myHealth = 0;
		}
		myDraw.filledRectangle(myX, myY + 0.1, myHealth * 0.001, 0.0075);
		
		myDraw.setPenColor(new Color(0, 255, 0, 100));
		Cannon rightCannon = myRightGuns.get(0);
		double cannonAngleInRadians = (rightCannon.myAngle + 90) * Math.PI / 180.;
		myDraw.filledCircle(rightCannon.myX + rightCannon.myWidth * 2 * Math.cos(cannonAngleInRadians), 
				rightCannon.myY + rightCannon.myHeight * 2 * Math.sin(cannonAngleInRadians), 
				myReloadRightProgress * 0.001);
		
		Cannon leftCannon = myLeftGuns.get(0);
		double leftCannonAngleInRadians = (leftCannon.myAngle + 90) * Math.PI / 180.;
		myDraw.filledCircle(leftCannon.myX + leftCannon.myWidth * 2 * Math.cos(leftCannonAngleInRadians), 
				leftCannon.myY + leftCannon.myHeight * 2 * Math.sin(leftCannonAngleInRadians), 
				myReloadLeftProgress * 0.001);
		
	}

	public void updatePosition() {
		// Set speed to maxSpeed if speed is greater than maxSpeed
		if (mySpeed > myMaxSpeed) {
			mySpeed = myMaxSpeed;
		}

		// Convert speed and angle to vx and vy
		myVx += mySpeed * Math.cos((myAngle - 270) * Math.PI / 180);
		myVy += mySpeed * Math.sin((myAngle - 270) * Math.PI / 180);

		if (Math.sqrt(Math.pow(myVx,2) + Math.pow(myVy, 2)) > myMaxSpeed) {
			double myNewVx = myMaxSpeed * Math.cos((myAngle - 270) * Math.PI / 180);
			double myNewVy = myMaxSpeed * Math.sin((myAngle - 270) * Math.PI / 180);
			double weight = 20;
			myVx = (weight * myVx + myNewVx) / (weight + 1);
			myVy = (weight * myVy + myNewVy) / (weight + 1);
		}
		

		// Update the position
		myX += myVx;
		myY += myVy;
	}
}