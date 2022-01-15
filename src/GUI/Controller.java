package GUI;
// PLACEHOLDER

import javax.swing.*;
import com.formdev.flatlaf.*;

import DAO.BibliographicReferenceDAO;
import DAO.CategoryDAOPostgreSQL;
import Entities.*;
import Entities.References.*;
import GUI.Homepage.*;

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
        try {
            homepage.setVisible(true);
            loginFrame.setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
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

