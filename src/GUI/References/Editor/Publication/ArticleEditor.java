package GUI.References.Editor.Publication;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.PhysicalResources.Article;
import Exceptions.Input.InvalidInputException;
import Utilities.Tree.CustomTreeModel;
import java.util.Collection;
import javax.swing.JTextField;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un articolo.
 */
public class ArticleEditor extends PublicationEditor<Article> {

    private JTextField ISSN;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a un articolo,
     * ma senza categorie o rimandi selezionabili.
     */
    public ArticleEditor() {
        this(null, null);
    }

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a un articolo.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire un riferimento
     * @param references
     *            riferimenti selezionabili come rimandi
     */
    public ArticleEditor(CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super("Articolo", categoriesTree, references);
    }

    @Override
    protected void setupSecondaryFields() {
        super.setupSecondaryFields();

        ISSN = new JTextField();
        addFieldComponent(ISSN, "ISSN", "Codice identificativo ISSN dell'articolo.\nEsempio: \"0123-4567\".");
    }

    @Override
    protected void setDefaultValues() {
        super.setDefaultValues();
        setISSNValue(null);
    }

    @Override
    protected void setReferenceValues(Article reference) {
        super.setReferenceValues(reference);
        setISSNValue(reference.getISSN());
    }

    @Override
    protected Article getNewInstance() {
        return new Article("temp");
    }

    @Override
    protected Article createNewReference() throws InvalidInputException {
        Article reference = super.createNewReference();
        reference.setISSN(getISSNValue());
        return reference;
    }

    private void setISSNValue(String ISSN) {
        this.ISSN.setText(ISSN);
    }

    private String getISSNValue() {
        return ISSN.getText().trim();
    }

}