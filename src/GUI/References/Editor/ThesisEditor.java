package GUI.References.Editor;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.PhysicalResources.Thesis;
import Exceptions.Input.InvalidInputException;
import GUI.Utilities.Tree.CustomTreeModel;

import java.util.Collection;

import javax.swing.JTextField;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a una tesi.
 */
public class ThesisEditor extends PublicationEditor<Thesis> {

    private JTextField university;
    private JTextField faculty;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di una tesi.
     * 
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code categoryController == null} o {@code referenceController == null}
     */

    /**
     * TODO: commenta
     * 
     * @param categoriesTree
     * @param references
     */
    public ThesisEditor(CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super("Tesi", categoriesTree, references);
    }

    @Override
    protected void setupSecondaryFields() {
        super.setupSecondaryFields();

        university = new JTextField();
        faculty = new JTextField();

        addFieldComponent(university, "Università", "Università della tesi.");
        addFieldComponent(faculty, "Facoltà", "Facoltà dell'università.");
    }

    @Override
    protected void setFieldsInitialValues(Thesis reference) {
        super.setFieldsInitialValues(reference);

        if (reference == null) {
            setUniversityValue(null);
            setFacultyValue(null);
        } else {
            setUniversityValue(reference.getUniversity());
            setFacultyValue(reference.getFaculty());
        }
    }

    @Override
    protected Thesis getNewInstance() {
        return new Thesis("title");
    }

    @Override
    protected Thesis createNewReference() throws InvalidInputException {
        Thesis reference = super.createNewReference();

        reference.setUniversity(getUniversityValue());
        reference.setFaculty(getFacultyValue());

        return reference;
    }

    private void setUniversityValue(String university) {
        this.university.setText(university);
    }

    private String getUniversityValue() {
        return university.getText().trim();
    }

    private void setFacultyValue(String faculty) {
        this.university.setText(faculty);
    }

    private String getFacultyValue() {
        return faculty.getText().trim();
    }

}
