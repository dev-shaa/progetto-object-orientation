import javax.swing.SwingUtilities;

import DAO.UserDAO;
import Entities.User;
import GUI.Controller;
/**
 * Classe per avviare il programma.
 */
public class Driver {
    public static void main(String[] args) {
//    	User user = new User("Nome", "Password");
//    	UserDAO userDAO = new UserDAO();
//    	userDAO.SaveUser(user);
    	
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Controller();
            }
        });
    }
}

