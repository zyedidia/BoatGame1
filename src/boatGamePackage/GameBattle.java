package boatGamePackage;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import edu.princeton.cs.introcs.Draw;
import edu.princeton.cs.introcs.StdRandom;

public class GameBattle implements Runnable {

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
	private String myName;

	// Constructor \\
	public GameBattle(boolean online, ObjectInputStream in, ObjectOutputStream out, int id) {
		myOut = out;
		myIn = in;
		myOnlineBoatId = id;
		myOnline = online;
		
		String[] names = readSerializedString("name");
		myName = names[0];
		
		myDraw = new Draw("Boat Game");

		myDraw.setCanvasSize(768, 768);

		// Close the window when the exit button is pressed
		myDraw.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myDraw.frame.setJMenuBar(new JMenuBar());

		// Set the coordinate system
		myDraw.setXscale(-zoom, zoom);
		myDraw.setYscale(-zoom, zoom);
	}

	// Adjust the zoom of the screen based on where the boats are
	public void adjustZoom(ArrayList<Boat> boats) {
		int zoomIn = 0;
		for (Boat b : myBoats) {
			if (b.myZoomOut) {
				zoom += 0.015;
				myDraw.setXscale(-zoom, zoom);
				myDraw.setYscale(-zoom, zoom);
			}
			else if (zoom >= 1.0) {	
				zoomIn += 1;
			}
		}
		
		if (zoomIn == myBoats.size()) {
			zoom -= 0.005;
			myDraw.setXscale(-zoom, zoom);
			myDraw.setYscale(-zoom, zoom);
		}
	}

	// Initialize everything
	public void init(int numPlayerBoats, int numAiBoats, boolean withIsland) throws IOException {
		long seed = (long) StdRandom.uniform(0.0, Double.MAX_VALUE);
		if (myOnline && myOnlineBoatId == 0) {
			myOut.writeLong(seed);
			System.out.println("Sent seed: " + seed);
			StdRandom.setSeed(seed);
		} else if (myOnline) {
			seed = myIn.readLong();
			System.out.println("Received seed: " + seed);
			StdRandom.setSeed(seed);
		}
		
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
		
		if (withIsland) {
			// Create islands
			for (int c = 0; c < 20; c++) {
				Island il = new Island(myDraw, seed);
				sprites.add(il);
			}
		}
	}

	public void loop() throws ClassNotFoundException, IOException {
		// Online games must initialize with 2 players and no AI
		if (myOnline) {
			init(2, 0, false);
		} else {
			// This might be changed in later updates based on a file from the options menu
			double[] values = readSerializedDouble("options");
			init((int) values[1], (int) values[2], false);
		}

		FPS = 1;

		double oldTime = System.currentTimeMillis();
		double beginTime = System.currentTimeMillis();
		double changeTime = 1;

		//hi zack this is Nicholas I know what a comment does :) 

		while (true) {
			
			changeTime = System.currentTimeMillis() - oldTime;

			// Get the frames per second
			if (changeTime > 0) {
				FPS = 1 / changeTime * 1000;

				oldTime = System.currentTimeMillis();
			}

			// Draw the blue background
			myDraw.setPenColor(new Color(25, 25, 255));
			myDraw.filledSquare(0.0, 0.0, 100);
			myDraw.setPenColor(Color.GREEN);
			myDraw.square(0, 0, 2);
			//myDraw.picture(0, 0, "resources/Ocean.png", 30, 30);
			
			// Adjust the zoom of the screen
			adjustZoom(myBoats);

			// Update all the sprites
			updateSprites();
			
			// Handle the network
			handleNetwork();

			// FPS counter
			myDraw.setPenColor(Color.GREEN);
			myDraw.text(-zoom, zoom, "FPS: " + Integer.toString((int) FPS));

			if (myDraw.isKeyPressed(KeyEvent.VK_ENTER)) { // Enter key (restart game button)
				loop();
			}

			if (myDraw.isKeyPressed(KeyEvent.VK_O)) { // o key (pause button)
				onPause();
			}

			// Display all the changes to the screen
			myDraw.show(0);

			// Lock the FPS to be 62 frames per second
			while((System.currentTimeMillis() - beginTime) % 16 != 0) {

			}
		}		
	}

