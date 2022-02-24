package GUI.References.Editor;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.Website;
import Exceptions.Input.InvalidInputException;
import GUI.Utilities.Tree.CustomTreeModel;
import java.util.Collection;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un sito web.
 */
public class WebsiteEditor extends OnlineResourceEditor<Website> {

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a un sito web.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire un riferimento
     * @param references
     *            riferimenti selezionabili come rimandi
     */
    public WebsiteEditor(CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super("Sito web", categoriesTree, references);
    }

    @Override
    protected void setupSecondaryFields() {
        super.setupSecondaryFields();
    }

    @Override
    protected void setDefaultValues() {
        super.setDefaultValues();
    }

    @Override
    protected void setReferenceValues(Website reference) {
        super.setReferenceValues(reference);
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
