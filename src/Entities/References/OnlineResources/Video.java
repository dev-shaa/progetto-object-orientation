package Entities.References.OnlineResources;

import Entities.Author;
import Entities.References.*;
import java.util.ArrayList;

/**
 * TODO: commenta
 */
public class Video extends OnlineResource {

    private Integer width;
    private Integer height;
    private Integer frameRate;
    private Float duration;

    /**
     * Crea un nuovo riferimento a un video con il titolo e l'url indicati
     * 
     * @param title
     *            titolo del video
     * @param authors
     *            autori del video
     * @param URL
     *            url del video
     * @throws IllegalArgumentException
     *             se il titolo o l'url non sono validi
     */
    public Video(String title, Author[] authors, String URL) throws IllegalArgumentException {
        super(title, authors, URL);
    }

    /**
     * Imposta la larghezza del video.
     * 
     * @param width
     *            larghezza del video
     */
    public void setWidth(Integer width) {
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
     */
    public void setHeight(Integer height) {
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
     * 
     * @param frameRate
     */
    public void setFrameRate(Integer frameRate) {
        this.frameRate = frameRate;
    }

    /**
     * 
     * @return
     */
    public Integer getFrameRate() {
        return this.frameRate;
    }

    /**
     * Imposta la durata del video.
     * 
     * @param duration
     *            durata del video
     */
    public void setDuration(Float duration) {
        this.duration = duration;
    }

    /**
     * Restituisce la durata del video.
     * 
     * @return
     *         durata del video
     */
    public Float getDuration() {
        return this.duration;
    }

    @Override
    public ArrayList<BibliographicReferenceField> getReferenceFields() {
        ArrayList<BibliographicReferenceField> fields = super.getReferenceFields();

        fields.add(new BibliographicReferenceField("Larghezza", getWidth()));
        fields.add(new BibliographicReferenceField("Altezza", getHeight()));
        fields.add(new BibliographicReferenceField("Frequenza fotogrammi", getFrameRate()));
        fields.add(new BibliographicReferenceField("Durata", getDuration()));

        return fields;
    }

}