	// When the game is paused
	public void onPause() throws ClassNotFoundException, IOException {
		double oldZoom = zoom;
		zoom = 1.0;
		myDraw.setXscale(-zoom, zoom);
		myDraw.setYscale(-zoom, zoom);
		Button paused = new Button(myDraw, "Unpause Game", 0, 0, Color.RED);
		Button options = new Button(myDraw, "Options", 0, -0.250, Color.RED);
		Button menu = new Button(myDraw, "Main Menu", 0, -0.125, Color.RED);
		Button quit = new Button(myDraw, "Quit", 0, -0.375, Color.RED);
		while (true) {
			if (options.isClicked()) {
				System.out.println(OptionsMenu.isRunning);
				if (!OptionsMenu.isRunning) {
					OptionsMenu om = new OptionsMenu(true);
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
				// Unpause the game
				myDraw.setXscale(-oldZoom, oldZoom);
				myDraw.setYscale(-oldZoom, oldZoom);
				System.out.println("Pause Clicked");
				break;
			}
			else if (menu.isClicked()) {
				myDraw.frame.setVisible(false);
				myDraw.frame.dispose();
				
				Menu m = new Menu();
				m.loop();
			}
			// Render all elements of the pause menu
			paused.render();
			menu.render();
			options.render();
			quit.render();
			myDraw.show();

			// Unpause if the "P" key is pressed
			if (myDraw.isKeyPressed(KeyEvent.VK_P)) {
				myDraw.setXscale(-oldZoom, oldZoom);
				myDraw.setYscale(-oldZoom, oldZoom);
				break;
			}
		}
	}

	// Update all the objects being drawn to the screen
	public void updateSprites() {
		try {
			for (int i = 0; i < sprites.size(); i++) {
				Sprite s = sprites.get(i);
				s.updateSelf();
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Deleted object requested. Ignoring.");
		}
	}
	
	public double[] readSerializedDouble(String fileName) {
		double[] arrayToReturn = new double[3];
		FileInputStream fileIn;
		//Read in options from options.ser
		try {
			fileIn = new FileInputStream(fileName + ".ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
	        arrayToReturn = (double[]) in.readObject();
	        in.close();
	        fileIn.close();
		} catch (IOException e) {
			arrayToReturn[0] = 5; // Default settings
			arrayToReturn[1] = 2;
			arrayToReturn[2] = 0;
			System.out.println("Could not read from " + fileName + ".ser: " + e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			arrayToReturn[0] = 5;
			arrayToReturn[1] = 2;
			arrayToReturn[2] = 0;
			System.out.println("Could not read from " + fileName + ".ser: " + e);
		}
		
		return arrayToReturn;
	}
	
	public String[] readSerializedString(String fileName) {
		String[] arrayToReturn = new String[3];
		FileInputStream fileIn;
		//Read in options from options.ser
		try {
			fileIn = new FileInputStream(fileName + ".ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
	        arrayToReturn = (String[]) in.readObject();
	        in.close();
	        fileIn.close();
		} catch (IOException e) {
			arrayToReturn[0] = "Player" + (myOnlineBoatId + 1);
			System.out.println("Could not read from " + fileName + ".ser: " + e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			arrayToReturn[0] = "Player" + (myOnlineBoatId + 1);
			System.out.println("Could not read from " + fileName + ".ser: " + e);
		}
		
		return arrayToReturn;
	}
	
	public void handleNetwork() throws IOException, ClassNotFoundException {
		if (myOnline) {
			// Do all of this if the game is over the Internet
			for (Boat b : myBoats) {
				// If the boat is of the id that this computer received from the server
				// This computer is responsible for updating the boat with 
				// the id received from the server
				if (b.myPID == myOnlineBoatId) {
					// Put all the boat's data into an array and send it to the server
					double[] arrayToSend = {b.myX, b.myY, b.myPID, b.myAngle};
					myOut.writeObject(arrayToSend);
					myOut.writeBoolean(b.shouldFireLeft());
					myOut.writeBoolean(b.shouldFireRight());
					myOut.writeUTF(myName);
					myOut.flush();
					
					// Display the name above the player
					myDraw.setPenColor(Color.GREEN);
					myDraw.text(b.myX, b.myY, myName);
				} else {
					// Make sure the boat which this computer is not responsible for
					// doesn't get any key inputs (it cannot be affected by the other computers)
					if (b instanceof PlayerBoat) {
						((PlayerBoat) b).myReceivesKeyInputs = false;
					}
					// Set the boat to have the coordinates and 
					// characteristics of the received array
					double[] receivedArray = (double[]) myIn.readObject();
					boolean left = myIn.readBoolean();
					boolean right = myIn.readBoolean();
					String name = myIn.readUTF();
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
							
							// Display the name of the player
							myDraw.setPenColor(Color.GREEN);
							myDraw.text(b1.myX, b1.myY, name);
						}
					}
				}
			}
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