package GUI;

import Entities.*;
import Exceptions.UserDatabaseException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controller.Controller;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TODO: commenta
 */
public class LoginPage extends JFrame {

	private Controller controller;

	private JTextField usernameField;
	private JPasswordField passwordField;

	/**
	 * TODO: commenta
	 * 
	 * @param controller
	 */
	public LoginPage(Controller controller) {
		setController(controller);

		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		usernameField = new JTextField();
		usernameField.setBounds(140, 64, 250, 24);
		contentPane.add(usernameField);

		passwordField = new JPasswordField();
		passwordField.setBounds(140, 143, 250, 24);
		contentPane.add(passwordField);

		JLabel usernameLabel = new JLabel("Nome Utente", JLabel.RIGHT);
		usernameLabel.setBounds(34, 64, 96, 24);
		contentPane.add(usernameLabel);

		JLabel passwordLabel = new JLabel("Password", JLabel.RIGHT);
		passwordLabel.setBounds(34, 143, 96, 24);
		contentPane.add(passwordLabel);

		JButton registerButton = new JButton("Registrati");
		registerButton.setBounds(214, 213, 85, 24);
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				register();
			}
		});
		contentPane.add(registerButton);

		JButton accessButton = new JButton("Accedi");
		accessButton.setBounds(330, 213, 85, 24);
		accessButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		contentPane.add(accessButton);
	}

	/**
	 * TODO: commenta
	 * 
	 * @param controller
	 * @throws IllegalArgumentException
	 *             se {@code controller == null}
	 */
	public void setController(Controller controller) {
		if (controller == null)
			throw new IllegalArgumentException("controller can't be null");

		this.controller = controller;
	}

	/**
	 * TODO: commenta
	 * 
	 * @return
	 */
	public Controller getController() {
		return controller;
	}

	private void register() {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());

		try {
			User user = new User(username, password);

			getController().getUserController().register(user);
			getController().openHomePage(user);
		} catch (IllegalArgumentException | UserDatabaseException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore registrazione utente", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void login() {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());

		try {
			User user = new User(username, password);

			if (getController().getUserController().login(user))
				getController().openHomePage(user);
			else
				JOptionPane.showMessageDialog(this, "Impossibile accedere: nome o password errati.", "Errore accesso utente", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException | UserDatabaseException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore accesso utente", JOptionPane.ERROR_MESSAGE);
		}
	}

}
