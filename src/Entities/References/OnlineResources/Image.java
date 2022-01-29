package Entities.References.OnlineResources;

import java.util.List;

import Entities.References.BibliographicReferenceField;

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
    public Image(String title, String URL) throws IllegalArgumentException {
        super(title, URL);

        setWidth(1);
        setHeight(1);
    }

    /**
     * Imposta la larghezza dell'immagine.
     * 
     * @param width
     *            larghezza dell'immagine
     * @throws IllegalArgumentHeight
     *             se {@code width < 1}
     */
    public void setWidth(int width) {
        if (width < 1)
            throw new IllegalArgumentException("width can't be less than 1");

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
     * @throws IllegalArgumentHeight
     *             se {@code height < 1}
     */
    public void setHeight(int height) {
        if (height < 1)
            throw new IllegalArgumentException("height can't be less than 1");

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
    public List<BibliographicReferenceField> getReferenceFields() {
        List<BibliographicReferenceField> fields = super.getReferenceFields();

        fields.add(new BibliographicReferenceField("Larghezza", getWidth()));
        fields.add(new BibliographicReferenceField("Altezza", getHeight()));

        return fields;
    }

}
