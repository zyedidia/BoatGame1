package boatGamePackage;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import edu.princeton.cs.introcs.Draw;

public class Boat extends Sprite {
	protected boolean myForward = false;
	protected boolean myBack = false;
	protected boolean myLeft = false;
	protected boolean myRight = false;
	protected boolean myRightFire = false;
	protected boolean myLeftFire = false;
	protected boolean myZoomOut = false;

	protected int myNumGuns = 5;	// Guns per side
	protected int myNumCrew = 100;	//Crew
	protected int myHealth = 100;
	protected double myAccel = 0.0005;
	
	protected int myPID;
	
	private final double myCannonDeadZone = 0.15;
	
	private int myReloadRightProgress = 0; // 0 is ready to shoot
	private int myReloadLeftProgress = 0;
	private int myReloadTime = (int) (100 * 1.5 );

	protected ArrayList<Cannon> myRightGuns = new ArrayList<Cannon>();
	protected ArrayList<Cannon> myLeftGuns = new ArrayList<Cannon>();

	// Constructors \\
	public Boat(Draw draw, int numGuns, int numCrew, int health, double x, double y, int angle, double accel, int PID) {
		super(draw, "Resources/boat.png", angle, x, y, 0.2, 0.5);
		init();
		myNumCrew = numCrew;
		myNumGuns = numGuns;
		myHealth = health;
		myAccel = accel;
		myPID = PID;
		readOptions();
		initGuns();
		setStartingPos();
	}

	public Boat(Draw draw, int PID) {
		super(draw, "Resources/boat.png", 0, 0, 0, 0.2, 0.5);
		init();
		myPID = PID;
		readOptions();
		initGuns();
		setStartingPos();
	}
	public Boat(Draw draw, int PID, String filename) {
		super(draw, filename, 0, 0, 0, 0.2, 0.5);
		init();
		myPID = PID;
		readOptions();
		initGuns();
		setStartingPos();
	}
	
	// Initialize all the variables
	public void init() {
		myMaxSpeed = 0.0075;
		myZoomOut = false;
		myNumGuns = 5;	// Guns per side
		myNumCrew = 100;	//Crew
		myHealth = 100;
		myAccel = 0.0005 * 40.0 / Game.FPS;
	}
	
	// Set the starting position and angle of the boat based on the player id
	public void setStartingPos() {
		if (myPID == 0) {
			myX = 0.75; myY = 0.75; myAngle = 180;
		}
		else if (myPID == 1) {
			myX = -0.75; myY = -0.75; myAngle = 0;
		}
		else if (myPID == 2) {
			myX = -0.75; myY = 0.75; myAngle = 180;
		}
		else if (myPID == 3) {
			myX = 0.75; myY = -0.75; myAngle = 0;
		} else {
			System.out.println("Error with Player ID");
			myX = 0; myY = 0; myAngle = 0;
		}
	}
	
