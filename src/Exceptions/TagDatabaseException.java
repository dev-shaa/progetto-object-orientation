package Exceptions;

/**
 * Eccezione per indicare che qualcosa è andato storto nell'interazione con il database delle parole chiave.
 */
public class TagDatabaseException extends Exception {
    public TagDatabaseException() {
        super();
    }

    public TagDatabaseException(String message) {
        super(message);
    }
}
