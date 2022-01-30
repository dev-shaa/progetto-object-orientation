package Entities;

import java.util.regex.Pattern;

/**
 * Classe che rappresenta l'autore di un riferimento bibliografico.
 */
public class Author {
    private String name;
    private String ORCID;

    private final Pattern orcidPattern = Pattern.compile("^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{3}[0-9xX]$", Pattern.CASE_INSENSITIVE);

    /**
     * Crea un nuovo autore con il nome, cognome e ORCID dati.
     * 
     * @param name
     *            nome dell'autore
     * @param ORCID
     *            identificativo ORCID dell'autore
     * @throws IllegalArgumentException
     *             se firstName, lastName o ORCID non sono validi
     * @see #setName(String)
     * @see #setLastName(String)
     * @see #setORCID(String)
     */
    public Author(String name, String ORCID) {
        setName(name);
        setORCID(ORCID);
    }

    /**
     * Imposta il nome dell'autore.
     * 
     * @param firstName
     *            nome dell'autore
     * @throws IllegalArgumentException
     *             se {@code firstName == null }
     */
    public void setName(String firstName) {
        if (firstName == null)
            throw new IllegalArgumentException();

        this.name = firstName;
    }

    /**
     * Restituisce il nome dell'autore.
     * 
     * @return
     *         nome dell'autore
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il codice ORCID dell'autore.
     * 
     * @param ORCID
     *            ORCID dell'autore
     * @throws IllegalArgumentException
     *             se la stringa di input non rispetta il pattern del codice ORCID
     */
    public void setORCID(String ORCID) {
        if (!isORCIDValid(ORCID))
            throw new IllegalArgumentException("Codice ORCID non valido");

        this.ORCID = ORCID;
    }

    /**
     * Restituisce il codice ORCID dell'autore.
     * 
     * @return
     *         ORCID dell'autore
     */
    public String getORCID() {
        return ORCID;
    }

    @Override
    public String toString() {
        String string = name;

        if (getORCID() != null)
            string += " [ORCID: " + getORCID() + "]";

        return string;
    }

    private boolean isORCIDValid(String ORCID) {
        if (ORCID == null || ORCID.isBlank())
            return true;

        return orcidPattern.matcher(ORCID).find();
    }

}
