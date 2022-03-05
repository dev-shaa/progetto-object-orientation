package GUI.References.OnlineResource;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.OnlineResource;
import Exceptions.Input.InvalidInputException;
import GUI.References.ReferenceEditor;
import Utilities.Tree.CustomTreeModel;
import io.codeworth.panelmatic.PanelBuilder;
import java.util.Collection;
import javax.swing.JTextField;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a una risorsa online.
 */
public abstract class OnlineResourceEditor<T extends OnlineResource> extends ReferenceEditor<T> {

    private JTextField URL;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a una risorsa online,
     * ma senza categorie o rimandi selezionabili.
     *
     * @param title
     *            titolo della finestra
     */
    public OnlineResourceEditor(String title) {
        this(title, null, null);
    }

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a una risorsa online.
     * 
     * @param title
     *            titolo della finestra
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire un riferimento
     * @param references
     *            riferimenti selezionabili come rimandi
     */
    public OnlineResourceEditor(String title, CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super(title, categoriesTree, references);
    }

    @Override
    protected void setupSecondaryFields(PanelBuilder panelBuilder) {
        super.setupSecondaryFields(panelBuilder);

        URL = new JTextField(OnlineResource.URL_MAX_LENGTH);
        URL.setToolTipText("URL della risorsa online.");
        panelBuilder.add("URL", URL);
    }

    @Override
    protected void setDefaultValues() {
        super.setDefaultValues();
        setURLValue(null);
    }

    @Override
    protected void setReferenceValues(T reference) {
        super.setReferenceValues(reference);
        setURLValue(reference.getURL());
    }

    @Override
    protected T createNewReference() throws InvalidInputException {
        T reference = super.createNewReference();
        reference.setURL(getURLValue());
        return reference;
    }

    private void setURLValue(String URL) {
        this.URL.setText(URL);
    }

    private String getURLValue() {
        return URL.getText().trim();
    }

}