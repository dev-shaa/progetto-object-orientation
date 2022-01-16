package GUI.ReferenceEditor;

import GUI.Categories.CategoriesTreeManager;
import DAO.BibliographicReferenceDAO;
import Entities.References.PhysicalResources.Book;
import Exceptions.RequiredFieldMissingException;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a un libro.
 */
public class BookEditor extends PublicationEditor {

    private Book book;
    private JTextField ISBN;

    /**
     * Crea un nuovo pannello di dialogo per la creazione di un riferimento a un libro.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public BookEditor(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        this(categoriesTree, referenceDAO, null);
    }

    /**
     * Crea un nuovo pannello di dialogo per la modifica di un riferimento a un libro, inserendo i valori già presenti all'interno dei campi.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @param book
     *            libro da modificare (se nullo, non verrà inserito alcun valore e si considera come se si stesse creando un nuovo riferimento)
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public BookEditor(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO, Book book) throws IllegalArgumentException {
        super("Libro", categoriesTree, referenceDAO, book);
        this.book = book;

        if (book != null)
            setISBNValue(book.getISBN());
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTree) {
        super.setup(categoriesTree);

        ISBN = new JTextField();
        addFieldComponent(ISBN, "ISBN");
    }

    @Override
    protected void saveReference() {
        Book bookToFill = book == null ? new Book("placeholder", null) : book;

        try {
            fillBookValues(bookToFill);
            // TODO: salva nel database
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Imposta il valore iniziale dell'ISBN.
     * 
     * @param ISBN
     *            ISBN iniziale del libro
     */
    protected void setISBNValue(String ISBN) {
        this.ISBN.setText(ISBN);
    }

    /**
     * Restituisce l'ISBN inserito dall'utente.
     * 
     * @return
     *         ISBN del libro, {@code null} se non è stato inserito niente
     */
    protected String getISBNValue() {
        return convertEmptyStringToNull(ISBN.getText().trim());
    }

    /**
     * Riempie i campi del libro passato con i valori inseriti dall'utente.
     * 
     * @param book
     *            libro da riempire
     * @throws IllegalArgumentException
     *             se {@code book == null}
     * @throws RequiredFieldMissingException
     *             se i campi obbligatori non sono stati riempiti
     * @see #fillPublicationValues(Entities.References.PhysicalResources.Publication)
     */
    protected void fillBookValues(Book book) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillPublicationValues(book);

        book.setISBN(getISBNValue());
    }

}
