package GUI.References.Editor.Publication;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.PhysicalResources.Book;
import Exceptions.Input.InvalidInputException;
import Utilities.Tree.CustomTreeModel;
import io.codeworth.panelmatic.PanelBuilder;

import java.util.Collection;
import javax.swing.JTextField;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un libro.
 */
public class BookEditor extends PublicationEditor<Book> {

    private JTextField ISBN;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a un libro,
     * ma senza categorie o rimandi selezionabili.
     */
    public BookEditor() {
        this(null, null);
    }

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a un libro.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire un riferimento
     * @param references
     *            riferimenti selezionabili come rimandi
     */
    public BookEditor(CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super("Libro", categoriesTree, references);
    }

    @Override
    protected void setupSecondaryFields(PanelBuilder panelBuilder) {
        // TODO Auto-generated method stub
        super.setupSecondaryFields(panelBuilder);

        ISBN = new JTextField();
        // addFieldComponent(ISBN, "ISBN", "Codice identificativo ISBN dell'articolo.");
    }

    @Override
    protected void setDefaultValues() {
        super.setDefaultValues();
        setISBNValue(null);
    }

    @Override
    protected void setReferenceValues(Book reference) {
        super.setReferenceValues(reference);
        setISBNValue(reference.getISBN());
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
