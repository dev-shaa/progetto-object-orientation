/**
 * Classe astratta che si occupa di gestire la parte di database relativo alle categorie.
 */
public abstract class CategoryDAO {

    protected User user;

    /**
     * Crea {@code CategoryDAO} per interfacciarsi al database relativo alle categorie dell'utente.
     * 
     * @param user
     *            l'utente che accede al database
     * @throws IllegalArgumentException
     *             se l'utente di input è {@code null}
     */
    public CategoryDAO(User user) throws IllegalArgumentException {
        setUser(user);
    }

    /**
     * Imposta l'utente di cui recuperare le categorie.
     * 
     * @param user
     *            l'utente di cui recuperare le categorie.
     * @throws IllegalArgumentException
     *             se l'utente di input è {@code null}
     */
    public void setUser(User user) throws IllegalArgumentException {
        if (user == null)
            throw new IllegalArgumentException("L'utente non può essere nullo.");

        this.user = user;
    }

    /**
     * Restituisce l'utente che accede al database.
     * 
     * @return l'utente che accede al database
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Salva una nuova categoria nel database.
     * 
     * @param category
     *            Nuova categoria da salvare.
     * @throws CategoryDatabaseException
     *             se l'aggiunta della categoria al database non va a buon fine
     * @since 0.1
     */
    public abstract void addCategory(Category category) throws CategoryDatabaseException;

    /**
     * Modifica una categoria nel database.
     * 
     * @param category
     *            Categoria da modificare.
     * @param newName
     *            Nuovo nome da assegnare alla categoria.
     * @throws CategoryDatabaseException
     *             se la modifica della categoria nel database non va a buon fine
     * @since 0.1
     */
    public abstract void changeCategory(Category category, String newName) throws CategoryDatabaseException;

    /**
     * Elimina una categoria nel database.
     * 
     * @param category
     *            Categoria da eliminare.
     * @throws CategoryDatabaseException
     *             se la rimozione della categoria dal database non va a buon fine
     * @since 0.1
     */
    public abstract void removeCategory(Category category) throws CategoryDatabaseException;

    /**
     * Ottiene tutte le categorie appartenenti a un utente nel database.
     * 
     * @return Lista contenente tutte le categorie dell'utente.
     * @throws CategoryDatabaseException
     *             se il recupero delle categorie dal database non va a buon fine
     * @since 0.2
     */
    public abstract Category[] getUserCategories() throws CategoryDatabaseException;
}
