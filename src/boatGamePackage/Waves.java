package boatGamePackage;

import edu.princeton.cs.introcs.Draw;

public class Waves extends Sprite {



	public Waves(Draw draw, double x, double y, int angle) {
		super(draw, "Resources/oceanwaves.png", angle, x,y, 0.2, 0.5);
	}

	@Override
	public void updateSelf() {
		visualize();
	}

}
