package GUI.References.Editor.Reference;

import Entities.References.PhysicalResources.Thesis;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;

import Controller.CategoryController;
import Controller.ReferenceController;

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
    public ThesisEditor(Frame owner, CategoryController categoryController, ReferenceController referenceController) {
        super(owner, "Tesi", categoryController, referenceController);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();

        university = new JTextField();
        faculty = new JTextField();

        addFieldComponent(university, "Università", "Università della tesi.");
        addFieldComponent(faculty, "Facoltà", "Facoltà dell'università.");
    }

    @Override
    protected void setFieldsValues(Thesis reference) {
        super.setFieldsValues(reference);

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
        getReferenceController().saveReference(reference);
    }

    @Override
    protected void fillReferenceValues(Thesis reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        reference.setUniversity(getUniversityValue());
        reference.setFaculty(getFacultyValue());
    }

    private void setUniversityValue(String university) {
        this.university.setText(university);
    }

    private String getUniversityValue() {
        return convertEmptyStringToNull(university.getText().trim());
    }

    private void setFacultyValue(String faculty) {
        this.university.setText(faculty);
    }

    private String getFacultyValue() {
        return convertEmptyStringToNull(faculty.getText().trim());
    }

}
