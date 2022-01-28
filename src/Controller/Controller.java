package Controller;

import javax.swing.*;
import com.formdev.flatlaf.*;

import DAO.AuthorDAO;
import DAO.BibliographicReferenceDAOPostgreSQL;
import DAO.CategoryDAOPostgreSQL;
import Entities.*;
import GUI.LoginFrame;
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
            CategoryController categoryController = new CategoryController(new CategoryDAOPostgreSQL(user));
            ReferenceController referenceController = new ReferenceController(new BibliographicReferenceDAOPostgreSQL(user));
            AuthorController authorController = new AuthorController(new AuthorDAO());

            homepage = new Homepage(this, categoryController, referenceController, authorController, user);
            homepage.setVisible(true);
            loginFrame.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

}
