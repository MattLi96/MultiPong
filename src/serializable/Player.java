/*
 * Player.java
 */
package serializable;

import java.awt.event.*;

/**
 * The human interface in the Pong Legacy Game
 *
 * @author 2009-2010 WHS
 * <a href="http://winchester.k12.ma.us/~dpetty/apcs/">APCS</a> class
 */
public class Player extends Side implements KeyListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7449848015610274550L;
	/**
     * Player name
     */
    private String name;
    
    /**
     * Constructs a blank player
     */
    public Player() {
        super();
        name = "NOOB";
    }

    /**
     * Constructs a new Player with arguments
     * @param name Player name
     * @param X1 First X coordinate
     * @param Y1 First Y coordinate
     * @param X2 Second X coordinate
     * @param Y2 Second Y coordinate
     * @param numSide The side number
     */
    public Player(String name, double X1, double Y1, double X2, double Y2, int numSide) {
        super(X1, Y1, X2, Y2, Side.Status.OCCUPIED, numSide);
        this.name = name;
    }

    public Player(String name, Side side) {
        this(name, side.getX1(), side.getY1(), side.getX2(), side.getY2(), side.getPosition());
    }

    /**
     * Key action when a key is held pressed
     * @param e The key event that triggers key listener
     */
    public void keyPressed(KeyEvent e) {
        keyAction(e);
    }

    /**
     * Key action when a key is typed
     * @param e The key event that triggers key listener
     */
    public void keyTyped(KeyEvent e) {
    	keyAction(e);
    }

    /**
     * Key action when a key is released
     * @param e The key event that triggers key listener
     */
    public void keyReleased(KeyEvent e) {
        keyAction(e);
    }

    /**
     * Reads the key, and performs a movement
     * @param e
     */
    public void keyAction(KeyEvent e) {
        int id = e.getID();
        if (id == KeyEvent.VK_KP_LEFT) {
            super.getPaddle().moveLeft();
        } else if (id == KeyEvent.VK_KP_RIGHT) {
            super.getPaddle().moveRight();
        } else {
            ;
        }
    }

    public void move(KeyEvent e) {
        this.keyPressed(e);
        this.keyReleased(e);
    }

    public String getName() {
        return name;
    }
    
    public void killPlayer()
    {
        setSideStatus(Status.VACANT);
    }
}
