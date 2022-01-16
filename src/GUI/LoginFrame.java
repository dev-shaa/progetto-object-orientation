package GUI;

import Entities.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DropMode;
import javax.swing.JFormattedTextField;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField userName_TF;
	private JPasswordField passwordField;
	private Controller theController;

	/**
	 * Create the frame.
	 */

	public LoginFrame(Controller c) {
//		theController = c;
		
		Login login = new Login();

		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		userName_TF = new JTextField();
		userName_TF.setBounds(140, 64, 163, 19);
		contentPane.add(userName_TF);

		passwordField = new JPasswordField();
		passwordField.setBounds(140, 143, 163, 19);
		contentPane.add(passwordField);

		JButton btnAccedi = new JButton("Accedi");
		btnAccedi.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnAccedi.setBounds(330, 213, 85, 29);
		contentPane.add(btnAccedi);
		btnAccedi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = userName_TF.getText();
				String pwd = new String(passwordField.getPassword());
				
				// FIXME: DEBUG:
				c.openHomePage(new User(username));
			}
		});

		JLabel lblNewLabel = new JLabel("Nome Utente");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(34, 52, 96, 39);
		contentPane.add(lblNewLabel);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPassword.setBounds(34, 131, 96, 39);
		contentPane.add(lblPassword);

		JButton btnRegistrati = new JButton("Registrati");
		btnRegistrati.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnRegistrati.setBounds(214, 213, 85, 29);
		contentPane.add(btnRegistrati);
		btnRegistrati.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = userName_TF.getText();
				String pwd = new String(passwordField.getPassword());
				login.CheckUser(username, pwd);
			}
		});
	}
}

