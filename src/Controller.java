// PLACEHOLDER

import javax.swing.*;
import com.formdev.flatlaf.*;

public class Controller {

    private Homepage homepage;

    public Controller() {
        setupLookAndFeel();

        try {
            User user = new User("Nuovo Utente");

            CategoryDAO categoryDAO = new CategoryDAOPostgreSQL(user);
            BibliographicReferenceDAO bibliographicReferenceDAO = new BibliographicReferenceDAO();

            homepage = new Homepage(this, user, categoryDAO, bibliographicReferenceDAO);
            openHomePage();
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (CategoryDatabaseException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    public void openHomePage() {
        homepage.setVisible(true);
    }

    public void openReferenceCreatorPage() {
        // TODO:
    }

    public void openReferenceCreatorPage(BibliographicReference riferimento) {
        // TODO:
    }

    public void logout() {
        // TODO: implementa
    }

}
