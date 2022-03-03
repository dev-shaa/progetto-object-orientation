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
        if (isURLValid(url))
            this.URL = url;
        else
            throw new IllegalArgumentException("L'URL di una risorsa online non può essere vuoto o più lungo di " + URL_MAX_LENGTH + " caratteri.");
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

    private boolean isURLValid(String url) {
        return !isStringNullOrEmpty(url) && url.length() <= URL_MAX_LENGTH;
    }

}