package boatGamePackage;

import edu.princeton.cs.introcs.Draw;

public class Waves extends Sprite {



	public Waves(Draw draw, double x, double y, int angle) {
		// TODO Auto-generated constructor stub
		super(draw, "Resources/oceanwaves.png", angle, x,y, 0.2, 0.5);
	}

	@Override
	public void updateSelf() {
		// TODO Auto-generated method stub
		visualize();
	}

	public void checkwave(double mySpeed) {
		// TODO Auto-generated method stub
		if(mySpeed<0.001){
			System.out.println("Slow");
		}
	}

}
