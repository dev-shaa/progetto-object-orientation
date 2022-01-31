package GUI.Editor.Reference;

import Entities.References.OnlineResources.OnlineResource;
import Exceptions.RequiredFieldMissingException;

import javax.swing.*;

import Controller.AuthorController;
import Controller.CategoryController;
import Controller.ReferenceController;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a una risorsa online.
 */
public abstract class OnlineResourceEditor<T extends OnlineResource> extends ReferenceEditor<T> {

    private JTextField URL;

    private final String URLLabel = "URL (obbligatorio)";
    private final String URLTooltip = "URL della risorsa online";

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a una risorsa online.
     * 
     * @param dialogueTitle
     *            titolo della finestra
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @param authorController
     *            controller degli autori
     * @throws IllegalArgumentException
     *             se {@code categoryController == null}, {@code referenceController == null} o {@code authorController == null}
     */
    public OnlineResourceEditor(String dialogueTitle, CategoryController categoryController, ReferenceController referenceController, AuthorController authorController) {
        super(dialogueTitle, categoryController, referenceController, authorController);
    }

    @Override
    protected void initialize() {
        super.initialize();

        URL = new JTextField();

        addFieldComponent(URL, URLLabel, URLTooltip);
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
