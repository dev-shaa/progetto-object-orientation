package Entities.References.PhysicalResources;

import java.util.regex.Pattern;

/**
 * Classe che rappresenta un riferimento bibliografico a un articolo.
 */
public class Article extends Publication {

    private String ISSN;

    public static final int ISSN_LENGTH = 9;
    private final Pattern ISSN_PATTERN = Pattern.compile("^ *[0-9]{4}-[0-9]{3}[0-9xX] *$");

    /**
     * Crea un nuovo riferimento a un articolo con il titolo indicato.
     * 
     * @param title
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è valido
     * @see #setTitle(String)
     */
    public Article(String title) {
        super(title);
    }

    /**
     * Imposta il codice identificativo ISSN dell'articolo.
     * 
     * @param ISSN
     *            codice identificativo ISSN
     * @throws IllegalArgumentException
     *             se l'ISSN non rispetta il formato
     */
    public void setISSN(String ISSN) {
        if (isStringNullOrEmpty(ISSN))
            this.ISSN = null;
        else if (ISSN_PATTERN.matcher(ISSN).matches())
            this.ISSN = ISSN.trim();
        else
            throw new IllegalArgumentException("Il codice ISSN non è valido.");
    }

    /**
     * Restituisce il codice identificativo ISSN dell'articolo.
     * 
     * @return
     *         codice identificativo ISSN ({@code null} se non è indicato)
     */
    public String getISSN() {
        return this.ISSN;
    }

    @Override
    public String getInfo() {
        return super.getInfo()
                + "\nISSN: " + (getISSN() == null ? "" : getISSN());
    }

}