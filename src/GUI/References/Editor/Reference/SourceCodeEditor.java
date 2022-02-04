package GUI.References.Editor.Reference;

import Entities.References.OnlineResources.SourceCode;
import Entities.References.OnlineResources.ProgrammingLanguage;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;

import Controller.CategoryController;
import Controller.ReferenceController;

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
    public SourceCodeEditor(Frame owner, CategoryController categoryController, ReferenceController referenceController) {
        super(owner, "Codice sorgente", categoryController, referenceController);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();

        programmingLanguage = new JComboBox<>(ProgrammingLanguage.values());
        addFieldComponent(programmingLanguage, "Linguaggio", "Linguaggio di programmazione del codice.");
    }

    @Override
    protected void setFieldsValues(SourceCode reference) {
        super.setFieldsValues(reference);

        setProgrammingLanguageValue(reference == null ? ProgrammingLanguage.OTHER : reference.getProgrammingLanguage());
    }

    @Override
    protected SourceCode getNewInstance() {
        return new SourceCode("title", "URL");
    }

    @Override
    protected void saveToDatabase(SourceCode reference) throws ReferenceDatabaseException {
        getReferenceController().saveReference(reference);
    }

    @Override
    protected void fillReferenceValues(SourceCode reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        reference.setProgrammingLanguage(getProgrammingLanguageValue());
    }

    private void setProgrammingLanguageValue(ProgrammingLanguage programmingLanguage) {
        this.programmingLanguage.setSelectedItem(programmingLanguage);
    }

    private ProgrammingLanguage getProgrammingLanguageValue() {
        return (ProgrammingLanguage) programmingLanguage.getSelectedItem();
    }

}
