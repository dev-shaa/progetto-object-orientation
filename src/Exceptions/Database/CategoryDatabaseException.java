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

    public CategoryDatabaseException(Throwable cause) {
        super(cause);
    }

    public CategoryDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryDatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}