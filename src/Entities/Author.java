package Entities;

import java.util.regex.Pattern;

/**
 * Classe che rappresenta l'autore di un riferimento bibliografico.
 */
public class Author {

    private Integer id;
    private String name;
    private String ORCID;

    private final Pattern orcidPattern = Pattern.compile("^ *[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{3}[0-9x] *$");

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
        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof Author))
            return false;

        Author author = (Author) obj;

        // due autori sono uguali se hanno lo stesso nome e lo stesso orcid

        return getName().equals(author.getName()) && ((getORCID() == null && author.getORCID() == null) || (getORCID() != null && getORCID().equals(author.getORCID())));
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
     *             se {@code name} è nullo o vuoto
     */
    public void setName(String name) {
        if (isStringNullOrEmpty(name))
            throw new IllegalArgumentException("Il nome non può essere vuoto");

        this.name = name.trim();
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
        if (isStringNullOrEmpty(ORCID))
            this.ORCID = null;
        else if (orcidPattern.matcher(ORCID).matches())
            this.ORCID = ORCID.trim();
        else
            throw new IllegalArgumentException("Codice ORCID non valido");
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

    private boolean isStringNullOrEmpty(String string) {
        return string == null || string.isEmpty() || string.isBlank();
    }

}
