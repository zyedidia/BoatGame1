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
	
	public void connect() throws UnknownHostException, IOException {
		JOptionPane.showMessageDialog(null, "Connecting to " + myIpAddress + "...");
		mySocket = new Socket(myIpAddress, 4545);
		System.out.println("Connection successful");
		
		myIn = new ObjectInputStream(mySocket.getInputStream());
		myOut = new ObjectOutputStream(mySocket.getOutputStream());
		
		Input input = new Input(myOut, myIn);
		
		Thread t = new Thread(input);
		t.start();
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client c = new Client("localhost");
		
		c.connect();
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
		Game game = new Game();
		try {
			System.out.println("Waiting for ID...");
			//int id = myIn.readInt();
			System.out.println("Received ID");

			game.loop(true, myIn, myOut, 0);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


