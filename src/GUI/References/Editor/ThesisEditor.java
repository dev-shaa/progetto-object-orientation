package GUI.References.Editor;

import Entities.References.PhysicalResources.Thesis;
import Exceptions.InvalidInputException;
import Exceptions.Database.ReferenceDatabaseException;
import Repository.CategoryRepository;
import Repository.ReferenceRepository;

import java.awt.Frame;
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
     * @param owner
     *            proprietario di questa finestra di dialogo
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code categoryController == null} o {@code referenceController == null}
     */
    public ThesisEditor(Frame owner, CategoryRepository categoryController, ReferenceRepository referenceController) {
        super(owner, "Tesi", categoryController, referenceController);
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
    protected void saveToDatabase(Thesis reference) throws ReferenceDatabaseException {
        getReferenceController().save(reference);
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
