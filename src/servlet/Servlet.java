package servlet;

import bundle.request.*;
import bundle.response.*;
import game.state.State;
import game.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sun.org.apache.bcel.internal.generic.LSTORE;

/**
 * Main class for the servlet
 * 
 * @author Matthew
 */
@WebServlet("/")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static class Codes {
		public static final int OK = 200;
		public static final int UNAUTHORIZED = 401;
		public static final int FORBIDDEN = 403;
		public static final int NOT_FOUND = 404;
		public static final int BAD_REQUEST = 400;
	}

	private Gson gson;
	private Pong pong;
	private Authenticator auth;

	/**
	 * Initialization for the servlet
	 */
	public void init() throws ServletException {
		gson = new Gson();
		pong = new PongImpl();
		auth = new Authenticator();
	}

	// PostJsonBundle input = gson.fromJson(r, PostJsonBundle.class);
	// String json = gson.toJson(players);

	/**
	 * Handle GET request
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		ArrayList<String> pongURI = getPongURI(uri);

		if (pongURI.size() < 2) {
			defaultGet(request, response);
		} else {
			handlePongGet(request, response, pongURI);
		}
	}

	/**
	 * Handle POST request.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		ArrayList<String> pongURI = getPongURI(uri);

		if (pongURI.size() < 2) {
			defaultPost(request, response);
		} else {
			handlePongPost(request, response, pongURI);
		}
	}

	/**
	 * Handles the default get, which essentially just prints out some URI info
	 */
	private void defaultGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.addHeader("Content-Type", "text/plain");
		PrintWriter w = response.getWriter();

		w.append("This is the default get: URI = " + request.getRequestURI()
				+ "\r\n");

		w.flush();
		w.close();
		response.setStatus(Codes.OK);
	}

	/**
	 * Handles the default post, which is just a not found
	 */
	private void defaultPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter w = response.getWriter();
		w.flush();
		w.close();
		response.setStatus(Codes.NOT_FOUND);
	}

	/**
	 * Requires that pongURI has a size greater than 1
	 */
	private void handlePongGet(HttpServletRequest request,
			HttpServletResponse response, ArrayList<String> pongURI)
			throws IOException {
		if (pongURI.size() < 2) { // Safety check
			defaultGet(request, response);
			return;
		}

		// Setup
		PrintWriter w = response.getWriter();
		response.addHeader("Content-Type", "application/json");

		// The JSON string to send
		String json = "";

		// Check that the request valid
		int id = getUserId(request);
		if (id == -1) {
			defaultGet(request, response);
			return;
		} else if (!checkID(id)) {
			respondUnauthorized(response);
			return;
		}

		// In this structure so I can easily add more get if necessary
		switch (pongURI.get(1)) {
		case "state":
			json = gson.toJson(new StateResponse(pong.getState()));
			break;
		default:
			defaultGet(request, response);
			return;
		}

		w.println(json);
		w.flush();
		w.close();
		response.setStatus(Codes.OK);
	}

	/**
	 * Requires that pongURI has a size greater than 1
	 */
	private void handlePongPost(HttpServletRequest request,
			HttpServletResponse response, ArrayList<String> pongURI)
			throws IOException {
		if (pongURI.size() < 2) { // Safety check
			defaultPost(request, response);
			return;
		}

		// Setup
		BufferedReader r = request.getReader();
		PrintWriter w = response.getWriter();
		response.addHeader("Content-Type", "application/json");

		// The JSON string to send
		String json = "";

		// Login does not require a valid check
		if (!pongURI.get(1).equals("login")) {
			int id = getUserId(request);
			if (id == -1) {
				respondBadRequest(response);
				return;
			}
			if (!checkID(id)) {
				respondUnauthorized(response);
				return;
			}
		}

		// Made each it's own block for namespace reasons.
		switch (pongURI.get(1)) {
		case "login": {// login
			int id = auth.login();
			json = gson.toJson(new LoginResponse(id));
			break;
		}

		case "game": { // game related requests
			GameRequest req = gson.fromJson(r, GameRequest.class);
			if (req == null) {
				respondBadRequest(response);
				return;
			}

			boolean success = true;
			switch (req.type) {
			case START_LIVES:
				success = pong.start(req.lives);
				break;
			case START_DEFAULT:
				success = pong.start();
				break;
			case END:
				pong.end();
				break;
			}
			json = gson.toJson(new GameResponse(success));
			break;
		}

		case "player": {// player related requests
			PlayerRequest req = gson.fromJson(r, PlayerRequest.class);
			if (req == null) {
				respondBadRequest(response);
				return;
			}

			boolean success = true;
			switch (req.type) {
			case ADD:
				success = pong.addPlayer(req.player);
				break;
			case REMOVE:
				pong.removePlayer(req.player);
				break;
			}
			json = gson.toJson(new PlayerResponse(success));
			break;
		}

		case "move": { // move related requests
			MoveRequest req = gson.fromJson(r, MoveRequest.class);
			if (req == null) {
				respondBadRequest(response);
				return;
			}

			switch (req.type) {
			case LEFT:
				pong.moveLeft(req.player);
				break;
			case RIGHT:
				pong.moveRight(req.player);
				break;
			case NONE:
				pong.moveNone(req.player);
				break;
			}
			break;
		}

		default:
			defaultPost(request, response);
			return;
		}

		w.println(json);
		w.flush();
		w.close();
		response.setStatus(Codes.OK);
	}

	/**
	 * Checks if an ID is valid (in the authenticator). If the ID is not valid,
	 * it responds with an unauthorized
	 * 
	 * @return true if ID is valid, else false
	 */
	private boolean checkID(int id) {
		return auth.getUser(id) != null;
	}

	private void respondBadRequest(HttpServletResponse response)
			throws IOException {
		response.addHeader("Content-Type", "text/plain");
		PrintWriter w = response.getWriter();
		w.flush();
		w.close();
		response.setStatus(Codes.BAD_REQUEST);
	}

	private void respondUnauthorized(HttpServletResponse response)
			throws IOException {
		response.addHeader("Content-Type", "text/plain");
		PrintWriter w = response.getWriter();
		w.flush();
		w.close();
		response.setStatus(Codes.UNAUTHORIZED);
	}

	/**
	 * Gets the components of the pong URI. In other words, it splits the uri
	 * along '/' and those become the array list elements. It then removes all
	 * elements that come before the last "Pong" element, exclusive of the
	 * "Pong".
	 * 
	 * @param uri
	 *            The uri of an HttpServletRequest
	 * @return The array list. It returns an empty arraylist if there are no
	 */
	private ArrayList<String> getPongURI(String uri) {
		ArrayList<String> ret = new ArrayList<String>();
		String[] splitURI = uri.split("/");

		int lastPongIndex = -1;
		for (int i = splitURI.length - 1; i >= 0; i--) {
			if (splitURI[i].equals("Pong")) {
				lastPongIndex = i;
				break;
			}
		}

		if (lastPongIndex != -1) {
			for (int i = lastPongIndex; i < splitURI.length; i++) {
				ret.add(splitURI[i]);
			}
		}

		return ret;
	}

	/**
	 * @return the user_id from a request's parameter map. Returns -1 if there
	 *         was an issue
	 */
	private int getUserId(HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getHeader("user_id"));
		} catch (Exception e) {
			System.err.println("Was not able to get user_id");
			e.printStackTrace();
		}
		return -1;
	}
}
