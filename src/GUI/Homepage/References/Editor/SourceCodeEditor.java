package GUI.Homepage.References.Editor;

import DAO.BibliographicReferenceDAO;
import Entities.References.OnlineResources.SourceCode;
import Entities.References.OnlineResources.ProgrammingLanguage;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;
import GUI.Homepage.Categories.CategoryTreeModel;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a del codice sorgente.
 */
public class SourceCodeEditor extends OnlineResourceEditor<SourceCode> {

    private SourceCode sourceCode;
    private JComboBox<ProgrammingLanguage> programmingLanguage;

    /**
     * Crea un nuovo pannello di dialogo per la creazione di un riferimento a del codice sorgente.
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
    public SourceCodeEditor(CategoryTreeModel categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super("Codice sorgente", categoriesTree, referenceDAO);
    }

    @Override
    protected void setup() {
        super.setup();

        programmingLanguage = new JComboBox<>(ProgrammingLanguage.values());
        addFieldComponent(programmingLanguage, "Linguaggio");
    }

    @Override
    protected void resetFields(SourceCode reference) {
        super.resetFields(reference);

        setProgrammingLanguageValue(reference == null ? ProgrammingLanguage.OTHER : reference.getProgrammingLanguage());
    }

    @Override
    protected void saveReference() {
        SourceCode sourceCodeToFill = sourceCode == null ? new SourceCode("placeholder", "placeholder") : sourceCode;

        try {
            fillReferenceValues(sourceCodeToFill);
            getReferenceDAO().saveSourceCode(sourceCodeToFill);
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

    /**
     * Imposta il valore iniziale del linguaggio di programmazione.
     * 
     * @param programmingLanguage
     *            linguaggio di programmazione iniziale del codice
     */
    protected void setProgrammingLanguageValue(ProgrammingLanguage programmingLanguage) {
        this.programmingLanguage.setSelectedItem(programmingLanguage);
    }

    /**
     * Restituisce il linguaggio di programmazione inserito dall'utente.
     * 
     * @return
     *         linguaggio di programmazione del codice
     */
    protected ProgrammingLanguage getProgrammingLanguageValue() {
        return (ProgrammingLanguage) programmingLanguage.getSelectedItem();
    }

}