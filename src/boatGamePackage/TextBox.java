package boatGamePackage;

import java.awt.Color;
import java.awt.event.KeyEvent;

import edu.princeton.cs.introcs.Draw;

public class TextBox {
	private Draw myDraw;
	private double myX;
	private double myY;
	public String myText;
	private double myWidth;
	private double myHeight;
	private Color myColor;
	private Color myOriginalColor;
	
	// Constructor \\
	public TextBox(Draw draw, double x, double y, Color color) {
		myDraw = draw;
		myX = x;
		myY = y;
		myText = " ";
		myWidth = 0.025 * myText.length();
		myHeight = 0.045;
		myColor = color;
		myOriginalColor = color;
	}
	
	// Draw the TextBox
	public void render() {
		// Make sure you can't have 0 length in the box (will cause errors)
		if (myText.length() == 0) {
			myText += " ";
		} else {
			// Resizes the box based on the length of the text
			myWidth = 0.025 * myText.length();
		}
		myDraw.setPenColor(myColor);
		myDraw.rectangle(myX, myY, myWidth, myHeight);
		myDraw.text(myX, myY, myText);
	}
	
	// Check if it has been clicked
	// The TextBox can be edited if you hover over it
	// Clicking does not actually do anything but it returns a boolean
	public boolean isClicked() {
		boolean isClicked = false;
		
		// Check if the mouse is within the bounds of the rectange
		if (myDraw.mouseX() < myX + myWidth && myDraw.mouseX() > myX - myWidth
				&& myDraw.mouseY() < myY + myHeight && myDraw.mouseY() > myY - myHeight) {
			myColor = Color.orange;
			isClicked = true;
			onSelected();
			if (myDraw.mousePressed()) {
				
			}
		} else {
			myColor = myOriginalColor;
		}
		
		return isClicked;
	}
	
	// Type the text into the box
	public void onSelected() {
		// Delete a character if the delete key is pressed
		if (myDraw.isKeyPressed(KeyEvent.VK_BACK_SPACE)) {
			System.out.println("Delete");
			if (myText.length() > 0) {
				myText = myText.substring(0, myText.length() - 1);
			}
		}
		
		// Add the next key that is typed
		else if (myDraw.hasNextKeyTyped()) {
			// If the key pressed is a space, make sure it doesn't get trimmed
			if (myDraw.isKeyPressed(KeyEvent.VK_SPACE) || myText.charAt(myText.length() - 1) == ' ') {
				myText += myDraw.nextKeyTyped();
			} else {
				System.out.println("Text: \"" + myText + "\"");
				myText += myDraw.nextKeyTyped();
				myText = myText.trim();
			}
		}
		
		// Render after the text has been edited
		render();
	}
	
	// Local main method for testing and debugging
	public static void main(String[] args) {
		Draw draw = new Draw("TextBox Test");
		draw.setXscale(-1.0, 1.0);
		draw.setYscale(-1.0, 1.0);
		TextBox tb = new TextBox(draw, 0, 0, Color.BLUE);
		
		while (true) {
			draw.setPenColor(Color.white);
			draw.filledSquare(0, 0, 1.0);
			
			if (tb.isClicked()) {
				//System.out.println("TextBox Selected");
			}
			tb.render();
			
			draw.show(75);
		}
	}
}
