package Exceptions.Input;

/**
 * Eccezione per indicare che un campo obbligatorio non è stato riempito.
 */
public class RequiredFieldMissingException extends InvalidInputException {
    public RequiredFieldMissingException() {
        super();
    }

    public RequiredFieldMissingException(String message) {
        super(message);
    }
}
