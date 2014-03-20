package boatGamePackage;

import java.awt.Color;

import edu.princeton.cs.introcs.Draw;

public class Smoke extends Sprite {

	private String myFileAtlas;
	protected double iteration = 0;
	protected boolean isFinished = false;
	
	// Constructor \\
	public Smoke(Draw draw, String fileAtlas, int angle, double x, double y) {
		super(draw, "Resources/" + fileAtlas + "/explosion" + 0 + ".png", angle, x, y, 0.075, 0.075);
		iteration += 0.5;
		myFileAtlas = fileAtlas;
	}

	// Update the smoke
	@Override
	public void updateSelf() {
		animate();
		if (isFinished) {
			GameBattle.sprites.remove(this);
		}
	}
	
	// Display the next frame of the animation
	public void animate() {
		if (iteration < 14) {
			myFileName = "Resources/" + myFileAtlas + "/explosion" + (int) iteration + ".png";
			visualize();
		} else {
			// The smoke has finished animating - destroy it
			isFinished = true;
			die();
		}
		iteration += 0.5;
	}
	
	
	// Local main function for testing and debugging
	public static void main(String[] args) {
		Draw draw = new Draw();
		draw.setXscale(-1.0, 1.0);
		draw.setYscale(-1.0, 1.0);
		
		Smoke smoke = new Smoke(draw, "ExplosionAtlasFolder", 0, 0, 0);
		Smoke smoke1 = new Smoke(draw, "ExplosionAtlasFolder", 0, 0.5, 0.5);
		
		while (true) {
			draw.setPenColor(Color.white);
			draw.filledSquare(0, 0, 1);
			
			smoke.animate();
			smoke1.animate();
			
			draw.show(20);
		}

	}
}
