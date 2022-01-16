package Entities.References.OnlineResources;

import Entities.Author;

public class Image extends OnlineResource {

    private Integer width;
    private Integer height;

    /**
     * Crea un nuovo riferimento a un'immagine con il titolo e l'url indicati.
     * 
     * @param title
     *            titolo dell'immagine
     * @param authors
     *            autori dell'immagine
     * @param URL
     *            url dell'immagine
     * @throws IllegalArgumentException
     *             se il titolo o l'url non sono validi
     */
    public Image(String title, Author[] authors, String URL) throws IllegalArgumentException {
        super(title, authors, URL);
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
