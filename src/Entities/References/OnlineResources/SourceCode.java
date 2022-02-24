package Entities.References.OnlineResources;

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

        setProgrammingLanguage(ProgrammingLanguage.NOTSPECIFIED);
    }

    /**
     * Imposta il linguaggio di programmazione del codice.
     * <p>
     * Se {@code programmingLanguage == null}, viene impostato su {@code NOTSPECIFIED}.
     * 
     * @param programmingLanguage
     *            linguaggio di programmazione
     */
    public void setProgrammingLanguage(ProgrammingLanguage programmingLanguage) {
        if (programmingLanguage == null)
            this.programmingLanguage = ProgrammingLanguage.NOTSPECIFIED;
        else
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
    public String getInfo() {
        return super.getInfo() +
                "\nLinguaggio di programmazione: " + getProgrammingLanguage();
    }

}
