package client.http;

import game.Pong;
import game.state.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import utilities.TimerBarrier;
import bundle.request.GameRequest;
import bundle.request.GameRequest.GameRequestType;
import bundle.request.MoveRequest;
import bundle.request.MoveRequest.Type;
import bundle.request.PlayerRequest;
import bundle.request.PlayerRequest.PlayerRequestType;
import bundle.request.Request;
import bundle.response.GameResponse;
import bundle.response.LoginResponse;
import bundle.response.PlayerResponse;
import bundle.response.StateResponse;

import com.google.gson.Gson;

/**
 * An implementation of Pong that uses http requests to a Pong servlet.
 * 
 * @author Matthew
 */
public class HttpPong implements Pong {
	private String url;
	private Gson gson;

	private boolean idSet;
	private int id;

	// Store the current state for efficiency, use a separate thread to keep up
	// to date.
	private volatile State currState;

	private StateUpdater updater;

	/**
	 * 
	 * @param url
	 *            baseURL of the server to connect to
	 * @throws MalformedURLException
	 */
	public HttpPong(String url) throws MalformedURLException {
		try {
			idSet = false;
			this.url = url + "/Pong"; // It's assumed that every request from
										// here
										// has at least pong
			gson = new Gson();

			// Test to see if the url is valid, if not it will throw an
			// exception
			new URL(url);

			// Obtain an ID
			BufferedReader r = communicate("POST", "/login", null);
			LoginResponse resp = gson.fromJson(r, LoginResponse.class);
			id = resp.id;
			idSet = true;

			updater = new StateUpdater();
			updater.updateState(); // Update the state once so currState has a
									// state
			new Thread(updater).start();
		} catch (MalformedURLException e) {
			throw e;
		} catch (Exception e) { // Likely the same issue, which is invalid url
			e.printStackTrace();
			throw new MalformedURLException();
		}
	}

	// these two methods should work without locks since assignment is atomic
	// and currState is volatile
	@Override
	public State getState() {
		return currState;
	}

	private void updateCurrState(State state) {
		currState = state;
	}

	@Override
	public boolean start(int lives) {
		BufferedReader r = communicate("POST", "/game", new GameRequest(
				GameRequestType.START_LIVES, lives));
		GameResponse resp = gson.fromJson(r, GameResponse.class);
		return resp.success;
	}

	@Override
	public boolean start() {
		BufferedReader r = communicate("POST", "/game", new GameRequest(
				GameRequestType.START_DEFAULT));
		GameResponse resp = gson.fromJson(r, GameResponse.class);
		return resp.success;
	}

	@Override
	public void end() {
		communicate("POST", "/game", new GameRequest(GameRequestType.END));
	}

	@Override
	public boolean addPlayer(String player) {
		BufferedReader r = communicate("POST", "/player", new PlayerRequest(
				player, PlayerRequestType.ADD));
		PlayerResponse resp = gson.fromJson(r, PlayerResponse.class);
		return resp.success;
	}

	@Override
	public void removePlayer(String player) {
		communicate("POST", "/player", new PlayerRequest(player,
				PlayerRequestType.REMOVE));
	}

	@Override
	public void moveLeft(String player) {
		communicate("POST", "/move", new MoveRequest(player, Type.LEFT));
	}

	@Override
	public void moveRight(String player) {
		communicate("POST", "/move", new MoveRequest(player, Type.RIGHT));
	}

	@Override
	public void moveNone(String player) {
		communicate("POST", "/move", new MoveRequest(player, Type.NONE));
	}

	/**
	 * Use this method to send messages to server and then get the response
	 * 
	 * @param requestMethod
	 *            the type of request (POST, DELETE, GET)
	 * @param addedURL
	 *            additional components to add to the end of the base url
	 * @param body
	 *            A request bundle to put in the body. If the body is null then
	 *            the communicate does not send anything.
	 * @return A buffered reader with the given response. Returns null if
	 *         something went wrong
	 */
	private BufferedReader communicate(String requestMethod, String addedURL,
			Request body) {
		String requestURL = url + addedURL;
		BufferedReader ret = null;

		try {
			URL newURL = new URL(requestURL);
			HttpURLConnection connection = (HttpURLConnection) newURL
					.openConnection();
			connection.setRequestMethod(requestMethod);

			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");

			if (idSet) {
				connection.setRequestProperty("user_id", "" + id);
			}

			connection.connect();
			if (body != null) {
				PrintWriter w = new PrintWriter(connection.getOutputStream());
				w.println(gson.toJson(body));
				w.flush();
			}

			ret = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
		} catch (MalformedURLException e) {
			System.err.println("bad url: " + requestURL);
		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}

		return ret;
	}

	private class StateUpdater implements Runnable {
		private final TimerBarrier barrier;
		private volatile boolean stopEvent;

		/**
		 * Rate is in milliseconds (updates every 30 milliseconds)
		 */
		private static final int default_rate = 30;

		public StateUpdater() {
			this(default_rate);
		}

		public StateUpdater(int rate) {
			barrier = new TimerBarrier(rate);
			stopEvent = false;
		}

		@SuppressWarnings("unused")
		public void stop() {
			stopEvent = true;
		}

		public void run() {
			// Setup timer here
			barrier.start();

			while (!stopEvent) {
				// Wait for appropriate time to go
				barrier.await();
				updateState();
			}

			barrier.stop();
		}

		public void updateState() {
			BufferedReader r = communicate("GET", "/state", null);
			StateResponse resp = gson.fromJson(r, StateResponse.class);
			if (resp == null) {
				System.err.println("State was null!");
			} else {
				updateCurrState(resp.state);
			}
		}

	}
}
