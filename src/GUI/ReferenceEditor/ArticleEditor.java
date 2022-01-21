package GUI.ReferenceEditor;

import DAO.BibliographicReferenceDAO;
import GUI.Categories.CategoriesTreeManager;
import Entities.References.PhysicalResources.Article;
import Exceptions.RequiredFieldMissingException;

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
    public ArticleEditor(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super("Articolo", categoriesTree, referenceDAO);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTree) {
        super.setup(categoriesTree);

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
        Article articleToFill = article == null ? new Article("placeholder", null) : article;

        try {
            fillReferenceValues(articleToFill);
            // TODO: salva nel database
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Article reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(article);

        article.setISSN(getISSNValue());
    }

    @Override
    public void setVisible(boolean b) {
        setVisible(b, null);
    }

    /**
     * Imposta il valore iniziale dell'ISSN.
     * 
     * @param ISSN
     *            ISSN iniziale dell'articolo
     */
    protected void setISSNValue(String ISSN) {
        // TODO: issn regex

        this.ISSN.setText(ISSN);
    }

    /**
     * Restituisce l'ISSN inserito dall'utente.
     * 
     * @return
     *         ISSN dell'articolo, {@code null} se non è stato inserito niente
     */
    protected String getISSNValue() {
        return convertEmptyStringToNull(ISSN.getText().trim());
    }

}