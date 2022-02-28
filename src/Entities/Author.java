package Entities;

import java.util.regex.Pattern;
import Utilities.StringUtilities;

/**
 * Classe che rappresenta l'autore di un riferimento bibliografico.
 */
public class Author {

    private Integer id;
    private String name;
    private String ORCID;

    private final int NAME_MAX_LENGTH = 256;
    private final Pattern ORCID_PATTERN = Pattern.compile("^ *[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{3}[0-9x] *$");

    /**
     * Crea un nuovo autore con il nome e l'ORCID dati.
     * 
     * @param name
     *            nome dell'autore
     * @param ORCID
     *            identificativo ORCID dell'autore
     * @throws IllegalArgumentException
     *             se {@code name} o {@code ORCID} non sono validi
     * @see #setName(String)
     * @see #setORCID(String)
     */
    public Author(String name, String ORCID) {
        this(name, ORCID, null);
    }

    /**
     * Crea un nuovo autore con il nome e l'ORCID dati.
     * 
     * @param name
     *            nome dell'autore
     * @param ORCID
     *            identificativo ORCID dell'autore
     * @param id
     *            identificativo dell'autore
     * @throws IllegalArgumentException
     *             se {@code name} o {@code ORCID} non sono validi
     * @see #setName(String)
     * @see #setORCID(String)
     */
    public Author(String name, String ORCID, Integer id) {
        setID(id);
        setName(name);
        setORCID(ORCID);
    }

    @Override
    public String toString() {
        String string = name;

        if (getORCID() != null)
            string += " [ORCID: " + getORCID() + "]";

        return string;
    }

    @Override
    public boolean equals(Object obj) {
        // due autori sono uguali se hanno lo stesso nome e lo stesso orcid

        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof Author))
            return false;

        String thisName = getName();
        String thisORCID = String.valueOf(getORCID());

        Author otherAuthor = (Author) obj;
        String otherName = otherAuthor.getName();
        String otherORCID = String.valueOf(otherAuthor.getORCID());

        return thisName.equalsIgnoreCase(otherName) && thisORCID.equals(otherORCID);
    }

    /**
     * Imposta l'identificativo dell'autore.
     * 
     * @param id
     *            identificativo dell'autore
     */
    public void setID(Integer id) {
        this.id = id;
    }

    /**
     * Restituisce l'identificativo dell'autore.
     * 
     * @return identificativo dell'autore
     */
    public Integer getID() {
        return id;
    }

    /**
     * Imposta il nome dell'autore.
     * 
     * @param name
     *            nome dell'autore
     * @throws IllegalArgumentException
     *             se {@code name} è nullo, vuoto o più lungo di {@link #NAME_MAX_LENGTH}
     */
    public void setName(String name) {
        if (isNameValid(name))
            this.name = name.trim();
        else
            throw new IllegalArgumentException("Il nome non può essere vuoto o più lungo di " + NAME_MAX_LENGTH + " caratteri.");
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
     * <p>
     * Può essere {@code null}.
     * 
     * @param ORCID
     *            ORCID dell'autore
     * @throws IllegalArgumentException
     *             se la stringa di input non rispetta il pattern del codice ORCID
     */
    public void setORCID(String ORCID) {
        if (StringUtilities.isStringNullOrEmpty(ORCID))
            this.ORCID = null;
        else if (ORCID_PATTERN.matcher(ORCID).matches())
            this.ORCID = ORCID.trim();
        else
            throw new IllegalArgumentException("Il codice ORCID non rispetta il pattern corretto.");
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

    private boolean isNameValid(String name) {
        // return !StringUtilities.isStringNullOrEmpty(name) && name.length() <= NAME_MAX_LENGTH;
        return !StringUtilities.isStringNullOrEmpty(name) && StringUtilities.isStringShorterThan(name, NAME_MAX_LENGTH);
    }

}