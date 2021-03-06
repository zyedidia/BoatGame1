package boatGamePackage;

import edu.princeton.cs.introcs.Draw;
import edu.princeton.cs.introcs.StdRandom;


public class CannonBall extends Sprite {
	
	// Constructor \\
	public CannonBall(Draw draw, double x, double y, double speed, double maxSpeed, int angle) {
		
		super(draw, "Resources/BlackDot.png", speed, maxSpeed, angle, x, y, 0.02, 0.02);
	}
	
	// Run this when a bullet hits a boat
	public Sprite onHit(Boat b) {
		b.myHealth -= 5;
		// 1/15 chance you will lose a gun
		if (StdRandom.uniform(0, 15) < 2) {
			if (b.myRightGuns.size() > 1) {
				b.myRightGuns.remove(StdRandom.uniform(0, b.myRightGuns.size()));
			}
		}
		
		if (StdRandom.uniform(0, 15) < 2) {
			if (b.myLeftGuns.size() > 1) {
				b.myLeftGuns.remove(StdRandom.uniform(0, b.myLeftGuns.size()));
			}
		}
		
    	System.out.println("Bullet collided with boat");
    	
    	// Make the smoke
    	Smoke smoke = new Smoke(myDraw, "ExplosionAtlasFolder", 0, myX, myY);
    	
    	//Delete the bullet
    	this.die();
    	
    	return smoke;
	}
	
	// Return whether the bullet has hit a boat
	public boolean didCollideWithBoat(Boat b) {
		
		boolean didCollide = false;
		
		double deltaObstacleX = myX - b.myX;
        double deltaObstacleY = myY - b.myY;
                
        if (deltaObstacleX <= b.myWidth/2 && deltaObstacleX >= -b.myWidth/2 
        		&& deltaObstacleY <= b.myHeight/2 
        		&& deltaObstacleY >= -b.myHeight/2) {
        	didCollide = true;
        	GameBattle.sprites.remove(this);
        }
        
        return didCollide;
        
	}
	
	// Update self
	public void updateSelf() {
		
		// If the bullet is off the screen
		if (Math.abs(myX) > GameBattle.zoom || Math.abs(myY) > GameBattle.zoom) {
			GameBattle.sprites.remove(this); 
		}
		updatePosition();
		visualize();
		
		for (Boat b : GameBattle.myBoats) {
		
			if (didCollideWithBoat(b)) {
				GameBattle.sprites.add(onHit(b));
			}
		
		}
	}
	
}
