package boatGamePackage;

import edu.princeton.cs.introcs.Draw;

public class Waves extends Sprite {
	
	private int myLife;

	public Waves(Draw draw, double x, double y, int angle) {
		super(draw, "Resources/whiteCirlce.png", angle, x,y, 0.1, 0.1);
		myLife = 25;
	}

	// Update the waves
	@Override
	public void updateSelf() {
		// Waves will only last a certain amount of time before getting destroyed
		myLife--;
		visualize();
		
		if (myLife <= 0) {
			die();
		}
	}

}
