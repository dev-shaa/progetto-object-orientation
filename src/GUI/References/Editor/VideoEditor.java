package GUI.References.Editor;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.Video;
import Exceptions.Input.InvalidInputException;
import GUI.Utilities.Tree.CustomTreeModel;
import java.util.Collection;
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
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a un video.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire un riferimento
     * @param references
     *            riferimenti selezionabili come rimandi
     */
    public VideoEditor(CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super("Video", categoriesTree, references);
    }

    @Override
    protected void setDefaultValues() {
        super.setDefaultValues();

        setWidthValue(0);
        setHeightValue(0);
        setFrameRateValue(0);
        setDurationValue(0);
    }

    @Override
    protected void setReferenceValues(Video reference) {
        super.setReferenceValues(reference);

        setWidthValue(reference.getWidth());
        setHeightValue(reference.getHeight());
        setFrameRateValue(reference.getFrameRate());
        setDurationValue(reference.getDuration());
    }

    @Override
    protected void setupSecondaryFields() {
        super.setupSecondaryFields();

        width = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        height = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        frameRate = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        duration = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));

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
    protected Video createNewReference() throws InvalidInputException {
        Video reference = super.createNewReference();

        reference.setWidth(getWidthValue());
        reference.setHeight(getHeightValue());
        reference.setFrameRate(getFrameRateValue());
        reference.setDuration(getDurationValue());

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

    private void setFrameRateValue(int frameRate) {
        this.frameRate.setValue(frameRate);
    }

    private int getFrameRateValue() {
        return (int) frameRate.getValue();
    }

    private void setDurationValue(int duration) {
        this.duration.setValue(duration);
    }

    private int getDurationValue() {
        return (int) duration.getValue();
    }

}
