package utilities;

import pong.Pong;

import communicator.Server;

public class RunServer implements Runnable {
	Server server = null;
	Pong pong = null;
	int client = -1;

	public RunServer(Server server, int client, Pong pong) {
		this.server = server;
		this.pong = pong;
		this.client = client;
	}

	public void run() {
		while(server.getObjInputs().get(client) != null) {
			Object o = server.getNextObject(client);

			if (o instanceof double[]) { // paddlelocation in the format [side, location]
				double[] paddleLocation = (double[]) o;
				pong.getPolygon().getSide((int) (paddleLocation[0] + .5))
						.getPaddle().setCenter(paddleLocation[1]);
				pong.getGraphics().repaint();
				server.sendObject(paddleLocation);
			} else if (o instanceof String){
				if(o.equals("break")){ //end of game
					break;
				} else { //assume it's the name
					pong.getPolygon().setPlayer((client+1)*2, (String) o);
					pong.resetConnections();
				}
			}
		}
	}
}
