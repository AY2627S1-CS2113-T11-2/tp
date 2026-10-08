package seedu.chatgpat.exception;

/**
 * Represents the template for custom exceptions.
 */
public abstract class ChatGpatException extends RuntimeException {
    protected ChatGpatException(String message) {
        super(message);
    }
}
