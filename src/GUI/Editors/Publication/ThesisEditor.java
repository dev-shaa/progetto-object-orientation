package GUI.Editors.Publication;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.PhysicalResources.Thesis;
import Exceptions.Input.InvalidInputException;
import Utilities.Tree.CustomTreeModel;
import io.codeworth.panelmatic.PanelBuilder;

import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a una tesi.
 */
public class ThesisEditor extends PublicationEditor<Thesis> {

    private JTextField university;
    private JTextField faculty;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a una tesi,
     * ma senza categorie o rimandi selezionabili.
     */
    public ThesisEditor() {
        this(null, null);
    }

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a una tesi.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire un riferimento
     * @param references
     *            riferimenti selezionabili come rimandi
     */
    public ThesisEditor(CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super("Tesi", categoriesTree, references);
    }

    @Override
    protected void setupSecondaryFields(PanelBuilder panelBuilder) {
        super.setupSecondaryFields(panelBuilder);

        university = new JTextField(Thesis.UNIVERSITY_MAX_LENGTH);
        university.setToolTipText("Università della tesi.");
        panelBuilder.add(new JLabel("Università"));
        panelBuilder.add(university);

        faculty = new JTextField(Thesis.FACULTY_MAX_LENGTH);
        faculty.setToolTipText("Facoltà dell'università.");
        panelBuilder.add(new JLabel("Facoltà"));
        panelBuilder.add(faculty);
    }

    @Override
    protected void setDefaultValues() {
        super.setDefaultValues();

        setUniversityValue(null);
        setFacultyValue(null);
    }

    @Override
    protected void setReferenceValues(Thesis reference) {
        super.setReferenceValues(reference);

        setUniversityValue(reference.getUniversity());
        setFacultyValue(reference.getFaculty());
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