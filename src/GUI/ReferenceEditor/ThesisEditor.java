package GUI.ReferenceEditor;

import DAO.BibliographicReferenceDAO;
import GUI.Categories.CategoriesTreeManager;
import Entities.References.PhysicalResources.Thesis;
import javax.swing.JTextField;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a una tesi.
 */
public class ThesisEditor extends PublicationEditor {

    private JTextField university;
    private JTextField faculty;

    /**
     * Crea un nuovo pannello di dialogo per la creazione di un riferimento a una tesi.
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
    public ThesisEditor(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        this(categoriesTree, referenceDAO, null);
    }

    /**
     * Crea un nuovo pannello di dialogo per la modifica di un riferimento a una tesi, inserendo i valori già presenti all'interno dei campi.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @param thesis
     *            tesi da modificare (se nullo, non verrà inserito alcun valore e si considera come se si stesse creando un nuovo riferimento)
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public ThesisEditor(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO, Thesis thesis) throws IllegalArgumentException {
        super("Tesi", categoriesTree, referenceDAO, thesis);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTree) {
        super.setup(categoriesTree);

        university = new JTextField();
        faculty = new JTextField();

        addFieldComponent(university, "Università");
        addFieldComponent(faculty, "Facoltà");
    }

    @Override
    protected void saveReference() {
        // TODO Auto-generated method stub

    }

    /**
     * Imposta il valore iniziale dell'università.
     * 
     * @param university
     *            università iniziale della tesi
     */
    protected void setUniversityValue(String university) {
        this.university.setText(university);
    }

    /**
     * Restituisce l'università inserita dall'utente.
     * 
     * @return
     *         università della tesi, {@code null} se non è stato inserito niente
     */
    protected String getUniversityValue() {
        return convertEmptyStringToNull(university.getText().trim());
    }

    /**
     * Imposta il valore iniziale della facoltà.
     * 
     * @param programmingLanguage
     *            facoltà iniziale della tesi
     */
    protected void setFacultyValue(String faculty) {
        this.university.setText(faculty);
    }

    /**
     * Restituisce la facoltà inserita dall'utente.
     * 
     * @return
     *         facoltà della tesi, {@code null} se non è stato inserito niente
     */
    protected String getFacultyValue() {
        return convertEmptyStringToNull(faculty.getText().trim());
    }

}
