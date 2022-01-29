package Entities.References.OnlineResources;

import java.util.List;
import Entities.References.BibliographicReferenceField;

/**
 * Classe che rappresenta un riferimento bibliografico a un sito web.
 */
public class Website extends OnlineResource {

    /**
     * Crea un nuovo riferimento a sito web con il titolo e l'url indicati.
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
    public Website(String title, String URL) throws IllegalArgumentException {
        super(title, URL);
    }

    @Override
    public List<BibliographicReferenceField> getReferenceFields() {
        return super.getReferenceFields();
    }

}
