package Controller;

import javax.swing.*;
import com.formdev.flatlaf.*;
import DAO.*;
import GUI.*;
import GUI.References.ReferenceEditor;
import GUI.References.ReferenceEditorListener;
import GUI.References.Publication.*;
import GUI.References.OnlineResource.*;
import Repository.CategoryRepository;
import Repository.ReferenceRepository;
import Utilities.MessageDisplayer;
import Utilities.Functions.CheckedConsumer;
import Entities.*;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;

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

    private void setUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("user non può essere nullo.");

        this.user = user;
        setupRepositories();
    }

    private void setupRepositories() {
        CategoryDAO categoryDAO = new CategoryDAOPostgreSQL(user);
        BibliographicReferenceDAO referenceDAO = new BibliographicReferenceDAOPostgreSQL(user);
        AuthorDAO authorDAO = new AuthorDAOPostgreSQL();
        TagDAO tagDAO = new TagDAOPostgreSQL();

        categoryRepository = new CategoryRepository(categoryDAO);
        referenceRepository = new ReferenceRepository(referenceDAO, authorDAO, tagDAO, categoryRepository);
    }

    /**
     * Esegue la registrazione di un utente.
     * <p>
     * In caso di errore, mostra un messaggio nella pagina di login.
     * 
     * @param user
     *            utente che tenta di eseguire la registrazione
     */
    public void register(User user) {
        UserDAO userDAO = new UserDAOPostgreSQL();

        try {
            userDAO.register(user);
            openHomePage(user);
        } catch (Exception e) {
            MessageDisplayer.showErrorMessage("Errore registrazione", e.getMessage());
        }
    }

    /**
     * Esegue l'accesso per un utente.
     * <p>
     * In caso di errore, mostra un messaggio nella pagina di login.
     * 
     * @param user
     *            utente che tenta di accedere
     */
    public void login(User user) {
        UserDAO userDAO = new UserDAOPostgreSQL();

        try {
            if (userDAO.doesUserExist(user))
                openHomePage(user);
            else
                MessageDisplayer.showErrorMessage("Errore accesso", "Impossibile accedere: nome o password errati.");
        } catch (Exception e) {
            MessageDisplayer.showErrorMessage("Errore accesso", e.getMessage());
        }
    }

    /**
     * Esegue il logout.
     */
    public void logout() {
        openLoginPage();
    }

    private void openLoginPage() {
        loginPage.setVisible(true);
        homepage.setVisible(false);
    }

    private void openHomePage(User user) {
        try {
            setUser(user);
            homepage.setCategoriesTreeModel(categoryRepository.getTree());
            homepage.setReferences(referenceRepository.getAll());
            homepage.setVisible(true);
            loginPage.setVisible(false);
        } catch (Exception e) {
            MessageDisplayer.showErrorMessage("Errore apertura", "Impossibile caricare i dati dell'utente.");
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
            articleEditor = new ArticleEditor();
            setupReferenceEditor(articleEditor, (x) -> referenceRepository.save(x));
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
            bookEditor = new BookEditor();
            setupReferenceEditor(bookEditor, (x) -> referenceRepository.save(x));
        }

        openReferenceEditor(bookEditor, book);
    }

    /**
     * Apre l'editor di riferimenti per tesi.
     * 
     * @param thesis
     *            tesi da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openThesisEditor(Thesis thesis) {
        if (thesisEditor == null) {
            thesisEditor = new ThesisEditor();
            setupReferenceEditor(thesisEditor, (x) -> referenceRepository.save(x));
        }

        openReferenceEditor(thesisEditor, thesis);
    }

    /**
     * Apre l'editor di riferimenti per codice sorgente.
     * 
     * @param sourceCode
     *            codice sorgente da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openSourceCodeEditor(SourceCode sourceCode) {
        if (sourceCodeEditor == null) {
            sourceCodeEditor = new SourceCodeEditor();
            setupReferenceEditor(sourceCodeEditor, (x) -> referenceRepository.save(x));
        }

        openReferenceEditor(sourceCodeEditor, sourceCode);
    }

    /**
     * Apre l'editor di riferimenti per immagini.
     * 
     * @param image
     *            immagine da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openImageEditor(Image image) {
        if (imageEditor == null) {
            imageEditor = new ImageEditor();
            setupReferenceEditor(imageEditor, (x) -> referenceRepository.save(x));
        }

        openReferenceEditor(imageEditor, image);
    }

    /**
     * Apre l'editor di riferimenti per video.
     * 
     * @param video
     *            video da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openVideoEditor(Video video) {
        if (videoEditor == null) {
            videoEditor = new VideoEditor();
            setupReferenceEditor(videoEditor, (x) -> referenceRepository.save(x));
        }

        openReferenceEditor(videoEditor, video);
    }

    /**
     * Apre l'editor di riferimenti per siti web.
     * 
     * @param website
     *            sito web da modificare (se {@code null}, viene aperto la creazione, altrimenti per la modifica)
     */
    public void openWebsiteEditor(Website website) {
        if (websiteEditor == null) {
            websiteEditor = new WebsiteEditor();
            setupReferenceEditor(websiteEditor, (x) -> referenceRepository.save(x));
        }

        openReferenceEditor(websiteEditor, website);
    }

    private <T extends BibliographicReference> void openReferenceEditor(ReferenceEditor<T> editor, T referenceToChange) {
        if (editor == null)
            throw new IllegalArgumentException("Editor to open can't be null");

        try {
            editor.setCategoriesTree(categoryRepository.getTree());
            editor.setReferences(referenceRepository.getAll());
            editor.setReferenceToChange(referenceToChange);
            editor.setVisible(true);
        } catch (Exception e) {
            MessageDisplayer.showErrorMessage("Impossibile aprire editor riferimenti", "Si è verificato un errore nell'apertura dell'editor.");
        }
    }

    private <T extends BibliographicReference> void setupReferenceEditor(ReferenceEditor<T> editor, CheckedConsumer<T> saveMethod) {
        if (editor == null || saveMethod == null)
            throw new IllegalArgumentException();

        editor.addReferenceEditorListener(new ReferenceEditorListener<T>() {
            @Override
            public void onReferenceCreation(T newReference) {
                try {
                    saveMethod.call(newReference);
                    editor.setVisible(false);
                    homepage.reloadReferences();
                } catch (Exception e) {
                    MessageDisplayer.showErrorMessage("Salvataggio non riuscito", e.getMessage());
                }
            }
        });
    }

    /**
     * Rimuove un riferimento.
     * <p>
     * In caso si verifichi un errore, mostra un messaggio di errore nella homepage.
     * 
     * @param reference
     *            riferimento da rimuovere
     */
    public void removeReference(BibliographicReference reference) {
        try {
            referenceRepository.remove(reference);
            homepage.reloadReferences();
        } catch (Exception e) {
            MessageDisplayer.showErrorMessage("Errore rimozione riferimento", e.getMessage());
        }
    }

    /**
     * Aggiunge una categoria.
     * 
     * @param category
     *            categoria da aggiungere
     */
    public void addCategory(Category category) {
        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            MessageDisplayer.showErrorMessage("Errore salvataggio categoria", e.getMessage());
        }
    }

    /**
     * Aggiorna una categoria.
     * 
     * @param category
     *            categoria da aggiornare
     * @param newName
     *            nuovo nome della categoria
     */
    public void updateCategory(Category category, String newName) {
        try {
            categoryRepository.update(category, newName);
        } catch (Exception e) {
            MessageDisplayer.showErrorMessage("Errore modifica categoria", e.getMessage());
        }
    }

    /**
     * Rimuove una categoria.
     * 
     * @param category
     *            categoria da rimuovere
     */
    public void removeCategory(Category category) {
        try {
            categoryRepository.remove(category);
            referenceRepository.forceNextRetrievalFromDatabase();
            homepage.setReferences(referenceRepository.getAll());
        } catch (Exception e) {
            MessageDisplayer.showErrorMessage("Errore rimozione categoria", e.getMessage());
        }
    }

}