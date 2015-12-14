package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import pong.Pong;
import utilities.Constants;
import communicator.Server;

public class ServerUI extends JFrame {
	private static final long serialVersionUID = 1019782401351695809L;
	private JPanel contentPane;
	private Pong p = null;
	private Server s = null;
	public static DefaultListModel listModel;
	private JList list;	
	public static JTextField livesTextField;
	
	public ServerUI() {
		setTitle("Pong Legacy");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 500);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLives = new JLabel("Lives:");
		lblLives.setBounds(47, 290, 33, 20);
		contentPane.add(lblLives);
		
		livesTextField = new JTextField();
		livesTextField.setBounds(80, 290, 86, 20);
		contentPane.add(livesTextField);
		livesTextField.setColumns(10);
		
		s = new Server();
		p = new Pong(s);
		new Thread(new InitializePong()).start();
		
		JButton btnStartGame = new JButton("Start Game");
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(s.getNumClients() == 0){
					JOptionPane.showMessageDialog(getParent(), "Have you thought about getting some friends?");
					return;
				}
				try{
					int lives = Integer.parseInt(livesTextField.getText());
					if(lives>0){
						Constants.startingLives = lives;
					} else {
						System.out.println("using default lives");
					}
				} catch (NumberFormatException nfe){
					System.out.println("using default lives");
				}
				p.nowReady();
				setVisible(false);
			}
		});
		btnStartGame.setBounds(47, 400, 200, 25);
		contentPane.add(btnStartGame);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnExit.setBounds(117, 436, 60, 25);
		contentPane.add(btnExit);
		
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBounds(47, 50, 200, 225);
		contentPane.add(list);
		
	}

	public class InitializePong implements Runnable{
		public void run() {
			p.initServer();
		}
	}
}
