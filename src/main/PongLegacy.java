package main;

import javax.swing.UIManager;

import menus.MainMenu;

/**
 * <a href="http://java.sun.com/docs/books/tutorial/deployment/jar/appman.html">
 * Main-Class</a> of the <em>Pong Legacy</em> project with the <tt>public static 
 * void main(String...</tt> method as the entry point.
 * 
 * @author 2009-2010 WHS <a
 *         href="http://winchester.k12.ma.us/~dpetty/apcs/">APCS</a> class
 */
public class PongLegacy {
	/**
	 * <tt>PongLegacy</tt> main method, the <a href=
	 * "http://java.sun.com/docs/books/tutorial/deployment/jar/appman.html">
	 * Main-Class</a> entry point of the <em>Pong Legacy</em> project.
	 * 
	 * @param args
	 *            command-line arguments
	 */
	public static void main(String... args) {
		for (String string : args) {
			System.out.print(string + " ");
		}

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		new MainMenu();
	}
}