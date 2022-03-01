package Utilities;

import javax.swing.JOptionPane;

/**
 * TODO: commenta
 */
public class MessageDisplayer {

    /**
     * Mostra un messaggio di errore.
     * 
     * @param title
     *            titolo della finestra di dialogo
     * @param message
     *            messaggio da mostrare
     */
    public static void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
