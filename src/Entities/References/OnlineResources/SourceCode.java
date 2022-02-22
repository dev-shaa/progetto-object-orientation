package Entities.References.OnlineResources;

import java.util.List;
import Entities.References.BibliographicReferenceField;

/**
 * Classe che rappresenta un riferimento bibliografico a codice sorgente.
 */
public class SourceCode extends OnlineResource {

    private ProgrammingLanguage programmingLanguage;

    /**
     * Crea un nuovo riferimento a codice sorgente con il titolo e l'url indicati.
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
    public SourceCode(String title, String URL) {
        super(title, URL);
    }

    /**
     * Imposta il linguaggio di programmazione del codice.
     * 
     * @param programmingLanguage
     *            linguaggio di programmazione
     * @throws IllegalArgumentException
     *             se {@code programmingLanguage == null}
     */
    public void setProgrammingLanguage(ProgrammingLanguage programmingLanguage) {
        if (programmingLanguage == null)
            throw new IllegalArgumentException("programmingLanguage can't be null");

        this.programmingLanguage = programmingLanguage;
    }

    /**
     * Restituisce il linguaggio di programmazione del codice.
     * 
     * @return
     *         linguaggio di programmazione
     */
    public ProgrammingLanguage getProgrammingLanguage() {
        return programmingLanguage;
    }

    @Override
    public List<BibliographicReferenceField> getReferenceFields() {
        List<BibliographicReferenceField> fields = super.getReferenceFields();

        fields.add(new BibliographicReferenceField("Linguaggio", getProgrammingLanguage()));

        return fields;
    }

}
