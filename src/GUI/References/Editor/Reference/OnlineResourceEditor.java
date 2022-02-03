package GUI.References.Editor.Reference;

import Entities.References.OnlineResources.OnlineResource;
import Exceptions.RequiredFieldMissingException;

import Controller.CategoryController;
import Controller.ReferenceController;

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
    public OnlineResourceEditor(Frame owner, String title, CategoryController categoryController, ReferenceController referenceController) {
        super(owner, title, categoryController, referenceController);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();

        URL = new JTextField();

        addFieldComponent(URL, "URL (obbligatorio)", "URL della risorsa online.");
    }

    @Override
    protected void setFieldsValues(T reference) {
        super.setFieldsValues(reference);

        setURLValue(reference == null ? null : reference.getURL());
    }

    @Override
    protected void fillReferenceValues(T reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        reference.setURL(getURLValue());
    }

    private void setURLValue(String URL) {
        this.URL.setText(URL);
    }

    private String getURLValue() throws RequiredFieldMissingException {
        String newURL = convertEmptyStringToNull(URL.getText().trim());

        if (newURL == null)
            throw new RequiredFieldMissingException("L'URL di una risorsa online non può essere nullo.");

        return newURL;
    }

}