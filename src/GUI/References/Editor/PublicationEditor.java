package GUI.References.Editor;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.PhysicalResources.Publication;
import Exceptions.Input.InvalidInputException;
import GUI.Utilities.Tree.CustomTreeModel;

import java.util.Collection;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a una pubblicazione.
 */
public abstract class PublicationEditor<T extends Publication> extends ReferenceEditor<T> {

    private JSpinner pageCount;
    private JTextField URL;
    private JTextField publisher;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento.
     * TODO: commenta
     * 
     * @param title
     *            titolo della finestra
     * @param categoriesTree
     * @param references
     */
    public PublicationEditor(String title, CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super(title, categoriesTree, references);
    }

    @Override
    protected void setupSecondaryFields() {
        super.setupSecondaryFields();

        pageCount = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        URL = new JTextField();
        publisher = new JTextField();

        addFieldComponent(pageCount, "Pagine", "Numero di pagine della pubblicazione.");
        addFieldComponent(URL, "URL", "URL della pubblicazione.");
        addFieldComponent(publisher, "Editore", "Editore della pubblicazione.");
    }

    @Override
    protected void setFieldsInitialValues(T reference) {
        super.setFieldsInitialValues(reference);

        if (reference == null) {
            setPageCountValue(0);
            setURLValue(null);
            setPublisherValue(null);
        } else {
            setPageCountValue(reference.getPageCount());
            setURLValue(reference.getURL());
            setPublisherValue(reference.getPublisher());
        }
    }

    @Override
    protected T createNewReference() throws InvalidInputException {
        T reference = super.createNewReference();

        reference.setPageCount(getPageCountValue());
        reference.setURL(getURLValue());
        reference.setPublisher(getPublisherValue());

        return reference;
    }

    private void setPageCountValue(int pageCount) {
        this.pageCount.setValue(pageCount);
    }

    private int getPageCountValue() {
        return (int) pageCount.getValue();
    }

    private void setURLValue(String URL) {
        this.URL.setText(URL);
    }

    private String getURLValue() {
        return URL.getText().trim();
    }

    private void setPublisherValue(String publisher) {
        this.publisher.setText(publisher);
    }

    private String getPublisherValue() {
        return publisher.getText().trim();
    }

}
