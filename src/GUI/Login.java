package GUI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import DAO.UserDAO;
import Entities.User;

public class Login {

	public boolean Register(String username, String pwd) {

		boolean result = false;
		User user = new User(username, pwd);

		if (!username.isEmpty() && !pwd.isEmpty()) {
			System.out.println(username + " " + pwd);
			UserDAO userDao = new UserDAO();
			boolean esito = userDao.ExistUser(user);
			if (!esito) {
				userDao.SaveUser(user);
				result = true;
			} else {
				String message = "Utente già registrato";
				JOptionPane.showMessageDialog(new JFrame(), message, "Errore", JOptionPane.ERROR_MESSAGE);
				result = false;
			}
		}

		else {
			String message = "Nome Utente o Password sbagliati";
			JOptionPane.showMessageDialog(new JFrame(), message, "Errore", JOptionPane.ERROR_MESSAGE);
			result = false;
		}

		return result;
	}

	public boolean CheckLogin(User user) {
		if (user.getName().isEmpty() || user.getPassword().isEmpty()) {
			String message = "Nome Utente o Password sbagliati";
			JOptionPane.showMessageDialog(new JFrame(), message, "Errore", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		UserDAO userDao = new UserDAO();
		User userDB = userDao.GetUserLogin(user);
		
		if(userDB == null) {
			System.out.println("Utente null");
			return false;
		}
		
		if (user.getName().equals(userDB.getName()) && user.getPassword().equals(userDB.getPassword())) {
			System.out.println("Utente loggato");
			return true;
		}
		else {
			String message = "Nome Utente o Password sbagliati";
			JOptionPane.showMessageDialog(new JFrame(), message, "Errore", JOptionPane.ERROR_MESSAGE);
			return false;
		}

	}

}
