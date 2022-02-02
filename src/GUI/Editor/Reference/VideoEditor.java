package GUI.Editor.Reference;

import Entities.References.OnlineResources.Video;
import Exceptions.RequiredFieldMissingException;

import Controller.CategoryController;
import Controller.ReferenceController;

import java.awt.Frame;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un video.
 */
public class VideoEditor extends OnlineResourceEditor<Video> {

    private JSpinner width;
    private JSpinner height;
    private JSpinner frameRate;
    private JSpinner duration;

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
    public VideoEditor(Frame owner, CategoryController categoryController, ReferenceController referenceController) {
        super(owner, "Video", categoryController, referenceController);
    }

    @Override
    protected void setFieldsValues(Video reference) {
        super.setFieldsValues(reference);

        if (reference == null) {
            setWidthValue(1);
            setHeightValue(1);
            setFrameRateValue(1);
            setDurationValue(1);
        } else {
            setWidthValue(reference.getWidth());
            setHeightValue(reference.getHeight());
            setFrameRateValue(reference.getFrameRate());
            setDurationValue(reference.getDuration());
        }
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();

        width = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        height = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        frameRate = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        duration = new JSpinner(new SpinnerNumberModel(1.0, 1.0, null, 1));

        addFieldComponent(width, "Larghezza", "Largezza del video.");
        addFieldComponent(height, "Altezza", "Altezza del video.");
        addFieldComponent(frameRate, "Frequenza", "Numero di fotogrammi al secondo del video.");
        addFieldComponent(duration, "Durata", "Durata del video (in secondi).");
    }

    @Override
    protected Video getNewInstance() {
        return new Video("title", "URL");
    }

    @Override
    protected void fillReferenceValues(Video reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        reference.setWidth(getWidthValue());
        reference.setHeight(getHeightValue());
        reference.setFrameRate(getFrameRateValue());
        reference.setDuration(getDurationValue());
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

    private void setFrameRateValue(int frameRate) {
        this.frameRate.setValue(frameRate);
    }

    private int getFrameRateValue() {
        return (int) frameRate.getValue();
    }

    private void setDurationValue(float duration) {
        this.duration.setValue(duration);
    }

    private float getDurationValue() {
        return (float) duration.getValue();
    }

}
