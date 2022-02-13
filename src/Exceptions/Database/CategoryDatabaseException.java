package Exceptions.Database;

/**
 * Eccezione per indicare che qualcosa è andato storto nell'interazione con il database delle categorie.
 */
public class CategoryDatabaseException extends DatabaseException {
    public CategoryDatabaseException() {
        super();
    }

    public CategoryDatabaseException(String message) {
        super(message);
    }
}
