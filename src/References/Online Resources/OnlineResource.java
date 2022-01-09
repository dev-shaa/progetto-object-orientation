import java.util.ArrayList;

/**
 * 
 */
public abstract class OnlineResource extends BibliographicReference {

    private String URL;

    /**
     * Crea una risorsa online con il titolo e l'URL indicati.
     * 
     * @param title
     *            titolo della risorsa online
     * @param URL
     *            URL della risorsa online
     * @throws IllegalArgumentException
     *             se il titolo o l'url non sono validi
     */
    public OnlineResource(String title, String URL) throws IllegalArgumentException {
        super(title);
        setURL(URL);
    }

    /**
     * Imposta l'URL della risorsa online.
     * 
     * @param URL
     *            URL del riferimento
     * @throws IllegalArgumentException
     *             se l'url non è valido
     */
    public void setURL(String URL) throws IllegalArgumentException {
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
    public ArrayList<BibliographicReferenceField> getReferenceFields() {
        ArrayList<BibliographicReferenceField> fields = super.getReferenceFields();

        fields.add(new BibliographicReferenceField("URL", getURL()));

        return fields;
    }

}
