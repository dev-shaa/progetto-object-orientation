package Entities.References.OnlineResources;

import Entities.Author;

/**
 * Classe che rappresenta un riferimento bibliografico a codice sorgente.
 */
public class SourceCode extends OnlineResource {

    private ProgrammingLanguage programmingLanguage = null;

    /**
     * Crea un nuovo riferimento a codice sorgente con il titolo, gli autori e l'url indicati.
     * 
     * @param title
     *            titolo del riferimento
     * @param authors
     *            autori del riferimento
     * @param URL
     *            url del riferimento
     * @throws IllegalArgumentException
     *             se il titolo o l'url non sono validi
     * @see #setTitle(String)
     * @see #setURL(String)
     */
    public SourceCode(String title, Author[] authors, String URL) throws IllegalArgumentException {
        super(title, authors, URL);
    }

    /**
     * Imposta il linguaggio di programmazione del codice.
     * 
     * @param programmingLanguage
     *            linguaggio di programmazione
     */
    public void setProgrammingLanguage(ProgrammingLanguage programmingLanguage) {
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

}
