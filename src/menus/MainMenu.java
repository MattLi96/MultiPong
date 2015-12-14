package menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.border.*;

import utilities.Constants;

public class MainMenu extends JFrame {
	private static final long serialVersionUID = 1667157157626059967L;
	public static JTextField nameTextField;

	public MainMenu() {
		setTitle("Pong Legacy || Version 1.0");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 500);
		setLocationRelativeTo(null);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(121, 45, 52, 14);
		contentPane.add(lblUsername);
		nameTextField = new JTextField();
		nameTextField.setBounds(104, 67, 86, 20);
		contentPane.add(nameTextField);
		nameTextField.setColumns(10);

		JButton btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (nameTextField.getText().length() < 3) {
					JOptionPane.showMessageDialog(getParent(), "Please input a valid username (At least 3 characters)");
				} else {
					// new Statistics(textField.getText()); Move this elsewhere.
					// Work on it when implementing chat client.
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								Constants.name = nameTextField.getText();
								ServerUI frame = new ServerUI();
								frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					setVisible(false);
				}
			}
		});
		btnStartServer.setBounds(97, 364, 100, 25);
		contentPane.add(btnStartServer);

		JButton btnStartClient = new JButton("Start Client");
		btnStartClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (nameTextField.getText().length() < 3) {
					JOptionPane.showMessageDialog(getParent(), "Please input a valid username (Longer than 3 characters)");
				} else {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								Constants.name = nameTextField.getText();
								ClientUI frame = new ClientUI();
								frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					setVisible(false);
				}
			}
		});
		btnStartClient.setBounds(97, 400, 100, 25);
		contentPane.add(btnStartClient);

		JButton btnOptions = new JButton("Options");
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							OptionsUI frame = new OptionsUI();
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		btnOptions.setBounds(37, 436, 100, 25);
		contentPane.add(btnOptions);

		JButton btnQuitGame = new JButton("Quit Game");
		btnQuitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnQuitGame.setBounds(157, 436, 100, 25);
		contentPane.add(btnQuitGame);

		setVisible(true);
	}
}