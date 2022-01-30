package GUI.Homepage.References.Editor;

import Entities.References.OnlineResources.Video;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;

import Controller.AuthorController;
import Controller.CategoryController;
import Controller.ReferenceController;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un video.
 */
public class VideoEditor extends OnlineResourceEditor<Video> {

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a un video.
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
    public VideoEditor(CategoryController categoryController, ReferenceController referenceController, AuthorController authorController) {
        super("Video", categoryController, referenceController, authorController);
    }

    private JSpinner width;
    private JSpinner height;
    private JSpinner frameRate;
    private JSpinner duration;

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
    protected void initialize() {
        super.initialize();

        width = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        height = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        frameRate = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        duration = new JSpinner(new SpinnerNumberModel(1.0, 1.0, null, 1));

        addFieldComponent(width, "Larghezza");
        addFieldComponent(height, "Altezza");
        addFieldComponent(frameRate, "Frequenza");
        addFieldComponent(duration, "Durata");
    }

    @Override
    protected void saveReference() {
        try {
            Video videoToSave = getOpenReference() == null ? new Video("temp", "temp") : getOpenReference();
            fillReferenceValues(videoToSave);
            getReferenceController().saveReference(videoToSave);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore database", JOptionPane.ERROR_MESSAGE);
        }
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
