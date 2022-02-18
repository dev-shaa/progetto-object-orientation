package GUI.References.Editor;

import Entities.References.OnlineResources.Website;
import Exceptions.Database.ReferenceDatabaseException;
import Exceptions.Input.InvalidInputException;
import Repository.CategoryRepository;
import Repository.ReferenceRepository;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un sito web.
 */
public class WebsiteEditor extends OnlineResourceEditor<Website> {

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di codice sorgente.
     * 
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code categoryController == null} o {@code referenceController == null}
     */
    public WebsiteEditor(CategoryRepository categoryController, ReferenceRepository referenceController) {
        super("Sito web", categoryController, referenceController);
    }

    @Override
    protected void setupSecondaryFields() {
        super.setupSecondaryFields();
    }

    @Override
    protected void setFieldsInitialValues(Website reference) {
        super.setFieldsInitialValues(reference);
    }

    @Override
    protected Website getNewInstance() {
        return new Website("title", "URL");
    }

    @Override
    protected void save(Website reference) throws ReferenceDatabaseException {
        getReferenceRepository().save(reference);
    }

    @Override
    protected Website createNewReference() throws InvalidInputException {
        return super.createNewReference();
    }

}
