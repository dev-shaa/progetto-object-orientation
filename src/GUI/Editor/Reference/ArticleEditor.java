package GUI.Editor.Reference;

import Entities.References.PhysicalResources.Article;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;

import Controller.AuthorController;
import Controller.CategoryController;
import Controller.ReferenceController;

import java.awt.Frame;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un articolo.
 */
public class ArticleEditor extends PublicationEditor<Article> {

    private JTextField ISSN;

    private final String ISSNLabel = "ISSN";
    private final String ISSNTooltip = "Codice identificativo ISSN dell'articolo";

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un articolo.
     * 
     * @param owner
     *            proprietario di questa finestra di dialogo
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @param authorController
     *            controller degli autori
     * @throws IllegalArgumentException
     *             se {@code categoryController == null}, {@code referenceController == null} o {@code authorController == null}
     */
    public ArticleEditor(Frame owner, CategoryController categoryController, ReferenceController referenceController, AuthorController authorController) {
        super(owner, "Articolo", categoryController, referenceController, authorController);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();

        ISSN = new JTextField();
        addFieldComponent(ISSN, ISSNLabel, ISSNTooltip);
    }

    @Override
    protected void setFieldsValues(Article reference) {
        super.setFieldsValues(reference);

        setISSNValue(reference == null ? null : reference.getISSN());
    }

    @Override
    protected void saveReference() {
        try {
            Article articleToSave = getOpenReference() == null ? new Article("temp") : getOpenReference();
            fillReferenceValues(articleToSave);
            getReferenceController().saveReference(articleToSave);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore database", JOptionPane.ERROR_MESSAGE);
        }
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