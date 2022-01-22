package Entities.References.OnlineResources;

/**
 * Classe che rappresenta un riferimento bibliografico a un'immagine.
 */
public class Image extends OnlineResource {

    private Integer width;
    private Integer height;

    /**
     * Crea un nuovo riferimento a immagine con il titolo e l'url indicati.
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
    public Image(String title, String URL) throws IllegalArgumentException {
        super(title, URL);
    }

    /**
     * Imposta la larghezza dell'immagine.
     * 
     * @param width
     *            larghezza dell'immagine
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * Restituisce la larghezza dell'immagine.
     * 
     * @return
     *         larghezza dell'immagine
     */
    public Integer getWidth() {
        return this.width;
    }

    /**
     * Imposta l'altezza dell'immagine.
     * 
     * @param height
     *            altezza dell'immagine
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * Restituisce l'altezza dell'immagine.
     * 
     * @return
     *         altezza dell'immagine
     */
    public Integer getHeight() {
        return this.height;
    }

}
