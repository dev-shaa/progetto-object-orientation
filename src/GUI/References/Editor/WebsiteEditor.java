package GUI.References.Editor;

import Entities.References.OnlineResources.Website;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;

import Controller.CategoryController;
import Controller.ReferenceController;

import java.awt.Frame;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un sito web.
 */
public class WebsiteEditor extends OnlineResourceEditor<Website> {

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di codice sorgente.
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
    public WebsiteEditor(Frame owner, CategoryController categoryController, ReferenceController referenceController) {
        super(owner, "Sito web", categoryController, referenceController);
    }

    @Override
    protected void setupSecondaryFields() {
        super.setupSecondaryFields();
    }

    @Override
    protected void setFieldsValues(Website reference) {
        super.setFieldsValues(reference);
    }

    @Override
    protected Website getNewInstance() {
        return new Website("title", "URL");
    }

    @Override
    protected void saveToDatabase(Website reference) throws ReferenceDatabaseException {
        getReferenceController().save(reference);
    }

    @Override
    protected Website createNewReference() throws RequiredFieldMissingException {
        return super.createNewReference();
    }

}
