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
 * Pannello di dialogo per la creazione o modifica di un riferimento a un video.
 */
public class VideoEditor extends OnlineResourceEditor<Video> {

    /**
     * TODO: commenta
     * 
     * @param categoryController
     * @param referenceController
     * @param authorController
     */
    public VideoEditor(CategoryController categoryController, ReferenceController referenceController, AuthorController authorController) {
        super("Video", categoryController, referenceController, authorController);
    }

    private Video video;
    private JSpinner width;
    private JSpinner height;
    private JSpinner frameRate;
    private JSpinner duration;

    @Override
    protected void setFieldsValues(Video reference) {
        super.setFieldsValues(reference);

        if (video == null) {
            setWidthValue(1);
            setHeightValue(1);
            setFrameRateValue(1);
            setDurationValue(1);
        } else {
            setWidthValue(video.getWidth());
            setHeightValue(video.getHeight());
            setFrameRateValue(video.getFrameRate());
            setDurationValue(video.getDuration());
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
            Video videoToFill = video == null ? new Video("placeholder", "placeholder") : video;
            fillReferenceValues(videoToFill);
            getReferenceController().saveReference(videoToFill);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, "Si è verificato un errore durante il salvataggio", "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Video reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        video.setWidth(getWidthValue());
        video.setHeight(getHeightValue());
        video.setFrameRate(getFrameRateValue());
        video.setDuration(getDurationValue());
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
