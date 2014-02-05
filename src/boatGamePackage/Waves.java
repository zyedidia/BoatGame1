package boatGamePackage;

import edu.princeton.cs.introcs.Draw;

public class Waves extends Sprite {
	
	private int myLife;

	public Waves(Draw draw, double x, double y, int angle) {
		super(draw, "Resources/whiteCirlce.png", angle, x,y, 0.1, 0.1);
		myLife = 25;
	}

	@Override
	public void updateSelf() {
		myLife--;
		visualize();
		
		if (myLife <= 0) {
			die();
		}
	}

}
