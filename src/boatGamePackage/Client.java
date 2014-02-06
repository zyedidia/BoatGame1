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
	
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		Client c = new Client("localhost");
		
		c.connect();
	}
	
	public void update() throws IOException, ClassNotFoundException {
		Game game = new Game();
		game.loop(true, myIn, myOut, 1);
	}
}

class Input implements Runnable {
	private ObjectInputStream myIn;
	private ObjectOutputStream myOut;
	
	public Input(ObjectOutputStream out, ObjectInputStream in) {
		myIn = in;
		myOut = out;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Waiting for ID...");
			int id = myIn.readInt();
			System.out.println("Received ID: " + id);
			Game game = new Game();
			game.loop(true, myIn, myOut, id);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


