package utilities;


import java.awt.geom.Point2D;

import pong.Pong;

import serializable.Polygon;
import serializable.Score;

import communicator.Client;


public class RunClient implements Runnable{
	Client client = null;
	Pong pong = null;
	
	public RunClient(Client client, Pong pong){
		this.client = client;
		this.pong = pong;
		new Thread(new ProcessBall()).start();
	}
	
	public void run() {
		Object o = null;
    	while(true){
			o = client.getNextObject();
			if(o instanceof double[]){ //double[] is paddles in the form [side, location]
				double[] center = (double[]) o;
				int side = (int)(center[0] + .5);
				if(side != pong.getSide()){
					pong.getPolygon().getSide(side)
							.getPaddle().setCenter(center[1]);
					pong.getGraphics().repaint();
				}
			} else if(o instanceof Score){
				pong.setScore((Score)o);
				pong.getScore().printScore();
			} else if(o instanceof Polygon){
				pong.setPolygon((Polygon) o);
			} else if(o instanceof Integer){ //Integers are sides
				pong.setSide((Integer) o);
				client.sendObject(Constants.name);
			} else if(o instanceof String){
				if(o.equals("break")){
					client.sendObject("break");
				}
			} else {
				System.out.println("not understood object type");
				System.out.println(o);
			}
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}
	
	class ProcessBall implements Runnable{
		public void run() {
			Point2D p = null;
			while(true){
				p = client.getNextBallLocation();
				if(p != null){
					pong.getBall().setLocation(p);
					pong.getGraphics().repaint();
				}
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
}