package Entities;

/**
 * La classe {@code Category} rappresenta un modo di raggruppare riferimenti bibliografici che condividono delle caratteristiche comuni.
 */
public class Category {

    private Integer id;
    private String name;
    private Category parent;

    /**
     * Crea una categoria con un nome e nessun genitore.
     * 
     * @param name
     *            nome della categoria
     * @throws IllegalArgumentException
     *             se il nome non è valido
     */
    public Category(String name) {
        this(name, null);
    }

    /**
     * Crea una categoria con un nome e il genitore specificato.
     * 
     * @param name
     *            nome della categoria
     * @param parent
     *            categoria genitore
     * @throws IllegalArgumentException
     *             se il nome non è valido
     */
    public Category(String name, Category parent) {
        this(name, parent, null);
    }

    /**
     * Crea una categoria con un nome, il genitore e l'id specificati.
     * 
     * @param name
     *            nome della categoria
     * @param parent
     *            categoria genitore
     * @param id
     *            identificativo della categoria
     * @throws IllegalArgumentException
     *             se il nome non è valido
     */
    public Category(String name, Category parent, Integer id) {
        setName(name);
        setParent(parent);
        setID(id);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        // due categorie sono uguali se hanno lo stesso id

        if (obj == this)
            return true;

        if (!(obj instanceof Category))
            return false;

        return String.valueOf(getID()).equals(String.valueOf(((Category) obj).getID()));
    }

    /**
     * Imposta l'identificativo della categoria.
     * ATTENZIONE: dovrebbe essere chiamata solo durante la creazione di una nuova
     * categoria, quindi la funzione è stata resa privata.
     * 
     * @param id
     *            identificativo della categoria
     */
    public void setID(Integer id) {
        this.id = id;
    }

    /**
     * Restituisce l'identificativo della categoria.
     * 
     * @return
     *         identificativo della categoria
     */
    public Integer getID() {
        return id;
    }

    /**
     * Imposta il nome di questa categoria.
     * 
     * @param name
     *            nome della categoria
     * @throws IllegalArgumentException
     *             se il nome non è valido
     * @see #isNameValid(String)
     */
    public void setName(String name) {
        if (!isNameValid(name))
            throw new IllegalArgumentException("Il nome della categoria non può essere nullo.");

        this.name = name.trim();
    }

    /**
     * Restituisce il nome di questa categoria.
     * 
     * @return il nome della categoria
     */
    public String getName() {
        return this.name;
    }

    /**
     * Imposta il genitore di questa categoria
     * 
     * @param parent
     *            il genitore della categoria
     */
    public void setParent(Category parent) {
        this.parent = parent;
    }

    /**
     * Restituisce il genitore di questa categoria
     * 
     * @return il genitore della categoria
     */
    public Category getParent() {
        return this.parent;
    }

    private boolean isNameValid(String name) {
        return name != null && !name.isEmpty() && !name.isBlank();
    }

}
