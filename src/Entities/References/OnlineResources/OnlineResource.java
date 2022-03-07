package Entities.References.OnlineResources;

import Entities.References.*;

/**
 * Classe che rappresenta un riferimento bibliografico a una risorsa online.
 */
public abstract class OnlineResource extends BibliographicReference {

    private String URL;

    public static final int URL_MAX_LENGTH = 256;

    /**
     * Crea un nuovo riferimento a una risorsa online con il titolo e l'url indicati.
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
    public OnlineResource(String title, String URL) {
        super(title);
        setURL(URL);
    }

    /**
     * Imposta l'URL della risorsa online.
     * 
     * @param url
     *            URL del riferimento
     * @throws IllegalArgumentException
     *             se l'url è nullo, vuoto o più lungo di {@link #URL_MAX_LENGTH}
     */
    public void setURL(String url) {
        this.URL = getNullOrValidString(url, URL_MAX_LENGTH, "L'URL");
    }

    /**
     * Restituisce l'URL della risorsa online.
     * 
     * @return
     *         URL del riferimento
     */
    public String getURL() {
        return this.URL;
    }

    @Override
    public String getInfo() {
        return super.getInfo()
                + "\nURL: " + getURL();
    }

}