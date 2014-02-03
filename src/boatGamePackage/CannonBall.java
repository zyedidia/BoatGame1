package boatGamePackage;

import edu.princeton.cs.introcs.Draw;
import edu.princeton.cs.introcs.StdRandom;


public class CannonBall extends Sprite {
	
	// Constructor \\
	public CannonBall(Draw draw, double x, double y, double speed, double maxSpeed, int angle) {
		
		super(draw, "Resources/BlackDot.png", speed, maxSpeed, angle, x, y, 0.015, 0.015);
	}
	
	// Run this when a bullet hits a boat
	public Smoke onHit(Boat b) {
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
    	Smoke smoke = new Smoke(myDraw, "ExplosionAtlasFolder", 0, myX, myY);
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
        	Game.sprites.remove(this);
        }
        
        return didCollide;
        
	}
	
	// Update self
	public void updateSelf() {
		if (Math.abs(myX) > Game.zoom || Math.abs(myY) > Game.zoom) {
			Game.sprites.remove(this); 
		}
		updatePosition();
		visualize();
		if (didCollideWithBoat((Boat) Game.sprites.get(0))) {
			Game.sprites.add(onHit((Boat) Game.sprites.get(0)));
		}
		if (didCollideWithBoat((Boat) Game.sprites.get(1))) {
			Game.sprites.add(onHit((Boat) Game.sprites.get(1)));
		}
	}
	
}
