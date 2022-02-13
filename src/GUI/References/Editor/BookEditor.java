package GUI.References.Editor;

import Entities.References.PhysicalResources.Book;
import Exceptions.Database.ReferenceDatabaseException;
import Exceptions.Input.InvalidInputException;
import Repository.CategoryRepository;
import Repository.ReferenceRepository;

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
    public BookEditor(Frame owner, CategoryRepository categoryController, ReferenceRepository referenceController) {
        super(owner, "Libro", categoryController, referenceController);
    }

    @Override
    protected void setupSecondaryFields() {
        super.setupSecondaryFields();

        ISBN = new JTextField();
        addFieldComponent(ISBN, "ISBN", "Codice identificativo ISBN dell'articolo.");
    }

    @Override
    protected void setFieldsInitialValues(Book reference) {
        super.setFieldsInitialValues(reference);

        setISBNValue(reference == null ? null : reference.getISBN());
    }

    @Override
    protected Book getNewInstance() {
        return new Book("title");
    }

    @Override
    protected void saveToDatabase(Book reference) throws ReferenceDatabaseException {
        getReferenceRepository().save(reference);
    }

    @Override
    protected Book createNewReference() throws InvalidInputException {
        Book reference = super.createNewReference();

        reference.setISBN(getISBNValue());

        return reference;
    }

    private void setISBNValue(String ISBN) {
        this.ISBN.setText(ISBN);
    }

    private String getISBNValue() {
        return ISBN.getText().trim();
    }

}
