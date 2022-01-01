// PLACEHOLDER

import javax.swing.*;
import com.formdev.flatlaf.*;

public class Controller {

    private MainWindow homepage;

    public Controller() {
        setupLookAndFeel();

        User user = new User("Nuovo Utente");

        homepage = new MainWindow(this, user);

        openHomePage();
        // mainWindow.setVisible(true);
    }

    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Impossibile inizializzare FlatLightLaf");
        }
    }

    public void openHomePage() {
        homepage.setVisible(true);
    }

    public void openReferenceCreatorPage() {
        // TODO:
    }

    public void openReferenceCreatorPage(Riferimento riferimento) {
        // TODO:
    }

    public void logout() {
        // TODO: implementa
    }

}
