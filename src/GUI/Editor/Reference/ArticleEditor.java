package GUI.Editor.Reference;

import Entities.References.PhysicalResources.Article;
import Exceptions.RequiredFieldMissingException;

import Controller.CategoryController;
import Controller.ReferenceController;

import java.awt.Frame;
import javax.swing.JTextField;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un articolo.
 */
public class ArticleEditor extends PublicationEditor<Article> {

    private JTextField ISSN;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un articolo.
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
    public ArticleEditor(Frame owner, CategoryController categoryController, ReferenceController referenceController) {
        super(owner, "Articolo", categoryController, referenceController);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();

        ISSN = new JTextField();
        addFieldComponent(ISSN, "ISSN", "Codice identificativo ISSN dell'articolo.");
    }

    @Override
    protected void setFieldsValues(Article reference) {
        super.setFieldsValues(reference);

        setISSNValue(reference == null ? null : reference.getISSN());
    }

    @Override
    protected Article getNewInstance() {
        return new Article("temp");
    }

    @Override
    protected void fillReferenceValues(Article reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        reference.setISSN(getISSNValue());
    }

    private void setISSNValue(String ISSN) {
        this.ISSN.setText(ISSN);
    }

    private String getISSNValue() {
        return convertEmptyStringToNull(ISSN.getText().trim());
    }

}