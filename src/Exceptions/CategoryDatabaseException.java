package Exceptions;

/**
 * Eccezione per indicare che qualcosa è andato storto nell'interazione con il database delle categorie.
 */
public class CategoryDatabaseException extends Exception {
    public CategoryDatabaseException() {
        super();
    }

    public CategoryDatabaseException(String message) {
        super(message);
    }
}
