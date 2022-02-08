package Controller;

import DAO.UserDAO;
import Entities.User;
import Exceptions.UserDatabaseException;

public class UserController {

	private UserDAO userDAO;

	public UserController(UserDAO userDAO) {
		setUserDAO(userDAO);
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void register(User user) throws UserDatabaseException {
		getUserDAO().register(user);
	}

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
