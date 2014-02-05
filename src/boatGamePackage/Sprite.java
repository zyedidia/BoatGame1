package boatGamePackage;

import java.io.Serializable;

import edu.princeton.cs.introcs.Draw;

public abstract class Sprite implements Serializable {
	protected Draw myDraw;
	protected double mySpeed;
	protected double myAngle;
	protected double myMaxSpeed;
	protected double myX;
	protected double myY;
	protected String myFileName;
	protected double myWidth;
	protected double myHeight;
	protected double myVx;
	protected double myVy;
	protected boolean isDead;
	
	// Constructors \\
	public Sprite(Draw draw, String fileName, double speed, double maxSpeed, 
			int angle, double x, double y, double width, double height) {
		myDraw = draw;
		mySpeed = speed;
		myAngle = angle;
		myX = x;
		myY = y;
		myMaxSpeed = speed;
		myFileName = fileName;
		myWidth = width;
		myHeight = height;
	}
	
	public Sprite(Draw draw, String fileName, int angle, double x, double y, double width, double height) {
		this(draw, fileName, 0, 0.01, angle, x, y, width, height);
	}
	
	public Sprite(Draw draw, String fileName) {
		this(draw, fileName, 0, 0.01, 0, 0, 0, 0.2, 0.2);
	}
	
	// Draw the sprite to the screen
	public void visualize() {
		myDraw.picture(myX, myY, myFileName, myWidth, myHeight, myAngle);
	}
	
	// Update the position of the sprite
	public void updatePosition() {
		
		// Set speed to maxSpeed if speed is greater than maxSpeed
		if (mySpeed > myMaxSpeed) {
    		mySpeed = myMaxSpeed;
    	}
    	if (mySpeed < -myMaxSpeed) {
    		mySpeed = -myMaxSpeed;
    	}
    	
    	// Convert speed and angle to vx and vy
    	myVx = mySpeed * Math.cos((myAngle - 270) * Math.PI / 180);
		myVy = mySpeed * Math.sin((myAngle - 270) * Math.PI / 180);
		
		// Update the position
		myX += myVx;
		myY += myVy;
	}
	
	// Put the sprite off the screen
	public void die() {
		isDead = true;
		Game.sprites.remove(this);
		mySpeed = 0;
		myX = 0;
		myY = 0;
	}
	
	//For type-safety
	public abstract void updateSelf();
}
