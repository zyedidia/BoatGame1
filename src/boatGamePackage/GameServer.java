package boatGamePackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class GameServer {
	private final int myPort = 4545;
	private ServerSocket myServerSocket;
	private Socket mySocket;
	private ObjectOutputStream myOut;
	private ObjectInputStream myIn;
	
	public void beginServer() throws IOException, ClassNotFoundException {
		JOptionPane.showMessageDialog(null, "Hosting server on port: " + myPort);
		
		myServerSocket = new ServerSocket(myPort);
		
		JOptionPane.showMessageDialog(null, "Successfully started server");
		
		mySocket = myServerSocket.accept();
		
		System.out.println("Accepted connection from: " + mySocket.getInetAddress());
		
		myOut = new ObjectOutputStream(mySocket.getOutputStream());
		myIn = new ObjectInputStream(mySocket.getInputStream());
		
		Game game = new Game();
		game.loop(true, myIn, myOut, 0);
		
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		GameServer server = new GameServer();
		server.beginServer();
	}
}
