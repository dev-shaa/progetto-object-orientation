/**
 * 
 */
public class Video extends OnlineResource {

    private int width;
    private int height;
    private int frameRate;
    private float duration;

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
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Restituisce la larghezza del video.
     * 
     * @return
     *         larghezza del video
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Imposta l'altezza del video.
     * 
     * @param height
     *            altezza del video
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Restituisce l'altezza del video.
     * 
     * @return
     *         altezza del video
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * 
     * @param frameRate
     */
    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    /**
     * 
     * @return
     */
    public int getFrameRate() {
        return this.frameRate;
    }

    /**
     * 
     * @param duration
     */
    public void setDuration(float duration) {
        this.duration = duration;
    }

    /**
     * 
     * @return
     */
    public float getDuration() {
        return this.duration;
    }

}
