package GUI.Homepage.References.Editor;

import DAO.BibliographicReferenceDAO;
import Entities.References.PhysicalResources.Book;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;
import GUI.Homepage.Categories.CategoryTreeModel;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a un libro.
 */
public class BookEditor extends PublicationEditor<Book> {

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
    public BookEditor(CategoryTreeModel categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super("Libro", categoriesTree, referenceDAO);
    }

    @Override
    protected void setup() {
        super.setup();

        ISBN = new JTextField();
        addFieldComponent(ISBN, "ISBN");
    }

    @Override
    protected void resetFields(Book publication) {
        super.resetFields(publication);

        if (publication == null) {
            setISBNValue(null);
        } else {
            setISBNValue(publication.getISBN());
        }
    }

    @Override
    protected void saveReference() {
        Book bookToFill = book == null ? new Book("placeholder", null) : book;

        try {
            fillReferenceValues(bookToFill);

            getReferenceDAO().saveBook(bookToFill);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, "Si è verificato un errore durante il salvataggio", "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Book reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(book);

        book.setISBN(getISBNValue());
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
