package GUI.ReferenceEditor;

import DAO.BibliographicReferenceDAO;
import GUI.Categories.CategoriesTreeManager;
import Entities.References.PhysicalResources.Article;
import javax.swing.JTextField;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a un articolo.
 */
public class ArticleEditor extends PublicationEditor {

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
        this(categoriesTree, referenceDAO, null);
    }

    /**
     * Crea un nuovo pannello di dialogo per la modifica di un riferimento a un articolo, inserendo i valori già presenti all'interno dei campi.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @param article
     *            articolo da modificare (se nullo, non verrà inserito alcun valore e si considera come se si stesse creando un nuovo riferimento)
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public ArticleEditor(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO, Article article) throws IllegalArgumentException {
        super("Articolo", categoriesTree, referenceDAO, article);

        if (article != null) {
            setISSNValue(article.getISSN());
        }
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTree) {
        super.setup(categoriesTree);

        ISSN = new JTextField();

        addFieldComponent(ISSN, "ISSN");
    }

    @Override
    protected void saveReference() {
        // TODO: salva articolo
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