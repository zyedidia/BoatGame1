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
	
	public Client(String ip) {
		myIpAddress = ip;
	}
	
	public void connect() throws UnknownHostException, IOException, ClassNotFoundException {
		myIpAddress = JOptionPane.showInputDialog("Enter the ip to connect to");
		JOptionPane.showMessageDialog(null, "Connecting to " + myIpAddress + "...");
		mySocket = new Socket(myIpAddress, 4545);
		System.out.println("Connection successful");
		
		myIn = new ObjectInputStream(mySocket.getInputStream());
		System.out.println("Created myIn");
		myOut = new ObjectOutputStream(mySocket.getOutputStream());
		System.out.println("Created myOut");
		
		update();
	}
	
	public void update() throws IOException, ClassNotFoundException {
		Game game = new Game();
		game.loop(true, myIn, myOut, 1);
	}
}