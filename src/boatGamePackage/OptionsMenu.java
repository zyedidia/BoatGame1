package boatGamePackage;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;

import edu.princeton.cs.introcs.Draw;

public class OptionsMenu implements Runnable {
	private Draw myDraw;
	private Button myBackButton;
	private Button myDelete;
	private TextBox myGuns;
	private double[] values = new double[3];
	private TextBox myNumPlayers;
	private TextBox myNumAI;
	private Button myBackToGame;
	private boolean myIsThread;

	// Constructor \\
	public OptionsMenu(boolean isThread) throws ClassNotFoundException {
		
		//myDraw.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		values = readSerialized("options");
		
		myIsThread = isThread; // Whether or not this optionsmenu is running in a thread
		
		// Initialize all the elements of the menu
		myDraw = new Draw("Options");
		myDraw.setXscale(-1.0, 1.0);
		myDraw.setYscale(-1.0, 1.0);
		myNumAI = new TextBox(myDraw, 0, -0.5, Color.BLUE);
		myBackButton = new Button(myDraw, "Back to Main Menu", -0.7, 0.9, Color.BLUE);
		myNumPlayers = new TextBox(myDraw, 0, -0.25, Color.BLUE);
		myNumPlayers.myText = Integer.toString((int) values[1]);
		myNumAI.myText = Integer.toString((int) values[1]);
		myDelete = new Button(myDraw, "Clear Options File", -0.7, -0.8, Color.BLUE);
		myBackToGame = new Button(myDraw, "Back To Game", 0.7, 0.9, Color.BLUE);
		myGuns = new TextBox(myDraw, 0, 0.5, Color.BLUE);
		myGuns.myText = Integer.toString((int) values[0]);
	}
	
	// Check if the given String is an integer
	public static boolean isInteger(String s) {
		try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
		
		return true;
	}
	
	// Set the value of the array at index
	public void setValue(int index, String value, int min) {
		if (isInteger(value)) {
			if (Integer.parseInt(value) >= min) {
				values[index] = Integer.parseInt(value);
			} else {
				System.out.println("Guns cannot be less than " + min + ". Resetting to: " + values[0]);
			}
		}
	}
	
	// Render all the elements of the options menu
	public void render() throws ClassNotFoundException, IOException {
		
		myGuns.render();
		myDelete.render();
		myNumPlayers.render();
		myNumAI.render();
				
		// If the game is currently running in another thread display the "Back to Game"
		if (myIsThread) {
			myBackToGame.render();
			if (myBackToGame.isClicked()) {
				myDraw.frame.setVisible(false);
				myDraw.frame.dispose();
				
				serialize(values, "options");
			}
		}
		
		// If the game is not running in another thread display "Back to Menu"
		if (!myIsThread) {
			myBackButton.render();
			if (myBackButton.isClicked()) {
				
				setValue(0, myGuns.myText, 2);
				setValue(1, myNumPlayers.myText, 0);
				setValue(2, myNumAI.myText, 0);
				
				serialize(values, "options");
				
				myDraw.frame.setVisible(false);
				myDraw.frame.dispose();
				Menu menu = new Menu();
				menu.loop();
			}
		}
		
		if (myDelete.isClicked()) {
			File optionsFile = new File("options.ser");
			optionsFile.delete();
		}
		
		// Update the textBoxes
		myGuns.isClicked();
		myNumPlayers.isClicked();
		myNumAI.isClicked();
	}
	
	// Main options menu loop
	public void loop() throws ClassNotFoundException, IOException {
		myDraw.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		while (true) {
			myDraw.setPenColor(Color.white);
			myDraw.filledSquare(0, 0, 1.5);
			render();
			
			myDraw.show(50);
		}
	}
	
	// Serialize data
	public void serialize(double[] array, String fileName) {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(fileName + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(array);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in " + fileName + ".ser");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Could not write to " + fileName + ".ser: " + e1);
		}
	}
	
	public double[] readSerialized(String fileName) {
		double[] arrayToReturn = new double[3];
		FileInputStream fileIn;
		//Read in options from options.ser
		try {
			fileIn = new FileInputStream(fileName + ".ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
	        arrayToReturn = (double[]) in.readObject();
	        in.close();
	        fileIn.close();
	    // Got an error, set all values to defaults
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
