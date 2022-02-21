package GUI.References.Editor;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.PhysicalResources.Book;
import Exceptions.Input.InvalidInputException;
import GUI.Utilities.Tree.CustomTreeModel;

import java.util.Collection;

import javax.swing.JTextField;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un libro.
 */
public class BookEditor extends PublicationEditor<Book> {

    private JTextField ISBN;

    /**
     * TODO: commenta
     * 
     * @param categoriesTree
     * @param references
     */
    public BookEditor(CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super("Libro", categoriesTree, references);
    }

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento.
     * 
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code categoryController == null} o {@code referenceController == null}
     */
    // public BookEditor(CategoryRepository categoryController, ReferenceRepository referenceController) {
    // super("Libro", categoryController, referenceController);
    // }

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
