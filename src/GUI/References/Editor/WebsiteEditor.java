package GUI.References.Editor;

import java.util.Collection;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.Website;
import Exceptions.Input.InvalidInputException;
import GUI.Utilities.Tree.CustomTreeModel;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un sito web.
 */
public class WebsiteEditor extends OnlineResourceEditor<Website> {

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un sito web.
     * TODO: commenta
     * 
     * @param categoriesTree
     * @param references
     */
    public WebsiteEditor(CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super("Sito web", categoriesTree, references);
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
    protected Website createNewReference() throws InvalidInputException {
        return super.createNewReference();
    }

}
