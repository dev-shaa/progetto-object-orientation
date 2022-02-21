package GUI.References.Editor;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.PhysicalResources.Article;
import Exceptions.Input.InvalidInputException;
import GUI.Utilities.Tree.CustomTreeModel;

import java.util.Collection;

import javax.swing.JTextField;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un articolo.
 */
public class ArticleEditor extends PublicationEditor<Article> {

    private JTextField ISSN;

    /**
     * TODO: commenta
     * 
     * @param categoriesTree
     * @param references
     */
    public ArticleEditor(CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super("Articolo", categoriesTree, references);
    }

    @Override
    protected void setupSecondaryFields() {
        super.setupSecondaryFields();

        ISSN = new JTextField();
        addFieldComponent(ISSN, "ISSN", "Codice identificativo ISSN dell'articolo.");
    }

    @Override
    protected void setFieldsInitialValues(Article reference) {
        super.setFieldsInitialValues(reference);

        setISSNValue(reference == null ? null : reference.getISSN());
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