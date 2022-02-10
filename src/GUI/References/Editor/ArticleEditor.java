package GUI.References.Editor;

import Entities.References.PhysicalResources.Article;
import Exceptions.InvalidInputException;
import Exceptions.Database.ReferenceDatabaseException;
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
    protected void saveToDatabase(Article reference) throws ReferenceDatabaseException {
        getReferenceController().save(reference);
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