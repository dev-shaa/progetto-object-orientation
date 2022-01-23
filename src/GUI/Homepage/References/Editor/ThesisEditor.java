package GUI.Homepage.References.Editor;

import DAO.BibliographicReferenceDAO;
import Entities.References.PhysicalResources.Thesis;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;
import GUI.Homepage.Categories.CategoryTreeModel;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a una tesi.
 */
public class ThesisEditor extends PublicationEditor<Thesis> {

    private Thesis thesis;
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
    public ThesisEditor(CategoryTreeModel categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super("Tesi", categoriesTree, referenceDAO);
    }

    @Override
    protected void setup() {
        super.setup();

        university = new JTextField();
        faculty = new JTextField();

        addFieldComponent(university, "Università");
        addFieldComponent(faculty, "Facoltà");
    }

    @Override
    protected void resetFields(Thesis reference) {
        super.resetFields(reference);

        if (reference == null) {
            setUniversityValue(null);
            setFacultyValue(null);
        } else {
            setUniversityValue(reference.getUniversity());
            setFacultyValue(reference.getFaculty());
        }
    }

    @Override
    protected void saveReference() {
        Thesis thesisToFill = thesis == null ? new Thesis("placeholder") : thesis;

        try {
            fillReferenceValues(thesisToFill);
            getReferenceDAO().saveThesis(thesisToFill);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, "Si è verificato un errore durante il salvataggio", "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Thesis reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(thesis);

        thesis.setUniversity(getUniversityValue());
        thesis.setFaculty(getFacultyValue());
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
