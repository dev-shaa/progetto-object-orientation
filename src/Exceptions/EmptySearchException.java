package Exceptions;

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
}
