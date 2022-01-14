import javax.swing.SwingUtilities;

import GUI.Controller;
/**
 * Classe per avviare il programma.
 */
public class Driver {
    public static void main(String[] args) {
    	Connesione sqlConnect = new Connesione();
    	sqlConnect.connect();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Controller();
            }
        });
    }
}
