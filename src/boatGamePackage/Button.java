package boatGamePackage;

import java.awt.Color;

import edu.princeton.cs.introcs.Draw;

public class Button {
	private String myText;
	private Draw myDraw;
	private double myX;
	private double myY;
	private double myWidth;
	private double myHeight;
	private Color myColor;
	private Color myOriginalColor;
	
	// Constructor \\
	public Button(Draw draw, String text, double x, double y, Color buttonColor) {
		myDraw = draw;
		myText = text;
		myX = x;
		myY = y;
		myColor = buttonColor;
		myOriginalColor = buttonColor;
		myHeight = 0.045;
		myWidth = 0.045 * myText.length();
	}
	
	// Draw the button
	public void render() {
		myDraw.setPenColor(myColor);
		myDraw.text(myX, myY, myText);
	}
	
	// Check if the button has been clicked and change its color if you hover over it
	public boolean isClicked() {
		boolean isClicked = false;
		
		// Check if the mouse is within the bounds of the rectangle
		if (myDraw.mouseX() < myX + myWidth && myDraw.mouseX() > myX - myWidth
				&& myDraw.mouseY() < myY + myHeight && myDraw.mouseY() > myY - myHeight) {
			// Change button color
			myColor = Color.orange;
			if (myDraw.mousePressed()) {
				isClicked = true;
			}
		} else {
			// Change the color back if the mouse isn't hovering over the button
			myColor = myOriginalColor;
		}
		
		return isClicked;
	}
		
}
