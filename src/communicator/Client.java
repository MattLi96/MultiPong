package communicator;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import utilities.Constants;

public class Client implements Communicator {
	
	final String HOST; 
	Socket socket = null;
	ObjectOutputStream objOutput = null;
	ObjectInputStream objInput = null;
	
	Socket ballSocket = null;
	ObjectInputStream ballInput = null;
	
	int writeCount = 0;
	
	public Client(String ipIdentifier) {
		HOST = ipIdentifier;
		
		try {
			socket = new Socket(HOST, Constants.PORT);
			objOutput = new ObjectOutputStream(socket.getOutputStream());
			objInput = new ObjectInputStream(socket.getInputStream());
			
			ballSocket = new Socket(HOST, Constants.BALL_PORT);
			ballInput = new ObjectInputStream(ballSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + HOST);
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + HOST);
			System.exit(0);
		}
	}
	
	public synchronized void sendObject(Object o) {
		try {
			objOutput.writeUnshared(o);
			if(writeCount == 1000){
				objOutput.reset();
				writeCount = 0;
			}
			writeCount++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Point2D getNextBallLocation(){
		Object ret = null;
		try {
			ret = ballInput.readUnshared();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ball disconnected: connection probably lost");
			e.printStackTrace();
			System.exit(1);
		}
		return (ret instanceof Point2D? (Point2D)ret : null);
	}
	
	public Object getNextObject() {
		Object ret = null;
		try {
			ret = objInput.readUnshared();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("connection probably lost: client");
			e.printStackTrace();
			System.exit(1);
		}
		return ret;
	}
	
	public void close() {
		try {
			objOutput.close();
			objInput.close();
			socket.close();
			
			ballInput.close();
			ballSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
