package boatGamePackage;

import edu.princeton.cs.introcs.Draw;


public class CannonBall extends Sprite {
	
	// Constructor \\
	public CannonBall(Draw draw, double x, double y, double speed, double maxSpeed, int angle) {
		
		super(draw, "Resources/BlackDot.png", speed, maxSpeed, angle, x, y, 0.015, 0.015);
	}
	
	public void didCollideWithBoat(Boat b) {
		
		double deltaObstacleX = myX - b.myX;
        double deltaObstacleY = myY - b.myY;
        
        if (deltaObstacleX <= b.myWidth/2 && deltaObstacleX >= -b.myWidth/2 
        		&& deltaObstacleY <= b.myHeight/2 
        		&& deltaObstacleY >= -b.myHeight/2) {
        	b.myHealth -= 5;
        	this.die();
        	System.out.println("Bullet collided with boat");
        }
        
	}
	
	public void updateSelf() {
		updatePosition();
		visualize();
	}
	
}
