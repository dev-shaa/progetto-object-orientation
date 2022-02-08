package Entities;

import java.util.regex.Pattern;

/**
 * Classe che rappresenta l'autore di un riferimento bibliografico.
 */
public class Author {

    private Integer id;
    private String name;
    private String ORCID;

    private static final Pattern orcidPattern = Pattern.compile("^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{3}[0-9xX]$", Pattern.CASE_INSENSITIVE);

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
        setId(id);
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

        if (!(obj instanceof Author))
            return false;

        Author author = (Author) obj;

        return getName().equals(author.getName()) && ((getORCID() == null && author.getORCID() == null) || (getORCID() != null && getORCID().equals(author.getORCID())));
    }

    /**
     * Imposta l'identificativo dell'autore.
     * 
     * @param id
     *            identificativo dell'autore
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Restituisce l'identificativo dell'autore.
     * 
     * @return identificativo dell'autore
     */
    public Integer getId() {
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
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name can't be null");

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

    /**
     * Controlla se la stringa di input è un ORCID valido.
     * 
     * @param ORCID
     *            stringa da controllare
     * @return {@code true} se la stringa è nulla o se rispetta il codice ORCID.
     */
    public static boolean isORCIDValid(String ORCID) {
        if (ORCID == null || ORCID.isBlank())
            return true;

        return orcidPattern.matcher(ORCID).find();
    }

}
