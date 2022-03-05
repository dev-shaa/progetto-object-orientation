import javax.swing.SwingUtilities;
import Controller.Controller;

/**
 * Classe per avviare il programma.
 */
public class Driver {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Controller());
    }
}