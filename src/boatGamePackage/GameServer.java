package boatGamePackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class GameServer {
	private final int myPort = 4545; // Boat Game servers are always hosted on port 4545
	private ServerSocket myServerSocket;
	private Socket mySocket;
	private ObjectOutputStream myOut;
	private ObjectInputStream myIn;
	
	// Host a server
	public void beginServer() throws IOException, ClassNotFoundException {
		JOptionPane.showMessageDialog(null, "Hosting server on port: " + myPort);
		
		// Start the socketserver
		myServerSocket = new ServerSocket(myPort);
		
		JOptionPane.showMessageDialog(null, "Successfully started server. Waiting for connection");
		
		// Accept the first connection request (cannot accept and more)
		mySocket = myServerSocket.accept();
		
		System.out.println("Accepted connection from: " + mySocket.getInetAddress());
		
		// Create the input and output streams to communicate with the client
		myOut = new ObjectOutputStream(mySocket.getOutputStream());
		myIn = new ObjectInputStream(mySocket.getInputStream());
		
		// Start a game (all the client-server communications are handled in the Game class
		Game game = new Game(true, myIn, myOut, 0);
		//Thread t = new Thread(game);
		//t.start();
		game.loop();
		
	}
}
