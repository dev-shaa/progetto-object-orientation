package Entities.References.OnlineResources;

/**
 * Classe che rappresenta un riferimento bibliografico a un'immagine.
 */
public class Image extends OnlineResource {

    private int width;
    private int height;

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
    public Image(String title, String URL) {
        super(title, URL);

        setWidth(1);
        setHeight(1);
    }

    /**
     * Imposta la larghezza dell'immagine.
     * 
     * @param width
     *            larghezza dell'immagine
     * @throws IllegalArgumentException
     *             se {@code width < 0}
     */
    public void setWidth(int width) {
        if (width < 0)
            throw new IllegalArgumentException("width can't be less than 0");

        this.width = width;
    }

    /**
     * Restituisce la larghezza dell'immagine.
     * 
     * @return
     *         larghezza dell'immagine
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Imposta l'altezza dell'immagine.
     * 
     * @param height
     *            altezza dell'immagine
     * @throws IllegalArgumentException
     *             se {@code height < 0}
     */
    public void setHeight(int height) {
        if (height < 0)
            throw new IllegalArgumentException("height can't be less than 0");

        this.height = height;
    }

    /**
     * Restituisce l'altezza dell'immagine.
     * 
     * @return
     *         altezza dell'immagine
     */
    public int getHeight() {
        return this.height;
    }

    @Override
    public String getInfo() {
        return super.getInfo()
                + "\nLarghezza: " + (getWidth() == 0 ? "" : getWidth())
                + "\nAltezza: " + (getHeight() == 0 ? "" : getHeight());
    }

}