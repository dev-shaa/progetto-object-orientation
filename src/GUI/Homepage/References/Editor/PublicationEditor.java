package GUI.Homepage.References.Editor;

import Entities.References.PhysicalResources.Publication;
import Exceptions.RequiredFieldMissingException;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import Controller.AuthorController;
import Controller.CategoryController;
import Controller.ReferenceController;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a una pubblicazione.
 */
public abstract class PublicationEditor<T extends Publication> extends ReferenceEditorDialog<T> {

    private JSpinner pageCount;
    private JTextField URL;
    private JTextField publisher;

    public PublicationEditor(String dialogueTitle, CategoryController categoryController, ReferenceController referenceController, AuthorController authorController) {
        super(dialogueTitle, categoryController, referenceController, authorController);
    }

    @Override
    protected void initialize() {
        super.initialize();

        pageCount = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        URL = new JTextField();
        publisher = new JTextField();

        addFieldComponent(pageCount, "Pagine");
        addFieldComponent(URL, "URL");
        addFieldComponent(publisher, "Editore");
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
    protected void fillReferenceValues(T reference) throws RequiredFieldMissingException {
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
        return convertEmptyStringToNull(URL.getText().trim());
    }

    private void setPublisherValue(String publisher) {
        this.publisher.setText(publisher);
    }

    private String getPublisherValue() {
        return convertEmptyStringToNull(publisher.getText().trim());
    }

}
