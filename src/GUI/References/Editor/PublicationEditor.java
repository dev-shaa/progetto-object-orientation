package GUI.References.Editor;

import Entities.References.PhysicalResources.Publication;
import Exceptions.RequiredFieldMissingException;

import Controller.CategoryController;
import Controller.ReferenceController;

import java.awt.Frame;
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
     * 
     * @param owner
     *            proprietario di questa finestra di dialogo
     * @param title
     *            titolo della finestra
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code categoryController == null} o {@code referenceController == null}
     */
    public PublicationEditor(Frame owner, String title, CategoryController categoryController, ReferenceController referenceController) {
        super(owner, title, categoryController, referenceController);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();

        pageCount = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        URL = new JTextField();
        publisher = new JTextField();

        addFieldComponent(pageCount, "Pagine", "Numero di pagine della pubblicazione.");
        addFieldComponent(URL, "URL", "URL della pubblicazione.");
        addFieldComponent(publisher, "Editore", "Editore della pubblicazione.");
    }

    @Override
    protected void setFieldsValues(T reference) {
        super.setFieldsValues(reference);

        if (reference == null) {
            setPageCountValue(1);
            setURLValue(null);
            setPublisherValue(null);
        } else {
            setPageCountValue(reference.getPageCount());
            setURLValue(reference.getURL());
            setPublisherValue(reference.getPublisher());
        }
    }

    @Override
    protected void fillReferenceValues(T reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        reference.setPageCount(getPageCountValue());
        reference.setURL(getURLValue());
        reference.setPublisher(getPublisherValue());
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
