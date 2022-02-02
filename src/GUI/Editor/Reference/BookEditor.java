package GUI.Editor.Reference;

import Entities.References.PhysicalResources.Book;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;

import Controller.AuthorController;
import Controller.CategoryController;
import Controller.ReferenceController;

import java.awt.Frame;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un libro.
 */
public class BookEditor extends PublicationEditor<Book> {

    private JTextField ISBN;

    private final String ISBNLabel = "ISSN";
    private final String ISBNTooltip = "Codice identificativo ISSN dell'articolo";

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento.
     * 
     * @param owner
     *            proprietario di questa finestra di dialogo
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @param authorController
     *            controller degli autori
     * @throws IllegalArgumentException
     *             se {@code categoryController == null}, {@code referenceController == null} o {@code authorController == null}
     */
    public BookEditor(Frame owner, CategoryController categoryController, ReferenceController referenceController, AuthorController authorController) {
        super(owner, "Libro", categoryController, referenceController, authorController);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();

        ISBN = new JTextField();
        addFieldComponent(ISBN, ISBNLabel, ISBNTooltip);
    }

    @Override
    protected void setFieldsValues(Book reference) {
        super.setFieldsValues(reference);

        setISBNValue(reference == null ? null : reference.getISBN());
    }

    @Override
    protected void saveReference() {
        try {
            Book bookToSave = getOpenReference() == null ? new Book("temp") : getOpenReference();
            fillReferenceValues(bookToSave);
            getReferenceController().saveReference(bookToSave);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Book reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);
        reference.setISBN(getISBNValue());
    }

    private void setISBNValue(String ISBN) {
        this.ISBN.setText(ISBN);
    }

    private String getISBNValue() {
        return convertEmptyStringToNull(ISBN.getText().trim());
    }

}
