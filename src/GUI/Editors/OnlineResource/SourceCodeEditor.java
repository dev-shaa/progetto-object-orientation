package GUI.Editors.OnlineResource;

import Entities.References.OnlineResources.SourceCode;
import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.ProgrammingLanguage;
import Exceptions.Input.InvalidInputException;
import Utilities.Tree.CustomTreeModel;
import io.codeworth.panelmatic.PanelBuilder;

import java.util.Collection;
import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a del codice sorgente.
 */
public class SourceCodeEditor extends OnlineResourceEditor<SourceCode> {

    private JComboBox<ProgrammingLanguage> programmingLanguage;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a codice sorgente,
     * ma senza categorie o rimandi selezionabili.
     */
    public SourceCodeEditor() {
        this(null, null);
    }

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a codice sorgente.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire un riferimento
     * @param references
     *            riferimenti selezionabili come rimandi
     */
    public SourceCodeEditor(CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super("Codice sorgente", categoriesTree, references);
    }

    @Override
    protected void setupSecondaryFields(PanelBuilder panelBuilder) {
        super.setupSecondaryFields(panelBuilder);

        programmingLanguage = new JComboBox<>(ProgrammingLanguage.values());
        programmingLanguage.setToolTipText("Linguaggio di programmazione del codice.");
        panelBuilder.add(new JLabel("Linguaggio"));
        panelBuilder.add(programmingLanguage);
    }

    @Override
    protected void setDefaultValues() {
        super.setDefaultValues();
        setProgrammingLanguageValue(ProgrammingLanguage.NOTSPECIFIED);
    }

    @Override
    protected void setReferenceValues(SourceCode reference) {
        super.setReferenceValues(reference);
        setProgrammingLanguageValue(reference.getProgrammingLanguage());
    }

    @Override
    protected SourceCode getNewInstance() {
        return new SourceCode("title", "URL");
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