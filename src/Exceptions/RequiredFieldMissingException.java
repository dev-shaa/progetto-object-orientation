package Exceptions;

/**
 * Eccezione per indicare che un campo obbligatorio non è stato riempito.
 */
public class RequiredFieldMissingException extends Exception {
    public RequiredFieldMissingException() {
        super();
    }

    public RequiredFieldMissingException(String message) {
        super(message);
    }
}
