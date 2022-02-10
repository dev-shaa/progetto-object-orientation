package Exceptions;

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

}
