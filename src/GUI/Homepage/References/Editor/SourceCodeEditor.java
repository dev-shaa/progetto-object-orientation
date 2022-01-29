package GUI.Homepage.References.Editor;

import Entities.References.OnlineResources.SourceCode;
import Entities.References.OnlineResources.ProgrammingLanguage;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;

import Controller.AuthorController;
import Controller.CategoryController;
import Controller.ReferenceController;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a del codice sorgente.
 */
public class SourceCodeEditor extends OnlineResourceEditor<SourceCode> {

    private SourceCode sourceCode;
    private JComboBox<ProgrammingLanguage> programmingLanguage;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a codice sorgente.
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
    public SourceCodeEditor(CategoryController categoryController, ReferenceController referenceController, AuthorController authorController) {
        super("Codice sorgente", categoryController, referenceController, authorController);
    }

    @Override
    protected void initialize() {
        super.initialize();

        programmingLanguage = new JComboBox<>(ProgrammingLanguage.values());
        addFieldComponent(programmingLanguage, "Linguaggio");
    }

    @Override
    protected void setFieldsValues(SourceCode reference) {
        super.setFieldsValues(reference);

        setProgrammingLanguageValue(reference == null ? ProgrammingLanguage.OTHER : reference.getProgrammingLanguage());
    }

    @Override
    protected void saveReference() {
        try {
            SourceCode sourceCodeToFill = sourceCode == null ? new SourceCode("placeholder", "placeholder") : sourceCode;
            fillReferenceValues(sourceCodeToFill);
            getReferenceController().saveReference(sourceCodeToFill);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, "Si è verificato un errore durante il salvataggio", "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(SourceCode reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        sourceCode.setProgrammingLanguage(getProgrammingLanguageValue());
    }

    private void setProgrammingLanguageValue(ProgrammingLanguage programmingLanguage) {
        this.programmingLanguage.setSelectedItem(programmingLanguage);
    }

    private ProgrammingLanguage getProgrammingLanguageValue() {
        return (ProgrammingLanguage) programmingLanguage.getSelectedItem();
    }

}
