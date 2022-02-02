package Controller;

import javax.swing.*;
import com.formdev.flatlaf.*;

import DAO.AuthorDAO;
import DAO.BibliographicReferenceDAOPostgreSQL;
import DAO.CategoryDAOPostgreSQL;
import Entities.*;

import GUI.*;
import GUI.Editor.Reference.*;
import GUI.Homepage.*;
import Entities.References.OnlineResources.*;
import Entities.References.OnlineResources.Image;
import Entities.References.PhysicalResources.*;

/**
 * Controller dell'applicazione.
 */
public class Controller {

    private LoginFrame loginFrame;
    private Homepage homepage;

    private ArticleEditor articleEditor;
    private BookEditor bookEditor;
    private ThesisEditor thesisEditor;
    private ImageEditor imageEditor;
    private SourceCodeEditor sourceCodeEditor;
    private VideoEditor videoEditor;
    private WebsiteEditor websiteEditor;

    private User user;
    private CategoryController categoryController;
    private ReferenceController referenceController;

    /**
     * Crea un nuovo controller.
     */
    public Controller() {
        setupLookAndFeel();

        loginFrame = new LoginFrame(this);

        openLoginPage();
    }

    /**
     * Restituisce il controller dei riferimenti.
     * 
     * @return controller dei riferimenti
     */
    public ReferenceController getReferenceController() {
        return referenceController;
    }

    /**
     * Restituisce il controller delle categorie.
     * 
     * @return controller delle categoria
     */
    public CategoryController getCategoryController() {
        return categoryController;
    }

    /**
     * Restituisce l'utente che ha eseguito l'accesso.
     * 
     * @return utente che ha eseguito l'accesso
     */
    public User getUser() {
        return user;
    }

    /**
     * Apre la pagina di login.
     */
    public void openLoginPage() {
        loginFrame.setVisible(true);

        if (homepage != null)
            homepage.setVisible(false);
    }

    /**
     * Apre la pagina principale dell'applicazione.
     * 
     * @param user
     *            utente che ha eseguito l'accesso
     */
    public void openHomePage(User user) {
        try {
            this.user = user;
            this.categoryController = new CategoryController(new CategoryDAOPostgreSQL(user));
            this.referenceController = new ReferenceController(new BibliographicReferenceDAOPostgreSQL(user));

            homepage = new Homepage(this);

            homepage.setVisible(true);
            loginFrame.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void openArticleEditor(Article article) {
        if (articleEditor == null)
            articleEditor = new ArticleEditor(homepage, categoryController, referenceController);

        articleEditor.setVisible(true, article);
    }

    public void openBookEditor(Book book) {
        if (bookEditor == null)
            bookEditor = new BookEditor(homepage, categoryController, referenceController);

        bookEditor.setVisible(true, book);
    }

    public void openThesisEditor(Thesis thesis) {
        if (thesisEditor == null)
            thesisEditor = new ThesisEditor(homepage, categoryController, referenceController);

        thesisEditor.setVisible(true, thesis);
    }

    public void openSourceCodeEditor(SourceCode sourceCode) {
        if (sourceCodeEditor == null)
            sourceCodeEditor = new SourceCodeEditor(homepage, categoryController, referenceController);

        sourceCodeEditor.setVisible(true, sourceCode);
    }

    public void openImageEditor(Image image) {
        if (imageEditor == null)
            imageEditor = new ImageEditor(homepage, categoryController, referenceController);

        imageEditor.setVisible(true, image);
    }

    public void openVideoEditor(Video video) {
        if (videoEditor == null)
            videoEditor = new VideoEditor(homepage, categoryController, referenceController);

        videoEditor.setVisible(true, video);
    }

    public void openWebsiteEditor(Website website) {
        if (websiteEditor == null)
            websiteEditor = new WebsiteEditor(homepage, categoryController, referenceController);

        websiteEditor.setVisible(true, website);
    }

    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            // vab, ci faremo bastare il look di default ¯\_(ツ)_/¯
            e.printStackTrace();
        }
    }

}
