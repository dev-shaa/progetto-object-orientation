package RetrieveManagement.DAO;

import Entities.User;
import Exceptions.Database.UserDatabaseException;

/**
 * Interfaccia che deve essere implementata per gestire la parte di database relativo agli utenti.
 */
public interface UserDAO {

    /**
     * Salva un utente nel database.
     * 
     * @param user
     *            utente da salvare
     * @throws UserDatabaseException
     *             se il salvataggio non va a buon fine
     */
    public void register(User user) throws UserDatabaseException;

    /**
     * Controlla se un utente esiste nel database.
     * 
     * @param user
     *            utente da controllare
     * @return {@code true} se l'utente esiste
     * @throws UserDatabaseException
     *             se il recupero non va a buon fine
     */
    public boolean doesUserExist(User user) throws UserDatabaseException;
}