package utilities;

import java.awt.Color;

public final class Constants {
	public static final int PORT = 4444;
	public static final int BROADCAST_PORT = 4443;
	public static final int BALL_PORT = 4445;
	public static final double LINE_ADJUSTMENT = 4.0;
	
	public static final Color[] colors = {Color.BLACK, Color.GREEN, Color.BLUE, Color.PINK,
		Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.RED, Color.WHITE, Color.YELLOW};

	
	public static boolean retro = false;
	public static boolean ballEpilepsy = false;
	public static boolean textEpilepsy = false;
	public static boolean backgroundEpilepsy = false;
	
	public static int startingLives = 10;
	public static String name = "";
}
