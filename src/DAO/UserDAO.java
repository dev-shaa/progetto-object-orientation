package DAO;

import Entities.User;
import Exceptions.UserDatabaseException;

/**
 * TODO: commenta
 */
public interface UserDAO {
    public void register(User user) throws UserDatabaseException;

    public boolean doesUserExist(User user) throws UserDatabaseException;
}
