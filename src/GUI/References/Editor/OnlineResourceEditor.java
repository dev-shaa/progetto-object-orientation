package GUI.References.Editor;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.OnlineResource;
import Exceptions.Input.InvalidInputException;
import Exceptions.Input.RequiredFieldMissingException;
import GUI.Utilities.Tree.CustomTreeModel;

import java.util.Collection;

import javax.swing.*;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a una risorsa online.
 */
public abstract class OnlineResourceEditor<T extends OnlineResource> extends ReferenceEditor<T> {

    private JTextField URL;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento.
     * TODO: commenta
     * 
     * @param title
     * @param categoriesTree
     * @param references
     */
    public OnlineResourceEditor(String title, CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super(title, categoriesTree, references);
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

        if (newURL == null || newURL.isEmpty() || newURL.isBlank())
            throw new RequiredFieldMissingException("L'URL di una risorsa online non può essere nullo.");

        return newURL;
    }

}
