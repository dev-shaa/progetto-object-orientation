package Entities;

/**
 * La classe {@code Category} rappresenta un modo di raggruppare riferimenti
 * bibliografici
 * che condividono delle caratteristiche comuni.
 */
public class Category {

    private int id;
    private String name;
    private Category parent;

    /**
     * Crea {@code Category} con un nome e nessun genitore.
     * 
     * @param name
     *            nome della categoria
     * @throws IllegalArgumentException
     *             se il nome non è valido
     * @see #isNameValid(String)
     */
    public Category(String name) throws IllegalArgumentException {
        setName(name);
        setParent(null);
    }

    /**
     * Crea {@code Category} con un nome, un ID e nessun genitore.
     * 
     * @param name
     *            nome della categoria
     * @param id
     *            identificativo della categoria
     * @throws IllegalArgumentException
     *             se il nome non è valido
     * @see #isNameValid(String)
     */
    public Category(String name, int id) throws IllegalArgumentException {
        setName(name);
        setParent(null);
        setId(id);
    }

    /**
     * Crea {@code Category} con un nome e una categoria genitore (che può essere
     * {@code null}).
     * 
     * @param name
     *            nome della categoria
     * @param parent
     *            categoria genitore
     * @throws IllegalArgumentException
     *             se il nome non è valido
     * @see #isNameValid(String)
     */
    public Category(String name, Category parent) throws IllegalArgumentException {
        setName(name);
        setParent(parent);
    }

    /**
     * Imposta l'identificativo della categoria.
     * ATTENZIONE: dovrebbe essere chiamata solo durante la creazione di una nuova
     * categoria, quindi la funzione è stata resa privata.
     * 
     * @param id
     *            identificativo della categoria
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce l'identificativo della categoria.
     * 
     * @return
     *         identificativo della categoria
     */
    public int getId() {
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
    public void setName(String name) throws IllegalArgumentException {
        if (!isNameValid(name))
            throw new IllegalArgumentException("Il nome della categoria non può essere nullo.");

        this.name = name;
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
     * Controlla se il nome di input può essere accettato come nome di una
     * categoria.
     * Un nome valido deve essere non nullo e non vuoto.
     * 
     * @param name
     *            nome da controllare
     * @return
     *         {@code true} se il nome è valido
     */
    public boolean isNameValid(String name) {
        return name != null && !name.isBlank();
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

    @Override
    public String toString() {
        return getName();
    }
}
