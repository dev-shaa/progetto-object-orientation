package GUI.Homepage.References.Editor;

import Entities.References.PhysicalResources.Book;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;

import Controller.AuthorController;
import Controller.CategoryController;
import Controller.ReferenceController;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un libro.
 */
public class BookEditor extends PublicationEditor<Book> {

    private Book book;
    private JTextField ISBN;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a un libro.
     * 
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @param authorController
     *            controller degli autori
     * @throws IllegalArgumentException
     *             se {@code categoryController == null}, {@code referenceController == null} o {@code authorController == null}
     */
    public BookEditor(CategoryController categoryController, ReferenceController referenceController, AuthorController authorController) {
        super("Libro", categoryController, referenceController, authorController);
    }

    @Override
    protected void initialize() {
        super.initialize();

        ISBN = new JTextField();
        addFieldComponent(ISBN, "ISBN");
    }

    @Override
    protected void setFieldsValues(Book publication) {
        super.setFieldsValues(publication);

        if (publication == null) {
            setISBNValue(null);
        } else {
            setISBNValue(publication.getISBN());
        }
    }

    @Override
    protected void saveReference() {

        try {
            Book bookToFill = book == null ? new Book("placeholder", null) : book;
            fillReferenceValues(bookToFill);
            getReferenceController().saveReference(bookToFill);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, "Si è verificato un errore durante il salvataggio", "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Book reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        book.setISBN(getISBNValue());
    }

    private void setISBNValue(String ISBN) {
        this.ISBN.setText(ISBN);
    }

    private String getISBNValue() {
        return convertEmptyStringToNull(ISBN.getText().trim());
    }

}
