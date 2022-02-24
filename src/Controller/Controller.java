package Controller;

import java.util.Collection;

import javax.swing.*;
import com.formdev.flatlaf.*;

import DAO.*;
import GUI.*;
import GUI.References.Editor.*;
import GUI.Utilities.Tree.CustomTreeModel;
import Repository.CategoryRepository;
import Repository.ReferenceRepository;
import Entities.*;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.*;
import Entities.References.OnlineResources.Image;
import Entities.References.PhysicalResources.*;
import Exceptions.Database.CategoryDatabaseException;
import Exceptions.Database.DatabaseException;
import Exceptions.Database.ReferenceDatabaseException;
import Exceptions.Database.UserDatabaseException;

/**
 * Controller dell'applicazione.
 */
public class Controller {

    private LoginPage loginPage;
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

        loginPage = new LoginPage(this);
        homepage = new Homepage(this);

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

    private ReferenceRepository getReferenceRepository() {
        return referenceRepository;
    }

    private CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    private void setUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("user can't be null");

        this.user = user;

        setupRepositories();
    }

    /**
     * Restituisce l'utente che ha eseguito l'accesso.
     * 
     * @return utente che ha eseguito l'accesso
     */
    public User getUser() {
        return user;
    }

    private void setupRepositories() {
        CategoryDAO categoryDAO = new CategoryDAOPostgreSQL(getUser());
        BibliographicReferenceDAO referenceDAO = new BibliographicReferenceDAOPostgreSQL(getUser());
        AuthorDAO authorDAO = new AuthorDAOPostgreSQL();
        TagDAO tagDAO = new TagDAOPostgreSQL();

        categoryRepository = new CategoryRepository(categoryDAO);
        referenceRepository = new ReferenceRepository(referenceDAO, authorDAO, tagDAO, categoryRepository);
    }

    /**
     * Apre la pagina di login.
     */
    public void openLoginPage() {
        loginPage.setVisible(true);
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

        loginPage.setVisible(false);

        try {
            CustomTreeModel<Category> categoriesTree = getCategoryRepository().getTree();
            Collection<? extends BibliographicReference> references = getReferenceRepository().getAll();

            homepage.setTreeModel(categoriesTree);
            homepage.setReferences(references);

            homepage.setNameToDisplay(user.getName());
            homepage.setVisible(true);
        } catch (DatabaseException e) {
            // TODO: handle exception
        }

    }

    /**
     * Apre l'editor di riferimenti per articoli.
     * 
     * @param article
     *            articolo da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openArticleEditor(Article article) {
        if (articleEditor == null) {
            articleEditor = new ArticleEditor(null, null);

            articleEditor.addReferenceCreationListener(new ReferenceEditorListener<Article>() {

                @Override
                public void onReferenceCreation(Article newReference) {
                    try {
                        getReferenceRepository().save(newReference);
                        articleEditor.setVisible(false);
                        homepage.reloadReferences();
                    } catch (IllegalArgumentException | ReferenceDatabaseException e) {
                        articleEditor.showErrorMessage("Salvataggio non riuscito", e.getMessage());
                    }
                }

            });
        }

        openReferenceEditor(articleEditor, article);
    }

    /**
     * Apre l'editor di riferimenti per libri.
     * 
     * @param book
     *            libro da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openBookEditor(Book book) {
        if (bookEditor == null) {
            bookEditor = new BookEditor(null, null);

            bookEditor.addReferenceCreationListener(new ReferenceEditorListener<Book>() {

                @Override
                public void onReferenceCreation(Book newReference) {
                    try {
                        getReferenceRepository().save(newReference);
                        bookEditor.setVisible(false);
                    } catch (IllegalArgumentException | ReferenceDatabaseException e) {
                        bookEditor.showErrorMessage("Salvataggio non riuscito", e.getMessage());
                    }
                }

            });
        }

    }

    /**
     * Apre l'editor di riferimenti per tesi.
     * 
     * @param thesis
     *            tesi da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openThesisEditor(Thesis thesis) {
        if (thesisEditor == null) {
            thesisEditor = new ThesisEditor(null, null);

            thesisEditor.addReferenceCreationListener(new ReferenceEditorListener<Thesis>() {

                @Override
                public void onReferenceCreation(Thesis newReference) {
                    try {
                        getReferenceRepository().save(newReference);
                        articleEditor.setVisible(false);
                    } catch (IllegalArgumentException | ReferenceDatabaseException e) {
                        // FIXME:
                        // articleEditor.showErrorMessage("Salvataggio non riuscito", e.getMessage());
                    }
                }

            });
        }

    }

    /**
     * Apre l'editor di riferimenti per codice sorgente.
     * 
     * @param sourceCode
     *            codice sorgente da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openSourceCodeEditor(SourceCode sourceCode) {
        // if (sourceCodeEditor == null)
        // sourceCodeEditor = new SourceCodeEditor(getCategoryRepository(), getReferenceRepository());

        // sourceCodeEditor.setVisible(true, sourceCode);
    }

    /**
     * Apre l'editor di riferimenti per immagini.
     * 
     * @param image
     *            immagine da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openImageEditor(Image image) {
        // if (imageEditor == null)
        // imageEditor = new ImageEditor(getCategoryRepository(), getReferenceRepository());

        // imageEditor.setVisible(true, image);
    }

    /**
     * Apre l'editor di riferimenti per video.
     * 
     * @param video
     *            video da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openVideoEditor(Video video) {
        // if (videoEditor == null)
        // videoEditor = new VideoEditor(getCategoryRepository(), getReferenceRepository());

        // videoEditor.setVisible(true, video);
    }

    /**
     * Apre l'editor di riferimenti per siti web.
     * 
     * @param website
     *            sito web da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openWebsiteEditor(Website website) {
        // if (websiteEditor == null)
        // websiteEditor = new WebsiteEditor(getCategoryRepository(), getReferenceRepository());

        // websiteEditor.setVisible(true, website);
    }

    private <T extends BibliographicReference> void openReferenceEditor(ReferenceEditor<T> editor, T referenceToChange) {
        try {
            CustomTreeModel<Category> categoriesTree = getCategoryRepository().getTree();
            Collection<? extends BibliographicReference> references = getReferenceRepository().getAll();

            editor.setCategoriesTree(categoriesTree);
            editor.setReferences(references);
            editor.setReferenceToChange(referenceToChange);
            editor.setVisible(true);
        } catch (DatabaseException e) {
            // TODO: handle exception
        }
    }

    /**
     * FIXME:
     * 
     * @param category
     */
    public void addCategory(Category category) {
        try {
            getCategoryRepository().save(category);
        } catch (IllegalArgumentException | CategoryDatabaseException e) {
            homepage.showErrorMessage("Errore salvataggio categoria", e.getMessage());
        }
    }

    public void updateCategory(Category category, String newName) {
        try {
            getCategoryRepository().update(category, newName);
        } catch (CategoryDatabaseException | IllegalArgumentException e) {
            homepage.showErrorMessage("Errore modifica categoria", e.getMessage());
        }
    }

    public void removeCategory(Category category) {
        try {
            getCategoryRepository().remove(category);
            getReferenceRepository().forceNextRetrievalFromDatabase();
        } catch (CategoryDatabaseException e) {
            homepage.showErrorMessage("Errore rimozione categoria", e.getMessage());
        }
    }

    public void removeReference(BibliographicReference reference) {
        try {
            getReferenceRepository().remove(reference);
            homepage.reloadReferences();
        } catch (ReferenceDatabaseException e) {
            homepage.showErrorMessage("Errore rimozione riferimento", e.getMessage());
        }
    }

}
