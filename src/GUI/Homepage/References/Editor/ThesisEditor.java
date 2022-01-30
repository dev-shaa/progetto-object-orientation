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
 * Finestra di dialogo per la creazione o modifica di un riferimento a una tesi.
 */
public class ThesisEditor extends PublicationEditor<Thesis> {

    private JTextField university;
    private JTextField faculty;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a una tesi.
     * 
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @param authorController
     *            controller degli autori
     * @throws IllegalArgumentException
     *             se {@code categoryController == null}, {@code referenceController == null} o {@code authorController == null}
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
            Thesis thesisToSave = getOpenReference() == null ? new Thesis("temp") : getOpenReference();
            fillReferenceValues(thesisToSave);
            getReferenceController().saveReference(thesisToSave);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore database", JOptionPane.ERROR_MESSAGE);
        }
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
