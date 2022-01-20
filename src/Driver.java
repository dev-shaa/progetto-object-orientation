import javax.swing.SwingUtilities;

import DAO.UserDAO;
import Entities.Author;
import Entities.User;
import GUI.Controller;
/**
 * Classe per avviare il programma.
 */
public class Driver {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Controller();
            }
        });
    }
}

