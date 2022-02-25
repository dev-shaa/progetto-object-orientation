package Exceptions.Input;

/**
 * Eccezione per indicare che l'input degli autori inseriti non è valido.
 */
public class InvalidAuthorInputException extends InvalidInputException {

    public InvalidAuthorInputException() {
        super();
    }

    public InvalidAuthorInputException(String message) {
        super(message);
    }

    public InvalidAuthorInputException(Throwable cause) {
        super(cause);
    }

    public InvalidAuthorInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAuthorInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}