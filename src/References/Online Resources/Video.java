import java.util.ArrayList;

/**
 * 
 */
public class Video extends OnlineResource {

    private Integer width;
    private Integer height;
    private Integer frameRate;
    private Float duration;

    /**
     * 
     * @param title
     *            titolo del video
     * @param URL
     *            url del video
     * @throws IllegalArgumentException
     *             se il titolo o l'url non sono validi
     */
    public Video(String title, String URL) throws IllegalArgumentException {
        super(title, URL);
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

    // @Override
    // public String getFormattedDetails() {
    // return super.getFormattedDetails() +
    // "Larghezza:\t" + getWidth() +
    // "\nAltezza:\t" + getHeight() +
    // "\nFrequenza:\t" + getFrameRate() +
    // "\nDurata:\t" + getDuration() + "\n";
    // }

    @Override
    public ArrayList<BibliographicReferenceField> getInfoAsStrings() {
        ArrayList<BibliographicReferenceField> fields = super.getInfoAsStrings();

        fields.add(new BibliographicReferenceField("Larghezza", getWidth()));
        fields.add(new BibliographicReferenceField("Altezza", getHeight()));
        fields.add(new BibliographicReferenceField("Frequenza fotogrammi", getFrameRate()));
        fields.add(new BibliographicReferenceField("Durata", getDuration()));

        return fields;
    }

}
