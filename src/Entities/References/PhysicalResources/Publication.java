package Entities.References.PhysicalResources;

import Entities.References.*;

/**
 * Classe che rappresenta un riferimento bibliografico a una pubblicazione.
 */
public abstract class Publication extends BibliographicReference {

    private int pageCount;
    private String URL;
    private String publisher;

    public static final int URL_MAX_LENGTH = 256;
    public static final int PUBLISHER_MAX_LENGTH = 128;

    /**
     * Crea un nuovo riferimento a una pubblicazione con il titolo indicato.
     * 
     * @param title
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è valido
     * @see #setTitle(String)
     */
    public Publication(String title) {
        super(title);
    }

    /**
     * Imposta il numero di pagine della pubblicazione.
     * 
     * @param pageCount
     *            numero di pagine della pubblicazione
     * @throws IllegalArgumentException
     *             se {@code pageCount < 0}
     */
    public void setPageCount(int pageCount) {
        if (pageCount < 0)
            throw new IllegalArgumentException("Il numero di pagine del libro non può essere inferiore a 0");

        this.pageCount = pageCount;
    }

    /**
     * Restituisce il numero di pagine della pubblicazione.
     * 
     * @return
     *         numero di pagine della pubblicazione
     */
    public int getPageCount() {
        return this.pageCount;
    }

    /**
     * Imposta l'URL del riferimento
     * 
     * @param url
     *            URL del riferimento
     */
    public void setURL(String url) {
        this.URL = getNullOrValidString(url, URL_MAX_LENGTH, "L'URL");
    }

    /**
     * Restituisce l'URL del riferimento.
     * 
     * @return
     *         URL del riferimento ({@code null} se non è indicato)
     */
    public String getURL() {
        return this.URL;
    }

    /**
     * Imposta l'editore della pubblicazione.
     * 
     * @param publisher
     *            editore della pubblicazione
     */
    public void setPublisher(String publisher) {
        this.publisher = getNullOrValidString(publisher, PUBLISHER_MAX_LENGTH, "L'editore");
    }

    /**
     * Restituisce l'editore della pubblicazione.
     * 
     * @return
     *         editore della pubblicazione ({@code null} se non è indicato)
     */
    public String getPublisher() {
        return this.publisher;
    }

    @Override
    public String getInfo() {
        return super.getInfo()
                + "\nPagine: " + (getPageCount() == 0 ? "" : getPageCount())
                + "\nURL: " + (getURL() == null ? "" : getURL())
                + "\nEditore: " + (getPublisher() == null ? "" : getPublisher());
    }

}
