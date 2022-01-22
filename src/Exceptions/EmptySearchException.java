package Exceptions;

public class EmptySearchException extends Exception {

    public EmptySearchException() {
    }

    public EmptySearchException(String message) {
        super(message);
    }

}
