package boatGamePackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Server {
	private final int myPort = 4545;
	private ServerSocket myServerSocket;
	private Socket mySocket;
	private ObjectOutputStream myOut;
	private ObjectInputStream myIn;
	private User[] myUsers = new User[4];
	
	public void beginServer() throws IOException {
		JOptionPane.showMessageDialog(null, "Hosting server on port: " + myPort);
		
		myServerSocket = new ServerSocket(myPort);
		
		JOptionPane.showMessageDialog(null, "Successfully started server");
		
		while (true) {
			mySocket = myServerSocket.accept();
			
			for (int i = 0; i < 4; i++) {
				System.out.println("Connection from: " + mySocket.getInetAddress());
				
				myOut = new ObjectOutputStream(mySocket.getOutputStream());
				myIn = new ObjectInputStream(mySocket.getInputStream());
				System.out.println("Did this for the " + i + "th time");
				
				if (myUsers[i] == null) {
					myUsers[i] = new User(myOut, myIn, myUsers, i);
					Thread thread = new Thread(myUsers[i]);
					thread.start();
					System.out.println("Started thread");
					break;
				} else {
					System.out.println("Not null");
				}
				
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.beginServer();
	}
}

class User implements Runnable {
	
	private ObjectOutputStream myOut;
	private ObjectInputStream myIn;
	private User[] myUsers = new User[4];
	private int myId;
	
	public User(ObjectOutputStream out, ObjectInputStream in, User[] users, int id) {
		myOut = out;
		myIn = in;
		myUsers = users;
		myId = id;
	}

	@Override
	public void run() {
		try {
			myUsers[0].myOut.writeInt(myId);
			System.out.println("Sent ID");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		while (true) {
			try {
				System.out.println("Waiting for boat...");
				Boat receivedBoat = (Boat) myIn.readObject();
				System.out.println("Received boat");
				
				for (int i = 0; i < myUsers.length; i++) {
					myUsers[i].myOut.writeObject(receivedBoat);
					System.out.println("Sent boat");
				}
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				myUsers[myId] = null;
				
				try {
					myUsers[myId].myIn.close();
					myUsers[myId].myOut.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
