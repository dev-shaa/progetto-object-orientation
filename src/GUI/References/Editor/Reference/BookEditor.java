package GUI.References.Editor.Reference;

import Entities.References.PhysicalResources.Book;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;

import Controller.CategoryController;
import Controller.ReferenceController;

import java.awt.Frame;
import javax.swing.JTextField;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un libro.
 */
public class BookEditor extends PublicationEditor<Book> {

    private JTextField ISBN;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento.
     * 
     * @param owner
     *            proprietario di questa finestra di dialogo
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code categoryController == null} o {@code referenceController == null}
     */
    public BookEditor(Frame owner, CategoryController categoryController, ReferenceController referenceController) {
        super(owner, "Libro", categoryController, referenceController);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();

        ISBN = new JTextField();
        addFieldComponent(ISBN, "ISBN", "Codice identificativo ISBN dell'articolo.");
    }

    @Override
    protected void setFieldsValues(Book reference) {
        super.setFieldsValues(reference);

        setISBNValue(reference == null ? null : reference.getISBN());
    }

    @Override
    protected Book getNewInstance() {
        return new Book("title");
    }

    @Override
    protected void saveToDatabase(Book reference) throws ReferenceDatabaseException {
        getReferenceController().saveReference(reference);
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
