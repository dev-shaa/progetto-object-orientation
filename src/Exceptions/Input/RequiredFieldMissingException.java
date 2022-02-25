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

    public RequiredFieldMissingException(Throwable cause) {
        super(cause);
    }

    public RequiredFieldMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequiredFieldMissingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}