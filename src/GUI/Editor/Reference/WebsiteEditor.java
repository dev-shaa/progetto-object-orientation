package GUI.Editor.Reference;

import Entities.References.OnlineResources.Website;
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
    protected void initializeFields() {
        super.initializeFields();
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
    protected void fillReferenceValues(Website reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);
    }

}
