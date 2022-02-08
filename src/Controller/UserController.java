package Controller;

import DAO.UserDAO;
import Entities.User;
import Exceptions.UserDatabaseException;

/**
 * Controller per gestire il recupero e l'inserimento di utenti.
 */
public class UserController {

	private UserDAO userDAO;

	public UserController(UserDAO userDAO) {
		setUserDAO(userDAO);
	}

	/**
	 * Imposta la classe DAO per recuperare e salvare gli utenti nel database.
	 * 
	 * @param userDAO
	 *            classe DAO degli utenti
	 */
	public void setUserDAO(UserDAO userDAO) {
		if (userDAO == null)
			throw new IllegalArgumentException("userDAO can't be null");

		this.userDAO = userDAO;
	}

	/**
	 * Restituisce la classe DAO usata per recuperare e salvare utenti nel database.
	 * 
	 * @return classe DAO degli utenti
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * Registra un utente.
	 * 
	 * @param user
	 *            utente da registrare
	 * @throws UserDatabaseException
	 *             se il salvataggio non va a buon fine
	 */
	public void register(User user) throws UserDatabaseException {
		getUserDAO().register(user);
	}

	/**
	 * Controlla se l'utente è valido per eseguire un login.
	 * 
	 * @param user
	 *            utente che tenta di eseguire il login
	 * @return {@code true} se le credenziali dell'utente sono corrette
	 * @throws UserDatabaseException
	 *             se il controllo delle credenziali non va a buon fine
	 */
	public boolean login(User user) throws UserDatabaseException {
		return getUserDAO().doesUserExist(user);
	}

	// public boolean register(String username, String password) {

	// boolean result = false;
	// User user = new User(username, password);

	// if (!username.isEmpty() && !password.isEmpty()) {
	// System.out.println(username + " " + password);
	// UserDAOPostgreSQL userDao = new UserDAOPostgreSQL();
	// boolean esito = userDao.ExistUser(user);
	// if (!esito) {
	// userDao.register(user);
	// result = true;
	// } else {
	// String message = "Utente gi� registrato";
	// JOptionPane.showMessageDialog(new JFrame(), message, "Errore", JOptionPane.ERROR_MESSAGE);
	// result = false;
	// }
	// }

	// else {
	// String message = "Nome Utente o Password sbagliati";
	// JOptionPane.showMessageDialog(new JFrame(), message, "Errore", JOptionPane.ERROR_MESSAGE);
	// result = false;
	// }

	// return result;
	// }

	// public boolean CheckLogin(User user) {
	// if (user.getName().isEmpty() || user.getPassword().isEmpty()) {
	// String message = "Nome Utente o Password sbagliati";
	// JOptionPane.showMessageDialog(new JFrame(), message, "Errore", JOptionPane.ERROR_MESSAGE);
	// return false;
	// }

	// UserDAOPostgreSQL userDao = new UserDAOPostgreSQL();
	// User userDB = userDao.GetUserLogin(user);
	// if (userDB == null) {
	// System.out.println("Utente null");
	// return false;
	// }
	// if (user.getName().equals(userDB.getName()) && user.getPassword().equals(userDB.getPassword())) {
	// System.out.println("Utente loggato");
	// return true;
	// } else {
	// String message = "Nome Utente o Password sbagliati";
	// JOptionPane.showMessageDialog(new JFrame(), message, "Errore", JOptionPane.ERROR_MESSAGE);
	// return false;
	// }

	// }

}
