package boatGamePackage;

import java.awt.Color;

import edu.princeton.cs.introcs.Draw;

public class Fort extends Sprite {
	
	private Cannon myCannon;
	private final int myReloadTime = 100;
	private int myReloadProgress = 0;

	public Fort(Draw draw, String fileName, double x, double y) {
		super(draw, fileName, 0, x, y, 0, 0);
		
		myWidth = 0.175;
		myHeight = 0.175;
		
		myCannon = new Cannon(myDraw, this);
	}
	
	public void calcAngle() {
		Boat targetBoat = GameBattle.myBoats.get(0);
		double[] distances = new double[GameBattle.myBoats.size()];
		
		for (int i = 0; i < GameBattle.myBoats.size(); i++) {
			distances[i] = getDistanceToBoat(GameBattle.myBoats.get(i));
		}
		
		
		double currentHighestDistance = 0;
		for (int i = 0; i < distances.length; i++) {
			if (distances[i] < currentHighestDistance) {
				currentHighestDistance = distances[i];
			}
		}
		
		for (Boat b : GameBattle.myBoats) {
			if (getDistanceToBoat(b) == currentHighestDistance) {
				targetBoat = b;
			}
		}
		
		double directRight = Math.atan2(targetBoat.myY - myY, targetBoat.myX - myX);
		
		double angleInDegrees = directRight * 180 / Math.PI;
		angleInDegrees -= 90;
		
		myCannon.myAngle = angleInDegrees;
		
		if (myReloadProgress == 0) {
			GameBattle.sprites.add(myCannon.fire());
			myReloadProgress = myReloadTime;
		} else {
			myReloadProgress -= 1;
		}
		
		myDraw.setPenColor(new Color(0, 255, 0, 100));
		myDraw.filledCircle(myX, myY, myReloadProgress * 0.005);
		
	}
	
	public double getDistanceToBoat(Boat b) {
		double distance = 0;
		
		distance = Math.sqrt(Math.pow(b.myX - myX, 2) + Math.pow(b.myY - myY, 2));
		
		return distance;
	}

	@Override
	public void updateSelf() {
		// TODO Auto-generated method stub
		if (GameBattle.myBoats.size() > 1) {
			calcAngle();
		}
		updatePosition();
		visualize();
		myCannon.updateSelf();
	}

}
