package Controller;

import javax.swing.*;
import com.formdev.flatlaf.*;

import DAO.*;
import GUI.*;
import GUI.References.Editor.*;
import Repository.CategoryRepository;
import Repository.ReferenceRepository;
import Entities.*;
import Entities.References.OnlineResources.*;
import Entities.References.OnlineResources.Image;
import Entities.References.PhysicalResources.*;

/**
 * Controller dell'applicazione.
 */
public class Controller {

    private LoginPage loginFrame;
    private Homepage homepage;

    private ArticleEditor articleEditor;
    private BookEditor bookEditor;
    private ThesisEditor thesisEditor;
    private ImageEditor imageEditor;
    private SourceCodeEditor sourceCodeEditor;
    private VideoEditor videoEditor;
    private WebsiteEditor websiteEditor;

    private User user;
    private CategoryRepository categoryRepository;
    private ReferenceRepository referenceRepository;
    private UserController loginController;

    /**
     * Crea un nuovo controller.
     */
    public Controller() {
        setupLookAndFeel();
        openLoginPage();
    }

    /**
     * Restituisce il controller dei riferimenti.
     * 
     * @return controller dei riferimenti
     */
    public ReferenceRepository getReferenceController() {
        return referenceRepository;
    }

    /**
     * Restituisce il controller delle categorie.
     * 
     * @return controller delle categoria
     */
    public CategoryRepository getCategoryController() {
        return categoryRepository;
    }

    /**
     * Restituisce il controller degli utenti.
     * 
     * @return controller delle categoria
     */
    public UserController getUserController() {
        if (loginController == null)
            loginController = new UserController(new UserDAOPostgreSQL());

        return loginController;
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
        if (loginFrame == null)
            loginFrame = new LoginPage(this);

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
        this.user = user;

        CategoryDAO categoryDAO = new CategoryDAOPostgreSQL(getUser());
        BibliographicReferenceDAO referenceDAO = new BibliographicReferenceDAOPostgreSQL(getUser());
        AuthorDAO authorDAO = new AuthorDAOPostgreSQL();
        TagDAO tagDAO = new TagDAOPostgreSQL();

        categoryRepository = new CategoryRepository(categoryDAO);
        referenceRepository = new ReferenceRepository(referenceDAO, authorDAO, tagDAO, categoryRepository);

        if (homepage == null)
            homepage = new Homepage(this);

        loginFrame.setVisible(false);
        homepage.setVisible(true);
    }

    /**
     * Apre l'editor di riferimenti per articoli.
     * 
     * @param article
     *            articolo da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openArticleEditor(Article article) {
        if (articleEditor == null)
            articleEditor = new ArticleEditor(homepage, getCategoryController(), getReferenceController());

        articleEditor.setVisible(true, article);
    }

    /**
     * Apre l'editor di riferimenti per libri.
     * 
     * @param book
     *            libro da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openBookEditor(Book book) {
        if (bookEditor == null)
            bookEditor = new BookEditor(homepage, getCategoryController(), getReferenceController());

        bookEditor.setVisible(true, book);
    }

    /**
     * Apre l'editor di riferimenti per tesi.
     * 
     * @param thesis
     *            tesi da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openThesisEditor(Thesis thesis) {
        if (thesisEditor == null)
            thesisEditor = new ThesisEditor(homepage, getCategoryController(), getReferenceController());

        thesisEditor.setVisible(true, thesis);
    }

    /**
     * Apre l'editor di riferimenti per codice sorgente.
     * 
     * @param sourceCode
     *            codice sorgente da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openSourceCodeEditor(SourceCode sourceCode) {
        if (sourceCodeEditor == null)
            sourceCodeEditor = new SourceCodeEditor(homepage, getCategoryController(), getReferenceController());

        sourceCodeEditor.setVisible(true, sourceCode);
    }

    /**
     * Apre l'editor di riferimenti per immagini.
     * 
     * @param image
     *            immagine da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openImageEditor(Image image) {
        if (imageEditor == null)
            imageEditor = new ImageEditor(homepage, getCategoryController(), getReferenceController());

        imageEditor.setVisible(true, image);
    }

    /**
     * Apre l'editor di riferimenti per video.
     * 
     * @param video
     *            video da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openVideoEditor(Video video) {
        if (videoEditor == null)
            videoEditor = new VideoEditor(homepage, getCategoryController(), getReferenceController());

        videoEditor.setVisible(true, video);
    }

    /**
     * Apre l'editor di riferimenti per siti web.
     * 
     * @param website
     *            sito web da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openWebsiteEditor(Website website) {
        if (websiteEditor == null)
            websiteEditor = new WebsiteEditor(homepage, getCategoryController(), getReferenceController());

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
