package Exceptions.Input;

/**
 * Eccezione per indicare che i parametri di una ricerca sono tutti vuoti.
 */
public class EmptySearchException extends InvalidInputException {

    public EmptySearchException() {
        super();
    }

    public EmptySearchException(String message) {
        super(message);
    }

    public EmptySearchException(Throwable cause) {
        super(cause);
    }

    public EmptySearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptySearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}