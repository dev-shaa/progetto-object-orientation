package GUI.Homepage.References.Editor;

import Entities.References.PhysicalResources.Thesis;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;

import Controller.AuthorController;
import Controller.CategoryController;
import Controller.ReferenceController;

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
     * TODO:
     * 
     * @param categoryController
     * @param referenceController
     * @param authorController
     */
    public ThesisEditor(CategoryController categoryController, ReferenceController referenceController, AuthorController authorController) {
        super("Tesi", categoryController, referenceController, authorController);
    }

    @Override
    protected void initialize() {
        super.initialize();

        university = new JTextField();
        faculty = new JTextField();

        addFieldComponent(university, "Università");
        addFieldComponent(faculty, "Facoltà");
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
    protected void saveReference() {
        try {
            Thesis thesisToSave = thesis == null ? new Thesis("placeholder") : thesis;
            fillReferenceValues(thesisToSave);
            getReferenceController().saveReference(thesisToSave);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, "Si è verificato un errore durante il salvataggio", "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Thesis reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        thesis.setUniversity(getUniversityValue());
        thesis.setFaculty(getFacultyValue());
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
