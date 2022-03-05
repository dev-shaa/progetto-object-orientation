package GUI;

import Controller.Controller;
import Entities.*;
import Exceptions.Input.InvalidInputException;
import Utilities.MessageDisplayer;
import io.codeworth.panelmatic.PanelMatic;
import io.codeworth.panelmatic.componentbehavior.Modifiers;
import io.codeworth.panelmatic.util.Groupings;
import javax.swing.*;

/**
 * Pagina di accesso dell'applicazione.
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
		setSize(450, 300);
		setLocationRelativeTo(null);
		setResizable(false);

		usernameField = new JTextField(User.NAME_MAX_LENGTH);
		passwordField = new JPasswordField(User.PASSWORD_MAX_LENGTH);

		JButton registerButton = new JButton("Registrati");
		registerButton.addActionListener(e -> register());

		JButton loginButton = new JButton("Accedi");
		loginButton.addActionListener(e -> login());

		JPanel contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(80, 50, 20, 50));
		setContentPane(contentPane);

		PanelMatic.begin(contentPane)
				.add("Nome", usernameField)
				.addFlexibleSpace()
				.add("Password", passwordField)
				.add(Groupings.lineGroup(registerButton, loginButton), Modifiers.GROW, Modifiers.L_END, Modifiers.P_FEET)
				.get();
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