package Exceptions;

public class RequiredFieldMissingException extends Exception {

    public RequiredFieldMissingException() {
        super();
    }

    public RequiredFieldMissingException(String message) {
        super(message);
    }

}
