package GUI.Editor.Reference;

import Entities.References.OnlineResources.Image;
import Exceptions.RequiredFieldMissingException;

import Controller.CategoryController;
import Controller.ReferenceController;

import java.awt.Frame;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un'immagine.
 */
public class ImageEditor extends OnlineResourceEditor<Image> {

    private JSpinner width;
    private JSpinner height;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un'immagine.
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
    public ImageEditor(Frame owner, CategoryController categoryController, ReferenceController referenceController) {
        super(owner, "Immagine", categoryController, referenceController);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();

        width = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        height = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));

        addFieldComponent(width, "Larghezza", "Larghezza dell'immagine.");
        addFieldComponent(height, "Altezza", "Altezza dell'immagine.");
    }

    @Override
    protected void setFieldsValues(Image reference) {
        super.setFieldsValues(reference);

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
    protected void fillReferenceValues(Image reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        reference.setWidth(getWidthValue());
        reference.setHeight(getHeightValue());
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
