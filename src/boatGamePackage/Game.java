package boatGamePackage;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import edu.princeton.cs.introcs.Draw;

public class Game implements Runnable {

	public static ArrayList<Sprite> sprites;
	public static double zoom = 1.0;
	private Draw myDraw;
	public static ArrayList<Boat> myBoats;
	public static double FPS = 1;
	public static double targetFPS = 60;
	private ObjectOutputStream myOut;
	private ObjectInputStream myIn;
	private int myOnlineBoatId;
	private boolean myOnline;

	public Game(boolean online, ObjectInputStream in, ObjectOutputStream out, int id) {
		myOut = out;
		myIn = in;
		myOnlineBoatId = id;
		myOnline = online;
		
		myDraw = new Draw("Boat Game");

		myDraw.setCanvasSize(768, 768);

		// Close the window when the exit button is pressed
		myDraw.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myDraw.frame.setJMenuBar(new JMenuBar());

		// Set the coordinate system
		myDraw.setXscale(-zoom, zoom);
		myDraw.setYscale(-zoom, zoom);
	}

	public void adjustZoom(ArrayList<Boat> boats) {
		for (Boat b : myBoats) {
			if (b.myZoomOut) {
				zoom += 0.015;
				myDraw.setXscale(-zoom, zoom);
				myDraw.setYscale(-zoom, zoom);
			}
			else if (zoom >= 1.0) {	
				zoom -= 0.005;
				myDraw.setXscale(-zoom, zoom);
				myDraw.setYscale(-zoom, zoom);
			}
		}
	}

	public void init(int numPlayerBoats, int numAiBoats) {
		// Contains all sprites to be updated on loop
		sprites = new ArrayList<Sprite>();
		myBoats = new ArrayList<Boat>();

		zoom = 1.0;
		zoom = 1.0;

		int i = 0;

		// Create Player Boats
		for (i = 0; i < numPlayerBoats; i++) {
			Boat boat = new PlayerBoat(myDraw, i);
			myBoats.add(boat);
			sprites.add(boat);

		}

		// Create AI Boats
		for (; i < numAiBoats + numPlayerBoats; i++) {
			Boat boat = new AIBoat(myDraw, i, "Resources/boat2.png");
			myBoats.add(boat);
			sprites.add(boat);
		}
	}

	public void loop() 
					throws ClassNotFoundException, IOException {
		init(2, 0);

		FPS = 1;

		double oldTime = System.currentTimeMillis();
		double beginTime = System.currentTimeMillis();
		double changeTime = 1;


		while (true) {
			if (myOnline) {
				for (Boat b : myBoats) {
					if (b.myPID == myOnlineBoatId) {
						double[] arrayToSend = {b.myX, b.myY, b.myPID, b.myAngle};
						myOut.writeObject(arrayToSend);
						myOut.writeBoolean(b.shouldFireLeft());
						myOut.writeBoolean(b.shouldFireRight());
						myOut.flush();
						System.out.println("Sent boat");
					} else {
						System.out.println("Received boat");
						double[] receivedArray = (double[]) myIn.readObject();
						boolean left = myIn.readBoolean();
						boolean right = myIn.readBoolean();
						for (Boat b1 : myBoats) {
							if (b1.myPID == receivedArray[2]) {
								b1.myX = receivedArray[0];
								b1.myY = receivedArray[1];
								b1.myAngle = receivedArray[3];
								if (right) {
									b1.fireRightGuns();
								}
								
								if (left) {
									b1.fireLeftGuns();
								}
							}
						}
					}
				}
			}
			
			changeTime = System.currentTimeMillis() - oldTime;

			//System.out.println(changeTime + " since last update");

			if (changeTime > 0) {
				FPS = 1 / changeTime * 1000;

				oldTime = System.currentTimeMillis();
			}

			// Draw the blue background
			myDraw.setPenColor(new Color(25, 25, 255));
			myDraw.filledSquare(0.0, 0.0, 100);
		//	draw.picture(0, 0, "resources/Ocean.png", 10, 10);
			
			adjustZoom(myBoats);

			updateSprites();

			// FPS counter
			myDraw.setPenColor(Color.GREEN);
			myDraw.text(-zoom, zoom, "FPS: " + Integer.toString((int) FPS));

			if (myDraw.isKeyPressed(KeyEvent.VK_ENTER)) { // Enter key (restart game button)
				loop();
			}

			if (myDraw.isKeyPressed(KeyEvent.VK_O)) { // o key (pause button)
				onPause();
			}

			myDraw.show(0);

			while((System.currentTimeMillis() - beginTime) % 16 != 0) {

			}
		}		
	}

	// When the game is paused
	public void onPause() throws ClassNotFoundException, IOException {
		Button paused = new Button(myDraw, "Unpause Game", 0, 0, Color.RED);
		Button options = new Button(myDraw, "Options", 0, -0.125/zoom, Color.RED);
		Button quit = new Button(myDraw, "Quit", 0, -0.250/zoom, Color.RED);
		while (true) {
			if (options.isClicked()) {
				//draw.frame.setVisible(false);
				//draw.frame.dispose();
				System.out.println(Thread.activeCount());
				if (Thread.activeCount() < 6) {
					OptionsMenu om = new OptionsMenu();
					Thread t = new Thread(om);
					t.start();
				}
				//om.loop();
			}
			else if (quit.isClicked()) {
				System.out.println("Closing Program: quit button clicked");
				System.exit(0);
			}
			else if (paused.isClicked()) {
				System.out.println("Pause Clicked");
				break;
			}
			paused.render();
			options.render();
			quit.render();
			myDraw.show();

			if (myDraw.isKeyPressed(80)) {
				break;
			}
		}
	}

	public void updateSprites() {
		try {
			for (int i = 0; i < sprites.size(); i++) {
				Sprite s = sprites.get(i);
				s.updateSelf();
			}
		} catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Deleted object requested. Ignoring.");
		}
	}

	@Override
	public void run() {
		try {
			loop();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}