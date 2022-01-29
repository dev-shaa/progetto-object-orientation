package Entities.References.OnlineResources;

import Entities.References.*;
import java.util.List;

/**
 * Classe che rappresenta un riferimento bibliografico a una risorsa online.
 */
public abstract class OnlineResource extends BibliographicReference {

    private String URL;

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
     * @param URL
     *            URL del riferimento
     * @throws IllegalArgumentException
     *             se l'url è nullo o vuoto
     */
    public void setURL(String URL) {
        if (!isURLValid(URL))
            throw new IllegalArgumentException("L'URL di una risorsa online non può essere nullo.");

        this.URL = URL;
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

    private boolean isURLValid(String URL) {
        return URL != null && !URL.isBlank();
    }

    @Override
    public List<BibliographicReferenceField> getReferenceFields() {
        List<BibliographicReferenceField> fields = super.getReferenceFields();

        fields.add(new BibliographicReferenceField("URL", getURL()));

        return fields;
    }

}
