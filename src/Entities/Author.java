package Entities;

import java.util.regex.Pattern;

/**
 * Classe che rappresenta l'autore di un riferimento bibliografico.
 */
public class Author {
    private String firstName;
    private String lastName;
    private String ORCID;

    private final Pattern orcidPattern = Pattern.compile("^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{3}[0-9xX]$", Pattern.CASE_INSENSITIVE);

    /**
     * Crea un nuovo autore con il nome, cognome e ORCID dati.
     * 
     * @param firstName
     *            nome dell'autore
     * @param lastName
     *            cognome dell'autore
     * @param ORCID
     *            identificativo ORCID dell'autore
     * @throws IllegalArgumentException
     *             se firstName, lastName o ORCID non sono validi
     * @see #setFirstName(String)
     * @see #setLastName(String)
     * @see #setORCID(String)
     */
    public Author(String firstName, String lastName, String ORCID) throws IllegalArgumentException {
        setFirstName(firstName);
        setLastName(lastName);
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
    public void setFirstName(String firstName) throws IllegalArgumentException {
        if (firstName == null)
            throw new IllegalArgumentException();

        this.firstName = firstName;
    }

    /**
     * Restituisce il nome dell'autore.
     * 
     * @return
     *         nome dell'autore
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Imposta il cognome dell'autore.
     * 
     * @param lastName
     *            cognome dell'autore
     * @throws IllegalArgumentException
     *             se {@code lastName == null }
     */
    public void setLastName(String lastName) throws IllegalArgumentException {
        if (lastName == null)
            throw new IllegalArgumentException();

        this.lastName = lastName;
    }

    /**
     * Restituisce il cognome dell'autore.
     * 
     * @return
     *         cognome dell'autore
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Imposta il codice ORCID dell'autore.
     * 
     * @param ORCID
     *            ORCID dell'autore
     * @throws IllegalArgumentException
     *             se la stringa di input non rispetta il pattern del codice ORCID
     */
    public void setORCID(String ORCID) throws IllegalArgumentException {
        if (ORCID != null && !isORCIDValid(ORCID))
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
        String string = firstName + " " + lastName;

        if (getORCID() != null) {
            string += " [ORCID: " + getORCID() + "]";
        }

        return string;
    }

    private boolean isORCIDValid(String ORCID) {
        return orcidPattern.matcher(ORCID).find();
    }

}
