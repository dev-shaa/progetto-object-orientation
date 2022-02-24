package Entities;

/**
 * La classe {@code Category} rappresenta un modo di raggruppare riferimenti bibliografici che condividono delle caratteristiche comuni.
 */
public class Category {

    private Integer id;
    private String name;
    private Category parent;

    private final int NAME_MAX_LENGTH = 64;

    /**
     * Crea una categoria con un nome e nessun genitore.
     * 
     * @param name
     *            nome della categoria
     * @throws IllegalArgumentException
     *             se il nome non è valido
     * @see #setName(String)
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
     * @see #setName(String)
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
     * @see #setName(String)
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
        // due categorie sono uguali se hanno lo stesso nome e lo stesso genitore

        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof Category))
            return false;

        Category parent = getParent();
        Category otherCategory = (Category) obj;
        Category otherCategoryParent = otherCategory.getParent();

        return getName().equalsIgnoreCase(otherCategory.getName())
                && (parent == null && otherCategoryParent == null) || (parent != null && parent.equals(otherCategoryParent));
    }

    /**
     * Imposta l'identificativo della categoria.
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
     *             se il nome è nullo, vuoto o più lungo di {@link #NAME_MAX_LENGTH}
     */
    public void setName(String name) {
        name = name.trim();

        if (isNameValid(name))
            this.name = name;
        else
            throw new IllegalArgumentException("Il nome non può essere nullo o più lungo di " + NAME_MAX_LENGTH + " caratteri.");
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
     * Imposta il genitore della categoria.
     * 
     * @param parent
     *            il genitore della categoria
     * @throws IllegalArgumentException
     *             se viene a crearsi una dipendenza ciclica
     */
    public void setParent(Category parent) {
        if (parent != null && parent.isDescendantOf(this))
            throw new IllegalArgumentException("Non è possibile introdurre una dipendenza ciclica.");

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

    /**
     * Controlla se questa categoria è discendente della categoria indicata.
     * 
     * @param category
     * @return {@code true} se questa categoria è discendente di {@code category}
     */
    public boolean isDescendantOf(Category category) {
        if (category == null)
            return false;

        Category parent = getParent();

        while (parent != null) {
            if (parent.equals(category))
                return true;

            parent = parent.getParent();
        }

        return false;
    }

    private boolean isNameValid(String name) {
        return name != null && !name.isEmpty() && !name.isBlank() && name.length() <= NAME_MAX_LENGTH;
    }

}