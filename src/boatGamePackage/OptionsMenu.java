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

public class OptionsMenu {
	private Draw myDraw;
	private Button myBackButton;
	private Button myDelete;
	private TextBox myGuns;
	private double[] values = new double[3];
	private TextBox myNumPlayers;
	private TextBox myNumAI;

	public OptionsMenu() throws ClassNotFoundException {
		
		values = readSerialized("options");
		
		myDraw = new Draw("Options");
		myDraw.setXscale(-1.0, 1.0);
		myDraw.setYscale(-1.0, 1.0);
		myBackButton = new Button(myDraw, "Back to Main Menu", -0.7, 0.9, Color.BLUE);
		myNumPlayers = new TextBox(myDraw, 0, -0.25, Color.BLUE);
		//myNumPlayers.myText = Integer.toString((int) values[1]);
		//myNumAI.myText = Integer.toString((int) values[2]);
		myNumAI = new TextBox(myDraw, 0, -0.5, Color.BLUE);
		myDelete = new Button(myDraw, "Clear Options File", -0.7, -0.8, Color.BLUE);
		myGuns = new TextBox(myDraw, 0, 0.5, Color.BLUE);
		myGuns.myText = Integer.toString((int) values[0]);
	}
	
	public static boolean isInteger(String s) {
		try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
		
		return true;
	}
	
	public void setValue(int index, String value, int min) {
		if (isInteger(value)) {
			if (Integer.parseInt(value) >= min) {
				values[index] = Integer.parseInt(value);
			} else {
				System.out.println("Guns cannot be less than " + min + ". Resetting to: " + values[0]);
			}
		}
	}
	
	public void render() throws ClassNotFoundException, IOException {
		myBackButton.render();
		myGuns.render();
		myDelete.render();
		myNumPlayers.render();
		myNumAI.render();
		
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
		
		if (myDelete.isClicked()) {
			File optionsFile = new File("options.ser");
			optionsFile.delete();
		}
		
		myGuns.isClicked();
		myNumPlayers.isClicked();
		myNumAI.isClicked();
	}
	
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
			arrayToReturn[0] = 5; // Default amount of cannons
			System.out.println("Could not read from " + fileName + ".ser: " + e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not read from " + fileName + ".ser: " + e);
		}
		
		return arrayToReturn;
	}
}
