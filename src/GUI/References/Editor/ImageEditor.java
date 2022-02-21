package GUI.References.Editor;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.Image;
import Exceptions.Input.InvalidInputException;
import GUI.Utilities.Tree.CustomTreeModel;

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
     * TODO: commenta
     * 
     * @param categoriesTree
     * @param references
     */
    public ImageEditor(CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super("Immagine", categoriesTree, references);
    }

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un'immagine.
     * 
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code categoryController == null} o {@code referenceController == null}
     */
    // public ImageEditor(CategoryRepository categoryController, ReferenceRepository referenceController) {
    // super("Immagine", categoryController, referenceController);
    // }

    @Override
    protected void setupSecondaryFields() {
        super.setupSecondaryFields();

        width = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        height = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));

        addFieldComponent(width, "Larghezza", "Larghezza dell'immagine.");
        addFieldComponent(height, "Altezza", "Altezza dell'immagine.");
    }

    @Override
    protected void setFieldsInitialValues(Image reference) {
        super.setFieldsInitialValues(reference);

        if (reference == null) {
            setWidthValue(1);
            setHeightValue(1);
        } else {
            setWidthValue(reference.getWidth());
            setHeightValue(reference.getHeight());
        }
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
