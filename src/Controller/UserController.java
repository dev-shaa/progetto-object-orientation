package Controller;

import DAO.UserDAO;
import Entities.User;
import Exceptions.Database.UserDatabaseException;

/**
 * Controller per gestire il recupero e l'inserimento di utenti.
 */
public class UserController {

	private UserDAO userDAO;

	/**
	 * Crea un nuovo controller con la classe DAO indicata.
	 * 
	 * @param userDAO
	 *            classe DAO degli utenti
	 * @throws IllegalArgumentException
	 *             se {@code userDAO == null}
	 */
	public UserController(UserDAO userDAO) {
		setUserDAO(userDAO);
	}

	/**
	 * Imposta la classe DAO per recuperare e salvare gli utenti nel database.
	 * 
	 * @param userDAO
	 *            classe DAO degli utenti
	 * @throws IllegalArgumentException
	 *             se {@code userDAO == null}
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

}
