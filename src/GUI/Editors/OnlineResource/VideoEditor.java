package GUI.Editors.OnlineResource;

import Entities.Category;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.Video;
import Exceptions.Input.InvalidInputException;
import Utilities.Tree.CustomTreeModel;
import io.codeworth.panelmatic.PanelBuilder;

import java.util.Collection;

import javax.swing.JLabel;
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
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a un video,
     * ma senza categorie o rimandi selezionabili.
     */
    public VideoEditor() {
        this(null, null);
    }

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
    protected void setupSecondaryFields(PanelBuilder panelBuilder) {
        super.setupSecondaryFields(panelBuilder);

        width = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        width.setToolTipText("Larghezza del video.");
        panelBuilder.add(new JLabel("Larghezza"));
        panelBuilder.add(width);

        height = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        height.setToolTipText("Altezza del video.");
        panelBuilder.add(new JLabel("Altezza"));
        panelBuilder.add(height);

        frameRate = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        frameRate.setToolTipText("Numero di fotogrammi al secondo del video.");
        panelBuilder.add(new JLabel("Frequenza"));
        panelBuilder.add(frameRate);

        duration = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        duration.setToolTipText("Durata del video in secondi.");
        panelBuilder.add(new JLabel("Durata"));
        panelBuilder.add(duration);
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