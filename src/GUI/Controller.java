package GUI;
// PLACEHOLDER

import javax.swing.*;
import com.formdev.flatlaf.*;
import Entities.*;
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
            // vab, ci faremo bastare il look di default ¯\_(ツ)_/¯
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
            homepage = new Homepage(this, user);
            homepage.setVisible(true);
            loginFrame.setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
