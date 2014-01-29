package boatGamePackage;

import edu.princeton.cs.introcs.Draw;


public class CannonBall extends Sprite {
	
	// Constructor \\
	public CannonBall(Draw draw, double x, double y, double speed, double maxSpeed, int angle) {
		
		super(draw, "Resources/BlackDot.png", speed, maxSpeed, angle, x, y, 0.015, 0.015);
	}
	
	public Sprite onHit(Boat b) {
		b.myHealth -= 5;
    	System.out.println("Bullet collided with boat");
    	Smoke smoke = new Smoke(myDraw, "ExplosionAtlasFolder", 0, myX, myY);
    	this.die();
    	
    	return smoke;
	}
	
	public boolean didCollideWithBoat(Boat b) {
		
		boolean didCollide = false;
		
		double deltaObstacleX = myX - b.myX;
        double deltaObstacleY = myY - b.myY;
                
        if (deltaObstacleX <= b.myWidth/2 && deltaObstacleX >= -b.myWidth/2 
        		&& deltaObstacleY <= b.myHeight/2 
        		&& deltaObstacleY >= -b.myHeight/2) {
        	didCollide = true;
        }
        
        return didCollide;
        
	}
	
	public void updateSelf() {
		updatePosition();
		visualize();
	}
	
}
