package GUI.ReferenceEditor;

import DAO.BibliographicReferenceDAO;
import GUI.Categories.CategoriesTreeManager;
import Entities.References.OnlineResources.SourceCode;
import Entities.References.OnlineResources.ProgrammingLanguage;
import javax.swing.JComboBox;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a del codice sorgente.
 */
public class SourceCodeEditor extends OnlineResourceEditor {

    private JComboBox<ProgrammingLanguage> programmingLanguage;

    /**
     * Crea un nuovo pannello di dialogo per la creazione di un riferimento a del codice sorgente.
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
    public SourceCodeEditor(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        this(categoriesTree, referenceDAO, null);
    }

    /**
     * Crea un nuovo pannello di dialogo per la modifica di un riferimento a del codice sorgente, inserendo i valori già presenti all'interno dei campi.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @param sourceCode
     *            codice sorgente da modificare (se nullo, non verrà inserito alcun valore e si considera come se si stesse creando un nuovo riferimento)
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public SourceCodeEditor(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO, SourceCode sourceCode) throws IllegalArgumentException {
        super("Codice sorgente", categoriesTree, referenceDAO, sourceCode);

        if (sourceCode != null) {
            setProgrammingLanguageValue(sourceCode.getProgrammingLanguage());
        }
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTree) {
        super.setup(categoriesTree);

        programmingLanguage = new JComboBox<>(ProgrammingLanguage.values());
        addFieldComponent(programmingLanguage, "Linguaggio");
    }

    @Override
    protected void saveReference() {
        // TODO: Auto-generated method stub
    }

    /**
     * Imposta il valore iniziale del linguaggio di programmazione.
     * 
     * @param programmingLanguage
     *            linguaggio di programmazione iniziale del codice
     */
    protected void setProgrammingLanguageValue(ProgrammingLanguage programmingLanguage) {
        this.programmingLanguage.setSelectedItem(programmingLanguage);
    }

    /**
     * Restituisce il linguaggio di programmazione inserito dall'utente.
     * 
     * @return
     *         linguaggio di programmazione del codice
     */
    protected ProgrammingLanguage getProgrammingLanguageValue() {
        return (ProgrammingLanguage) programmingLanguage.getSelectedItem();
    }

}
