package marketplace.core;

public class InvalidEntityStateException extends RuntimeException {
    public InvalidEntityStateException(String message) {
        super(message);
    }
}
