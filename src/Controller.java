// PLACEHOLDER

import javax.swing.*;
import com.formdev.flatlaf.*;

public class Controller {

    public Controller() {
        setupLookAndFeel();

        User user = new User("Nuovo Utente");

        MainWindow mainWindow = new MainWindow(this, user);
        mainWindow.setVisible(true);
    }

    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Impossibile inizializzare FlatLightLaf");
        }
    }

    public void logout() {
        // TODO: implementa
    }
}
