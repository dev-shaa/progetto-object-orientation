package GUI.References.Editor;

import Entities.References.OnlineResources.SourceCode;
import Entities.References.OnlineResources.ProgrammingLanguage;
import Exceptions.Database.ReferenceDatabaseException;
import Exceptions.Input.InvalidInputException;
import Repository.CategoryRepository;
import Repository.ReferenceRepository;

import java.awt.Frame;
import javax.swing.JComboBox;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a del codice sorgente.
 */
public class SourceCodeEditor extends OnlineResourceEditor<SourceCode> {

    private JComboBox<ProgrammingLanguage> programmingLanguage;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di codice sorgente.
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
    public SourceCodeEditor(Frame owner, CategoryRepository categoryController, ReferenceRepository referenceController) {
        super(owner, "Codice sorgente", categoryController, referenceController);
    }

    @Override
    protected void setupSecondaryFields() {
        super.setupSecondaryFields();

        programmingLanguage = new JComboBox<>(ProgrammingLanguage.values());
        addFieldComponent(programmingLanguage, "Linguaggio", "Linguaggio di programmazione del codice.");
    }

    @Override
    protected void setFieldsInitialValues(SourceCode reference) {
        super.setFieldsInitialValues(reference);

        setProgrammingLanguageValue(reference == null ? ProgrammingLanguage.OTHER : reference.getProgrammingLanguage());
    }

    @Override
    protected SourceCode getNewInstance() {
        return new SourceCode("title", "URL");
    }

    @Override
    protected void save(SourceCode reference) throws ReferenceDatabaseException {
        getReferenceRepository().save(reference);
    }

    @Override
    protected SourceCode createNewReference() throws InvalidInputException {
        SourceCode reference = super.createNewReference();

        reference.setProgrammingLanguage(getProgrammingLanguageValue());

        return reference;
    }

    private void setProgrammingLanguageValue(ProgrammingLanguage programmingLanguage) {
        this.programmingLanguage.setSelectedItem(programmingLanguage);
    }

    private ProgrammingLanguage getProgrammingLanguageValue() {
        return (ProgrammingLanguage) programmingLanguage.getSelectedItem();
    }

}
