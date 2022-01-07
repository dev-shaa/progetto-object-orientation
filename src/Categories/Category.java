/**
 * La classe {@code Category} rappresenta un modo di raggruppare riferimenti bibliografici
 * che condividono delle caratteristiche comuni.
 * 
 * @version 0.9
 * @author Salvatore Di Gennaro
 */
public class Category {
    private String name;
    private Category parent;

    /**
     * Crea {@code Category} con un nome e una categoria genitore (che può essere {@code null}).
     * 
     * @param name
     *            il nome della categoria
     * @param parent
     *            la categoria genitore
     * @throws IllegalArgumentException
     *             se {@code name == null} o {@code name.isBlank()}
     */
    public Category(String name, Category parent) throws IllegalArgumentException {
        try {
            setName(name);
            setParent(parent);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Imposta il nome di questa categoria.
     * 
     * @param name
     *            il nome della categoria
     * @throws IllegalArgumentException
     *             se {@code name == null} o {@code name.isBlank()}
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
     * Controlla se il nome di input può essere accettato come nome di una categoria.
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
