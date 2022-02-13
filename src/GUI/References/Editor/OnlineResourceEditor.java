package GUI.References.Editor;

import Entities.References.OnlineResources.OnlineResource;
import Exceptions.Input.InvalidInputException;
import Exceptions.Input.RequiredFieldMissingException;
import Repository.CategoryRepository;
import Repository.ReferenceRepository;

import javax.swing.*;
import java.awt.Frame;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a una risorsa online.
 */
public abstract class OnlineResourceEditor<T extends OnlineResource> extends ReferenceEditor<T> {

    private JTextField URL;

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
    public OnlineResourceEditor(Frame owner, String title, CategoryRepository categoryController, ReferenceRepository referenceController) {
        super(owner, title, categoryController, referenceController);
    }

    @Override
    protected void setupSecondaryFields() {
        super.setupSecondaryFields();

        URL = new JTextField();

        addFieldComponent(URL, "URL (obbligatorio)", "URL della risorsa online.");
    }

    @Override
    protected void setFieldsInitialValues(T reference) {
        super.setFieldsInitialValues(reference);

        setURLValue(reference == null ? null : reference.getURL());
    }

    @Override
    protected T createNewReference() throws InvalidInputException {
        T reference = super.createNewReference();

        reference.setURL(getURLValue());

        return reference;
    }

    private void setURLValue(String URL) {
        this.URL.setText(URL);
    }

    private String getURLValue() throws RequiredFieldMissingException {
        String newURL = URL.getText().trim();

        if (isStringNullOrEmpty(newURL))
            throw new RequiredFieldMissingException("L'URL di una risorsa online non può essere nullo.");

        return newURL;
    }

}
