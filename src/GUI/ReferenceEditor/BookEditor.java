package GUI.ReferenceEditor;

import GUI.Categories.CategoriesTreeManager;
import DAO.BibliographicReferenceDAO;
import Entities.References.PhysicalResources.Book;
import javax.swing.JTextField;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a un libro.
 */
public class BookEditor extends PublicationEditor {

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

        if (book != null) {
            setISBNValue(book.getISBN());
        }
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTree) {
        super.setup(categoriesTree);

        ISBN = new JTextField();
        addFieldComponent(ISBN, "ISBN");
    }

    @Override
    protected void saveReference() {
        // TODO: salva nel database
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

}
