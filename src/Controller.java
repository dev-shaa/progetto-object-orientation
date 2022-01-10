// PLACEHOLDER

import javax.swing.*;
import com.formdev.flatlaf.*;

public class Controller {

    private LoginFrame loginFrame;
    private Homepage homepage;

    public Controller() {
        setupLookAndFeel();

        loginFrame = new LoginFrame(this);

        openLoginPage();
    }

    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public void openLoginPage() {
        loginFrame.setVisible(true);

        if (homepage != null)
            homepage.setVisible(false);
    }

    public void openHomePage(User user) {
        loginFrame.setVisible(false);

        try {
            homepage = new Homepage(this, user);
            homepage.setVisible(true);
        } catch (Exception e) {
            // TODO: mostra schermata di errore
        }
    }

    public void openReferenceCreatorPage() {
        loginFrame.setVisible(false);
        homepage.setVisible(false);

        // TODO:
    }

    public void openReferenceCreatorPage(BibliographicReference riferimento) {
        // TODO:
    }

}
