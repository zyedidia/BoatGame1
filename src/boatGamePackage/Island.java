package boatGamePackage;

import edu.princeton.cs.introcs.Draw;
import edu.princeton.cs.introcs.StdRandom;

public class Island extends Sprite {
	
	private Fort myFort;
	private boolean myHasFort;
	
	public Island(Draw draw, long seed) {
		super(draw, "Resources/BlackDot.png", 0, 0, 0, 0, 0);
		
		init();
	}
	
	public Island(Draw draw) {
		super(draw, "Resources/BlackDot.png", 0, 0, 0, 0, 0);
		
		init();
	}
	
	public void init() {
		myX = StdRandom.uniform(-10.0, 10.0);
		myY = StdRandom.uniform(-10.0, 10.0);
		
		myWidth = StdRandom.uniform(0.2, 0.5);
		myHeight = StdRandom.uniform(0.2, 0.5);
		
		int randomNum = StdRandom.uniform(0, 5);
		
		if (randomNum > 1) {
			myHasFort = true;
			myFort = new Fort(myDraw, "Resources/whiteCirlce.png", myX, myY);
		}
	}

	@Override
	public void updateSelf() {
		// TODO Auto-generated method stub
		
		
		updatePosition();
		visualize();
		
		if (myHasFort) {
			myFort.updateSelf();
		}
	}

}
