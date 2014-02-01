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
	private double[] values = new double[1];

	public OptionsMenu() throws ClassNotFoundException {
		
		values = readSerialized("options");
		
		myDraw = new Draw("Options");
		myDraw.setXscale(-1.0, 1.0);
		myDraw.setYscale(-1.0, 1.0);
		myBackButton = new Button(myDraw, "Back to Main Menu", -0.7, 0.9, Color.BLUE);
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
	
	public void render() throws ClassNotFoundException {
		myBackButton.render();
		myGuns.render();
		myDelete.render();
		
		if (myBackButton.isClicked()) {
			if (isInteger(myGuns.myText)) {
				if (Integer.parseInt(myGuns.myText) > 1) {
					values[0] = Integer.parseInt(myGuns.myText);
				} else {
					System.out.println("Guns cannot be less than 2. Resetting to: " + values[0]);
				}
			}
			
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
	}
	
	public void loop() throws ClassNotFoundException {
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
		double[] arrayToReturn = new double[0];
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
