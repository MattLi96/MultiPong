package communicator;

public interface Communicator {
	void sendObject(Object o); //sends Object O to other sockets
	void close(); //closes the connection
}
