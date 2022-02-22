package Entities.References.OnlineResources;

import Entities.References.*;
import java.util.List;

/**
 * Classe che rappresenta un riferimento bibliografico a un video.
 */
public class Video extends OnlineResource {

    private int width;
    private int height;
    private int frameRate;
    private int duration;

    /**
     * Crea un nuovo riferimento a video con il titolo e l'url indicati.
     * 
     * @param title
     *            titolo del riferimento
     * @param URL
     *            url del riferimento
     * @throws IllegalArgumentException
     *             se il titolo o l'url non sono validi
     * @see #setTitle(String)
     * @see #setURL(String)
     */
    public Video(String title, String URL) throws IllegalArgumentException {
        super(title, URL);
    }

    /**
     * Imposta la larghezza del video.
     * 
     * @param width
     *            larghezza del video
     * @throws IllegalArgumentException
     *             se {@code width < 0}
     */
    public void setWidth(int width) {
        if (width < 0)
            throw new IllegalArgumentException("width can't be less than 0");

        this.width = width;
    }

    /**
     * Restituisce la larghezza del video.
     * 
     * @return
     *         larghezza del video
     */
    public Integer getWidth() {
        return this.width;
    }

    /**
     * Imposta l'altezza del video.
     * 
     * @param height
     *            altezza del video
     * @throws IllegalArgumentException
     *             se {@code height < 0}
     */
    public void setHeight(int height) {
        if (height < 0)
            throw new IllegalArgumentException("height can't be less than 0");

        this.height = height;
    }

    /**
     * Restituisce l'altezza del video.
     * 
     * @return
     *         altezza del video
     */
    public Integer getHeight() {
        return this.height;
    }

    /**
     * Imposta la frequenza del video.
     * 
     * @param frameRate
     *            frequenza del video
     * @throws IllegalArgumentException
     *             se {@code frameRate < 0}
     */
    public void setFrameRate(int frameRate) {
        if (frameRate < 0)
            throw new IllegalArgumentException("frameRate can't be less than 0");

        this.frameRate = frameRate;
    }

    /**
     * Restituisce la frequenza del video.
     * 
     * @return
     *         frequenza del video
     */
    public int getFrameRate() {
        return frameRate;
    }

    /**
     * Imposta la durata del video.
     * 
     * @param duration
     *            durata del video (in secondi)
     * @throws IllegalArgumentException
     *             se {@code duration < 0}
     */
    public void setDuration(int duration) {
        if (duration < 0)
            throw new IllegalArgumentException("duration can't be less than 0");

        this.duration = duration;
    }

    /**
     * Restituisce la durata del video.
     * 
     * @return
     *         durata del video (in secondi)
     */
    public int getDuration() {
        return this.duration;
    }

    @Override
    public List<BibliographicReferenceField> getReferenceFields() {
        List<BibliographicReferenceField> fields = super.getReferenceFields();

        fields.add(new BibliographicReferenceField("Larghezza", getWidth()));
        fields.add(new BibliographicReferenceField("Altezza", getHeight()));
        fields.add(new BibliographicReferenceField("Frequenza fotogrammi", getFrameRate()));
        fields.add(new BibliographicReferenceField("Durata", getDuration()));

        return fields;
    }

}
