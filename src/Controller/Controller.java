package Controller;

import javax.swing.*;
import com.formdev.flatlaf.*;
import DAO.*;
import GUI.*;
import GUI.References.Editor.*;
import GUI.References.Editor.OnlineResource.*;
import GUI.References.Editor.Publication.*;
import Repository.CategoryRepository;
import Repository.ReferenceRepository;
import Entities.*;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;
import Exceptions.Database.ReferenceDatabaseException;

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

    /**
     * Imposta lo stile dell'applicazione.
     */
    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            // vab, ci faremo bastare il look di default ¯\_(ツ)_/¯
            e.printStackTrace();
        }
    }

    /**
     * Imposta l'utente che sta usando l'applicazione.
     * 
     * @param user
     *            utente da impostare
     * @throws IllegalArgumentException
     *             se {@code user == null}
     */
    private void setUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("user non può essere nullo.");

        this.user = user;
        setupRepositories();
    }

    /**
     * Inizializza i repository da usare.
     */
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

    /**
     * Apre la pagina di login.
     */
    private void openLoginPage() {
        loginPage.setVisible(true);
        homepage.setVisible(false);
    }

    /**
     * Apre la pagina principale dell'applicazione.
     * 
     * @param user
     *            utente che ha eseguito l'accesso
     */
    private void openHomePage(User user) {
        try {
            setUser(user);
            homepage.setCategoriesTreeModel(categoryRepository.getTree());
            homepage.setReferences(referenceRepository.getAll());
            homepage.setNameToDisplay(user.getName());
            homepage.setVisible(true);
            loginPage.setVisible(false);
        } catch (Exception e) {
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

            articleEditor.addReferenceEditorListener(new ReferenceEditorListener<Article>() {

                @Override
                public void onReferenceCreation(Article newReference) {
                    try {
                        referenceRepository.save(newReference);
                        articleEditor.setVisible(false);
                        homepage.reloadReferences();
                    } catch (IllegalArgumentException | ReferenceDatabaseException e) {
                        MessageDisplayer.showErrorMessage("Salvataggio non riuscito", e.getMessage());
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

            bookEditor.addReferenceEditorListener(new ReferenceEditorListener<Book>() {

                @Override
                public void onReferenceCreation(Book newReference) {
                    try {
                        referenceRepository.save(newReference);
                        bookEditor.setVisible(false);
                        homepage.reloadReferences();
                    } catch (IllegalArgumentException | ReferenceDatabaseException e) {
                        MessageDisplayer.showErrorMessage("Salvataggio non riuscito", e.getMessage());
                    }
                }

            });
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
            thesisEditor = new ThesisEditor(null, null);

            thesisEditor.addReferenceEditorListener(new ReferenceEditorListener<Thesis>() {

                @Override
                public void onReferenceCreation(Thesis newReference) {
                    try {
                        referenceRepository.save(newReference);
                        thesisEditor.setVisible(false);
                        homepage.reloadReferences();
                    } catch (IllegalArgumentException | ReferenceDatabaseException e) {
                        MessageDisplayer.showErrorMessage("Salvataggio non riuscito", e.getMessage());
                    }
                }

            });
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
            sourceCodeEditor = new SourceCodeEditor(null, null);

            sourceCodeEditor.addReferenceEditorListener(new ReferenceEditorListener<SourceCode>() {

                @Override
                public void onReferenceCreation(SourceCode newReference) {
                    try {
                        referenceRepository.save(newReference);
                        sourceCodeEditor.setVisible(false);
                        homepage.reloadReferences();
                    } catch (IllegalArgumentException | ReferenceDatabaseException e) {
                        MessageDisplayer.showErrorMessage("Salvataggio non riuscito", e.getMessage());
                    }
                }

            });
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
            imageEditor = new ImageEditor(null, null);

            imageEditor.addReferenceEditorListener(new ReferenceEditorListener<Image>() {

                @Override
                public void onReferenceCreation(Image newReference) {
                    try {
                        referenceRepository.save(newReference);
                        imageEditor.setVisible(false);
                        homepage.reloadReferences();
                    } catch (IllegalArgumentException | ReferenceDatabaseException e) {
                        MessageDisplayer.showErrorMessage("Salvataggio non riuscito", e.getMessage());
                    }
                }

            });
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
            videoEditor = new VideoEditor(null, null);

            videoEditor.addReferenceEditorListener(new ReferenceEditorListener<Video>() {

                @Override
                public void onReferenceCreation(Video newReference) {
                    try {
                        referenceRepository.save(newReference);
                        videoEditor.setVisible(false);
                        homepage.reloadReferences();
                    } catch (IllegalArgumentException | ReferenceDatabaseException e) {
                        MessageDisplayer.showErrorMessage("Salvataggio non riuscito", e.getMessage());
                    }
                }

            });
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
            websiteEditor = new WebsiteEditor(null, null);

            websiteEditor.addReferenceEditorListener(new ReferenceEditorListener<Website>() {
                @Override
                public void onReferenceCreation(Website newReference) {
                    try {
                        referenceRepository.save(newReference);
                        websiteEditor.setVisible(false);
                        homepage.reloadReferences();
                    } catch (IllegalArgumentException | ReferenceDatabaseException e) {
                        MessageDisplayer.showErrorMessage("Salvataggio non riuscito", e.getMessage());
                    }
                }
            });
        }

        openReferenceEditor(websiteEditor, website);
    }

    /**
     * Apre un editor di un tipo di riferimento.
     * 
     * @param <T>
     *            tipo del riferimento da creare o modificare
     * @param editor
     *            editor del riferimento
     * @param referenceToChange
     *            riferimento da cambiare (può essere {@code null})
     */
    private <T extends BibliographicReference> void openReferenceEditor(ReferenceEditor<T> editor, T referenceToChange) {
        try {
            editor.setCategoriesTree(categoryRepository.getTree());
            editor.setReferences(referenceRepository.getAll());
            editor.setReferenceToChange(referenceToChange);
            editor.setVisible(true);
        } catch (Exception e) {
            MessageDisplayer.showErrorMessage("Impossibile aprire editor riferimenti", "Si è verificato un errore nell'apertura dell'editor.");
        }
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