package boatGamePackage;

import edu.princeton.cs.introcs.Draw;

public class Fort extends Sprite {
	
	private Cannon myCannon;

	public Fort(Draw draw, String fileName, double x, double y) {
		super(draw, fileName, 0, x, y, 0, 0);
		
		myWidth = 0.175;
		myHeight = 0.175;
		
		myCannon = new Cannon(myDraw, this);
	}
	
	public void calcAngle() {
		Boat targetBoat = Game.myBoats.get(0);
		double[] distances = new double[Game.myBoats.size()];
		
		for (int i = 0; i < Game.myBoats.size(); i++) {
			distances[i] = getDistanceToBoat(Game.myBoats.get(i));
		}
		
		
		double currentHighestDistance = 0;
		for (int i = 0; i < distances.length; i++) {
			if (distances[i] < currentHighestDistance) {
				currentHighestDistance = distances[i];
			}
		}
		
		for (Boat b : Game.myBoats) {
			if (getDistanceToBoat(b) == currentHighestDistance) {
				targetBoat = b;
			}
		}
		//targetBoat = Game.myBoats.get(0);
		
		double directRight = Math.atan2(targetBoat.myY - myY, targetBoat.myX - myX);
		
		
		double angleInDegrees = directRight * 180 / Math.PI;
		angleInDegrees -= 90;
		
		myCannon.myAngle = angleInDegrees;
		Game.sprites.add(myCannon.fire());
	}
	
	public double getDistanceToBoat(Boat b) {
		double distance = 0;
		
		distance = Math.sqrt(Math.pow(b.myX - myX, 2) + Math.pow(b.myY - myY, 2));
		
		return distance;
	}

	@Override
	public void updateSelf() {
		// TODO Auto-generated method stub
		if (Game.myBoats.size() > 1) {
			calcAngle();
		}
		updatePosition();
		visualize();
		myCannon.updateSelf();
	}

}
