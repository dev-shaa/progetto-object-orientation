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
import Exceptions.Database.UserDatabaseException;

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

    /**
     * Crea un nuovo controller.
     */
    public Controller() {
        setupLookAndFeel();
        openLoginPage();
    }

    /**
     * Restituisce il repository dei riferimenti.
     * 
     * @return repository dei riferimenti
     */
    public ReferenceRepository getReferenceRepository() {
        return referenceRepository;
    }

    /**
     * Restituisce il repository delle categorie.
     * 
     * @return repository delle categorie
     */
    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    private void setUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("user can't be null");

        this.user = user;

        CategoryDAO categoryDAO = new CategoryDAOPostgreSQL(getUser());
        BibliographicReferenceDAO referenceDAO = new BibliographicReferenceDAOPostgreSQL(getUser());
        AuthorDAO authorDAO = new AuthorDAOPostgreSQL();
        TagDAO tagDAO = new TagDAOPostgreSQL();

        categoryRepository = new CategoryRepository(categoryDAO);
        referenceRepository = new ReferenceRepository(referenceDAO, authorDAO, tagDAO, categoryRepository);
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
     * TODO: commenta
     * 
     * @param user
     * @throws UserDatabaseException
     */
    public void registerUser(User user) throws UserDatabaseException {
        UserDAO userDAO = new UserDAOPostgreSQL();
        userDAO.register(user);
        openHomePage(user);
    }

    /**
     * TODO: commenta
     * 
     * @param user
     * @return
     * @throws UserDatabaseException
     */
    public boolean login(User user) throws UserDatabaseException {
        UserDAO userDAO = new UserDAOPostgreSQL();

        if (!userDAO.doesUserExist(user))
            return false;

        openHomePage(user);

        return true;
    }

    private void openHomePage(User user) {
        setUser(user);

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
            articleEditor = new ArticleEditor(getCategoryRepository(), getReferenceRepository());

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
            bookEditor = new BookEditor(getCategoryRepository(), getReferenceRepository());

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
            thesisEditor = new ThesisEditor(getCategoryRepository(), getReferenceRepository());

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
            sourceCodeEditor = new SourceCodeEditor(getCategoryRepository(), getReferenceRepository());

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
            imageEditor = new ImageEditor(getCategoryRepository(), getReferenceRepository());

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
            videoEditor = new VideoEditor(getCategoryRepository(), getReferenceRepository());

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
            websiteEditor = new WebsiteEditor(getCategoryRepository(), getReferenceRepository());

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
