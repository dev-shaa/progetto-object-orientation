package GUI.Homepage.References.Editor;

import DAO.BibliographicReferenceDAO;
import Entities.References.PhysicalResources.Article;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;
import GUI.Homepage.Categories.CategoryTreeModel;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a un articolo.
 */
public class ArticleEditor extends PublicationEditor<Article> {

    private Article article;
    private JTextField ISSN;

    /**
     * Crea un nuovo pannello di dialogo per la creazione di un riferimento a un articolo.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public ArticleEditor(CategoryTreeModel categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super("Articolo", categoriesTree, referenceDAO);
    }

    @Override
    protected void setup() {
        super.setup();

        ISSN = new JTextField();

        addFieldComponent(ISSN, "ISSN");
    }

    @Override
    protected void resetFields(Article reference) {
        super.resetFields(reference);

        if (reference == null) {
            setISSNValue(null);
        } else {
            setISSNValue(reference.getISSN());
        }
    }

    @Override
    protected void saveReference() {
        try {
            Article articleToFill = article == null ? new Article("placeholder") : article;

            fillReferenceValues(articleToFill);

            getReferenceDAO().saveArticle(articleToFill);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, "Si è verificato un errore durante il salvataggio", "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Article reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        reference.setISSN(getISSNValue());
    }

    /**
     * Imposta il valore iniziale dell'ISSN.
     * 
     * @param ISSN
     *            ISSN iniziale dell'articolo
     */
    private void setISSNValue(String ISSN) {
        this.ISSN.setText(ISSN);
    }

    /**
     * Restituisce l'ISSN inserito dall'utente.
     * 
     * @return
     *         ISSN dell'articolo, {@code null} se non è stato inserito niente
     */
    private String getISSNValue() {
        return convertEmptyStringToNull(ISSN.getText().trim());
    }

}