	// Read some serialized data
	public void readOptions() {
		FileInputStream fileIn;
		//Read in options from options.ser
		try {
			fileIn = new FileInputStream("options.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
	        double[] values = (double[]) in.readObject();
	        myNumGuns = (int) values[0];
	        in.close();
	        fileIn.close();
	    // Got an error, set all values to defaults
		} catch (IOException e) {
			myNumGuns = 5; // Default amount of cannons
			System.out.println("Could not read from options.ser: " + e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error reading from options.ser: " + e);
		}
	}

	// Add the guns to the ArrayLists
	public void initGuns() {
		
		// Create the guns on the right side of the boat
		for (int i = 0; i < myNumGuns; i++) {
			double deltaY = (myHeight - 2 * myCannonDeadZone) / 2;
			if (myNumGuns > 1) {
				deltaY = (myHeight - 2 * myCannonDeadZone) / (myNumGuns - 1);
			}
			
			double y = (myHeight - 2 * myCannonDeadZone) / 2 - i * deltaY;
			
			Cannon cannon = new Cannon(myDraw, -90, 0.05, y, this);

			myRightGuns.add(cannon);
		}

		// Create the guns on the left side of the boat
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
	public double changeAngle(boolean right, boolean left) {
		if (left == true && myAngle >= 360) myAngle = 2;
		if (left == true) myAngle += (int) 1;
		if (right == true && myAngle <= 2) myAngle = 360;
		if (right == true) myAngle -= (int) 1;

		return (int) myAngle;
	}

	// Move the boat based on keyPresses
	public void move() {
		
		// Visualize and update the guns
		for (Cannon c : myRightGuns) {
			c.updateSelf();
		}

		for (Cannon c : myLeftGuns) {
			c.updateSelf();
		}

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
	public void fireRightGuns() {
		// Takes an ArrayList of CannonBalls and adds as many CannonBalls as guns
		// Returns the ArrayList with the new CannonBalls
		if (myReloadRightProgress == 0) {
			for (Cannon c : myRightGuns) {
				Game.sprites.add(c.fire());
				Game.sprites.add(c.fireSmoke());
				myReloadRightProgress = myReloadTime;
			}
		}
	}

	// Fire the guns on the left side
	public void fireLeftGuns() {
		// Takes an ArrayList of CannonBalls and adds as many CannonBalls as guns
		// Returns the ArrayList with the new CannonBalls

		if (myReloadLeftProgress == 0) {
			for (Cannon c : myLeftGuns) {
				Game.sprites.add(c.fire());
				Game.sprites.add(c.fireSmoke());
				myReloadLeftProgress = myReloadTime;
			}
		}
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
		myAccel = 0.0005 * 40/Game.FPS;

		if (myHealth == 0) die();
		// Boat can't move if there is no crew
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
		
		checkCollideWithIslands();
		
		visualize();
		move();
		
		updateHUD();
		
		if (shouldFireLeft()) {
			fireLeftGuns(); 
		}
		if (shouldFireRight()) {
			fireRightGuns();
		}
	}
	
	public void updateHUD() {
		if (myHealth < 0) {
			myHealth = 0;
		}
		//myDraw.filledRectangle(myX, myY + 0.1, myHealth * 0.001, 0.0075);
		myDraw.picture(myX, myY+0.1, "resources/healthbar.png", myHealth * 0.002, 0.05);
		
		myDraw.setPenColor(new Color(0, 255, 0, 100));
		Cannon rightCannon = myRightGuns.get(0);
		
		// Draws the reload indicators
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

	// Update the position of the boat
	public void updatePosition() {
		// Set speed to maxSpeed if speed is greater than maxSpeed
		if (mySpeed > myMaxSpeed) {
			mySpeed = myMaxSpeed;
		}
		
		// Check if the boat is nearing the edge of the screen
			// If so, the Game will zoom out (adjustZoom() in Game class)
		if (myX > Game.zoom - Game.zoom / 6) {
			myZoomOut = true;
		}
		else if (myX < -Game.zoom + Game.zoom / 6) {
			myZoomOut = true;
		}
		else if (myY > Game.zoom - Game.zoom / 6) {
			myZoomOut = true;
		} 
		else if (myY < -Game.zoom + Game.zoom / 6) {
			myZoomOut = true;
		} else {
			myZoomOut = false;
		}
		
		System.out.println(Game.zoom * 2);
		System.out.println(myX);
		
		if (myX > 2) {
			myX = 2;
			//myZoomOut = false;
		}
		if (myX < -2) {
			myX = -2;
		}
		if (myY > 2) {
			myY = 2;
		}
		if (myY < -2) {
			myY = -2;
		}
		
		// Convert speed and angle to vx and vy
		myVx += mySpeed * Math.cos((myAngle - 270) * Math.PI / 180);
		myVy += mySpeed * Math.sin((myAngle - 270) * Math.PI / 180);

		// Gliding on the boats
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
	
	// Check if the boat collided with the other boat
	// Used for ramming
	public boolean didCollideWithBoat(Boat b) {
		boolean didCollide = false;
		
		if (myX > b.myX + b.myWidth && myX < b.myX - b.myWidth
				&& myY > b.myY + b.myHeight && myY < b.myY - b.myHeight) {
			System.out.println("Boat Collision!");
		}
		
		return didCollide;
	}
	
	// Check if the boat collided with any islands
	public void checkCollideWithIslands() {
		for (Sprite i : Game.sprites) {
			if (i instanceof Island) {
				
				double deltaObstacleX = myX - i.myX;
		        double deltaObstacleY = myY - i.myY;
		                
		        if (deltaObstacleX <= i.myWidth/2 && deltaObstacleX >= -i.myWidth/2 
		        		&& deltaObstacleY <= i.myHeight/2 
		        		&& deltaObstacleY >= -i.myHeight/2) {
		        	System.out.println("Boat collided with island");
		        	
		        }
			}
		}
	}
	
	// Overriding die() to remove boat from myBoats
	public void die() {
		Smoke s = new Smoke(myDraw, "ExplosionAtlasFolder", 0, myX, myY);
		s.myWidth = 0.3;
		s.myHeight = 0.3;
		Game.sprites.add(s);
		Game.sprites.remove(this);
		Game.myBoats.remove(this);
		mySpeed = 0;
		myX = 0;
		myY = 0;
	}
	
}