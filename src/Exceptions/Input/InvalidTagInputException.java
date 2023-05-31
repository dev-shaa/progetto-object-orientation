package Exceptions.Input;

/**
 * Eccezione per indicare che l'input dei tag inseriti non è valido.
 */
public class InvalidTagInputException extends InvalidInputException {

    public InvalidTagInputException() {
        super();
    }

    public InvalidTagInputException(String message) {
        super(message);
    }

    public InvalidTagInputException(Throwable cause) {
        super(cause);
    }

    public InvalidTagInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTagInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}