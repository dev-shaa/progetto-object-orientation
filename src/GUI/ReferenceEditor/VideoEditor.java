package GUI.ReferenceEditor;

import DAO.BibliographicReferenceDAO;
import GUI.Categories.CategoriesTreeManager;
import Entities.References.OnlineResources.Video;
import Exceptions.RequiredFieldMissingException;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a un video.
 */
public class VideoEditor extends OnlineResourceEditor<Video> {

    private Video video;
    private JSpinner width;
    private JSpinner height;
    private JSpinner frameRate;
    private JSpinner duration;

    /**
     * Crea un nuovo pannello di dialogo per la creazione di un riferimento a un video.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public VideoEditor(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super("Video", categoriesTree, referenceDAO);
    }

    @Override
    protected void resetFields(Video reference) {
        super.resetFields(reference);

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
    protected void setup(CategoriesTreeManager categoriesTree) {
        super.setup(categoriesTree);

        width = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        height = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        frameRate = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        duration = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE)); // FIXME:

        addFieldComponent(width, "Larghezza");
        addFieldComponent(height, "Altezza");
        addFieldComponent(frameRate, "Frequenza");
        addFieldComponent(duration, "Durata");
    }

    @Override
    protected void saveReference() {
        Video videoToFill = video == null ? new Video("placeholder", null, "placeholder") : video;

        try {
            fillReferenceValues(videoToFill);
            // TODO: salva nel database
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
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

    /**
     * Imposta il valore iniziale della larghezza.
     * 
     * @param university
     *            larghezza iniziale del video
     */
    protected void setWidthValue(int width) {
        this.width.setValue(width);
    }

    /**
     * Restituisce la larghezza inserita dall'utente.
     * 
     * @return
     *         larghezza del video
     */
    protected int getWidthValue() {
        return (int) width.getValue();
    }

    /**
     * Imposta il valore iniziale dell'altezza.
     * 
     * @param frameRate
     *            altezza iniziale del video
     */
    protected void setHeightValue(int height) {
        this.height.setValue(height);
    }

    /**
     * Restituisce l'altezza inserita dall'utente.
     * 
     * @return
     *         altezza del video
     */
    protected int getHeightValue() {
        return (int) height.getValue();
    }

    /**
     * Imposta il valore iniziale del frame rate.
     * 
     * @param frameRate
     *            frame rate iniziale del video
     */
    protected void setFrameRateValue(int frameRate) {
        this.frameRate.setValue(frameRate);
    }

    /**
     * Restituisce il frame rate inserito dall'utente.
     * 
     * @return
     *         frame rate del video
     */
    protected int getFrameRateValue() {
        return (int) frameRate.getValue();
    }

    /**
     * Imposta il valore iniziale della durata.
     * 
     * @param frameRate
     *            durata iniziale del video
     */
    protected void setDurationValue(float duration) {
        // TODO:
        // this.duration.setValue(duration);
    }

    /**
     * Restituisce la durata inserita dall'utente.
     * 
     * @return
     *         durata del video
     */
    protected float getDurationValue() {
        // TODO:
        return 0;
    }

}
