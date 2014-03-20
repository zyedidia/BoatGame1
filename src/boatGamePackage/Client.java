package boatGamePackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client {
	private String myIpAddress;
	private Socket mySocket;
	private ObjectInputStream myIn;
	private ObjectOutputStream myOut;
	
	// Connect to a server
	public void connect() throws UnknownHostException, IOException, ClassNotFoundException {
		// Show a popup which asks for the ip address
		myIpAddress = JOptionPane.showInputDialog("Enter the IP Address to connect to");
		JOptionPane.showMessageDialog(null, "Connecting to " + myIpAddress + "...");
		
		// Connect to the server at port 4545 (all boat game servers are hosted at this port)
		mySocket = new Socket(myIpAddress, 4545);
		System.out.println("Connection successful");
		
		// Create the input and output streams to communicate with the server
		myIn = new ObjectInputStream(mySocket.getInputStream());
		myOut = new ObjectOutputStream(mySocket.getOutputStream());
		
		// Create a Game instance
		// All client-server communications will be handled by the Game class
		GameBattle game = new GameBattle(true, myIn, myOut, 1);
		//Thread t = new Thread(game);
		//t.start();
		game.loop();
	}
}