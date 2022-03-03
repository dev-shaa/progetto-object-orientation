package GUI.References.Editor.OnlineResource;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.Image;
import Exceptions.Input.InvalidInputException;
import Utilities.Tree.CustomTreeModel;
import io.codeworth.panelmatic.PanelBuilder;

import java.util.Collection;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un'immagine.
 */
public class ImageEditor extends OnlineResourceEditor<Image> {

    private JSpinner width;
    private JSpinner height;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a un'immagine,
     * ma senza categorie o rimandi selezionabili.
     */
    public ImageEditor() {
        this(null, null);
    }

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a un'immagine.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire un riferimento
     * @param references
     *            riferimenti selezionabili come rimandi
     */
    public ImageEditor(CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super("Immagine", categoriesTree, references);
    }

    @Override
    protected void setupSecondaryFields(PanelBuilder panelBuilder) {
        super.setupSecondaryFields(panelBuilder);

        width = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        width.setToolTipText("Larghezza dell'immagine.");
        panelBuilder.add("Larghezza", width);

        height = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        height.setToolTipText("Altezza dell'immagine.");
        panelBuilder.add("Altezza", height);
    }

    @Override
    protected void setDefaultValues() {
        super.setDefaultValues();

        setWidthValue(0);
        setHeightValue(0);
    }

    @Override
    protected void setReferenceValues(Image reference) {
        super.setReferenceValues(reference);

        setWidthValue(reference.getWidth());
        setHeightValue(reference.getHeight());
    }

    @Override
    protected Image getNewInstance() {
        return new Image("title", "URL");
    }

    @Override
    protected Image createNewReference() throws InvalidInputException {
        Image reference = super.createNewReference();

        reference.setWidth(getWidthValue());
        reference.setHeight(getHeightValue());

        return reference;
    }

    private void setWidthValue(int width) {
        this.width.setValue(width);
    }

    private int getWidthValue() {
        return (int) width.getValue();
    }

    private void setHeightValue(int height) {
        this.height.setValue(height);
    }

    private int getHeightValue() {
        return (int) height.getValue();
    }

}