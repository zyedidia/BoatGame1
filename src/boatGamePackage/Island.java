package boatGamePackage;

import edu.princeton.cs.introcs.Draw;
import edu.princeton.cs.introcs.StdRandom;

public class Island extends Sprite {
	
	public Island(Draw draw, long seed) {
		super(draw, "Resources/BlackDot.png", 0, 0, 0, 0, 0);
		
		myX = StdRandom.uniform(-10.0, 10.0);
		myY = StdRandom.uniform(-10.0, 10.0);
		
		myWidth = StdRandom.uniform(0.1, 0.5);
		myHeight = StdRandom.uniform(0.1, 0.5);
	}
	
	public Island(Draw draw) {
		super(draw, "Resources/BlackDot.png", 0, 0, 0, 0, 0);
		
		myX = StdRandom.uniform(-10.0, 10.0);
		myY = StdRandom.uniform(-10.0, 10.0);
		
		myWidth = StdRandom.uniform(0.1, 0.5);
		myHeight = StdRandom.uniform(0.1, 0.5);
	}

	@Override
	public void updateSelf() {
		// TODO Auto-generated method stub
		updatePosition();
		visualize();
	}

}
