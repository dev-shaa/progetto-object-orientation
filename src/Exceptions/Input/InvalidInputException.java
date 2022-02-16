package Exceptions.Input;

/**
 * Eccezione per indicare che l'input inserito non è valido.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException() {
        super();
    }

    public InvalidInputException(String message) {
        super(message);
    }
}
