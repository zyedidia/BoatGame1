package boatGamePackage;

import edu.princeton.cs.introcs.Draw;

public class Button {
	private String myText;
	private Draw myDraw;
	private double myX;
	private double myY;
	private double myWidth;
	private double myHeight;
	
	public Button(Draw draw, String text, double x, double y) {
		myDraw = draw;
		myText = text;
		myX = x;
		myY = y;
	}
	
	public void render() {
		myDraw.text(myX, myY, myText);
	}
}
