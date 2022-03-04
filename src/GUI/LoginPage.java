package GUI;

import Controller.Controller;
import Entities.*;
import Exceptions.Input.InvalidInputException;
import Utilities.MessageDisplayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Pagina di accesso dell'applicativo.
 * <p>
 * Offre la possibilità di registrarsi o accedere.
 */
public class LoginPage extends JFrame {

	private Controller controller;
	private JTextField usernameField;
	private JPasswordField passwordField;

	/**
	 * Crea una nuova pagina di accesso.
	 * 
	 * @param controller
	 *            controller della GUI
	 * @throws IllegalArgumentException
	 *             se {@code controller == null}
	 */
	public LoginPage(Controller controller) {
		setController(controller);
		setup();
	}

	/**
	 * Imposta il controller della GUI.
	 * 
	 * @param controller
	 *            controller della GUI
	 * @throws IllegalArgumentException
	 *             se {@code controller == null}
	 */
	public void setController(Controller controller) {
		if (controller == null)
			throw new IllegalArgumentException("controller can't be null");

		this.controller = controller;
	}

	@Override
	public void setVisible(boolean b) {
		if (b)
			clear();

		super.setVisible(b);
	}

	private void setup() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		setupUsernameField();
		setupPasswordField();
		setupButtons();
	}

	private void setupUsernameField() {
		JLabel usernameLabel = new JLabel("Nome Utente", JLabel.RIGHT);
		usernameLabel.setBounds(34, 64, 96, 24);
		getContentPane().add(usernameLabel);

		usernameField = new JTextField();
		usernameField.setBounds(140, 64, 250, 24);
		getContentPane().add(usernameField);
	}

	private void setupPasswordField() {
		JLabel passwordLabel = new JLabel("Password", JLabel.RIGHT);
		passwordLabel.setBounds(34, 143, 96, 24);
		getContentPane().add(passwordLabel);

		passwordField = new JPasswordField();
		passwordField.setBounds(140, 143, 250, 24);
		getContentPane().add(passwordField);
	}

	private void setupButtons() {
		JButton registerButton = new JButton("Registrati");
		registerButton.setBounds(214, 213, 85, 24);
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				register();
			}
		});
		getContentPane().add(registerButton);

		JButton accessButton = new JButton("Accedi");
		accessButton.setBounds(330, 213, 85, 24);
		accessButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		getContentPane().add(accessButton);
	}

	private void clear() {
		usernameField.setText(null);
		passwordField.setText(null);
	}

	private void register() {
		try {
			User user = getUserFromFields();
			controller.register(user);
		} catch (InvalidInputException e) {
			MessageDisplayer.showErrorMessage("Errore registrazione", e.getMessage());
		}
	}

	private void login() {
		try {
			User user = getUserFromFields();
			controller.login(user);
		} catch (InvalidInputException e) {
			MessageDisplayer.showErrorMessage("Errore accesso", e.getMessage());
		}
	}

	private User getUserFromFields() throws InvalidInputException {
		try {
			String username = usernameField.getText();
			String password = new String(passwordField.getPassword());
			return new User(username, password);
		} catch (IllegalArgumentException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

}