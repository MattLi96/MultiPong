package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import pong.Pong;
import utilities.Constants;

import communicator.Client;


public class OptionsUI extends JFrame {
	private static final long serialVersionUID = 1019782401351695809L;
	private JPanel contentPane;
	private JRadioButton rdbtnRetro, rdbtnBallEpilepsy, rdbtnTextEpilepsy, rdbtnBackgroundEpilepsy;
	
	public OptionsUI() {
		setTitle("Pong Legacy");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 500);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		rdbtnRetro = new JRadioButton("Retro Graphics?", Constants.retro);
		rdbtnRetro.setBounds(66, 25, 161, 25);
		contentPane.add(rdbtnRetro);
		
		rdbtnBallEpilepsy = new JRadioButton("Epilepsy?", Constants.ballEpilepsy);
		rdbtnBallEpilepsy.setBounds(66, 50, 161, 25);
		contentPane.add(rdbtnBallEpilepsy);
		
		rdbtnTextEpilepsy = new JRadioButton("More Epilepsy??", Constants.textEpilepsy);
		rdbtnTextEpilepsy.setBounds(66, 75, 161, 25);
		contentPane.add(rdbtnTextEpilepsy);
		
		rdbtnBackgroundEpilepsy = new JRadioButton("Even More Epilepsy???", Constants.backgroundEpilepsy);
		rdbtnBackgroundEpilepsy.setBounds(66, 100, 161, 25);
		contentPane.add(rdbtnBackgroundEpilepsy);
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Constants.retro = rdbtnRetro.isSelected();
				Constants.ballEpilepsy = rdbtnBallEpilepsy.isSelected();
				Constants.textEpilepsy = rdbtnTextEpilepsy.isSelected();
				Constants.backgroundEpilepsy = rdbtnBackgroundEpilepsy.isSelected();
				
				setVisible(false);
			}
		});
		btnDone.setBounds(47, 400, 200, 25);
		contentPane.add(btnDone);
	}
}
