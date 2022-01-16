package GUI.ReferenceEditor;

import DAO.BibliographicReferenceDAO;
import GUI.Categories.CategoriesTreeManager;
import Entities.References.OnlineResources.Video;
import Exceptions.RequiredFieldMissingException;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a un video.
 */
public class VideoEditor extends OnlineResourceEditor {

    private Video video;
    private JSpinner width;
    private JSpinner height;
    private JSpinner frameRate;
    // TODO: durata

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
        this(categoriesTree, referenceDAO, null);
    }

    /**
     * Crea un nuovo pannello di dialogo per la modifica di un riferimento a un video, inserendo i valori già presenti all'interno dei campi.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @param video
     *            video da modificare (se nullo, non verrà inserito alcun valore e si considera come se si stesse creando un nuovo riferimento)
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public VideoEditor(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO, Video video) throws IllegalArgumentException {
        super("Video", categoriesTree, referenceDAO, video);
        this.video = video;

        if (video != null) {
            setWidthValue(video.getWidth());
            setHeightValue(video.getHeight());
            setFrameRateValue(video.getFrameRate());
        }
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTree) {
        super.setup(categoriesTree);

        width = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        height = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        frameRate = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));

        addFieldComponent(width, "Larghezza");
        addFieldComponent(height, "Altezza");
        addFieldComponent(frameRate, "Frequenza");
    }

    @Override
    protected void saveReference() {
        Video videoToFill = video == null ? new Video("placeholder", null, "placeholder") : video;

        try {
            fillVideoValues(videoToFill);
            // TODO: salva nel database
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        }
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
     * Riempie i campi del video passato con i valori inseriti dall'utente.
     * 
     * @param video
     *            video da riempire
     * @throws IllegalArgumentException
     *             se {@code video == null}
     * @throws RequiredFieldMissingException
     *             se i campi obbligatori non sono stati riempiti
     * @see #fillOnlineResourceValues(Entities.References.OnlineResources.OnlineResource)
     */
    protected void fillVideoValues(Video video) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillOnlineResourceValues(video);

        video.setWidth(getWidthValue());
        video.setHeight(getHeightValue());
        video.setFrameRate(getFrameRateValue());
        // TODO: duration
    }

}
