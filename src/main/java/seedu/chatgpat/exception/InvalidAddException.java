package seedu.chatgpat.exception;

/**
 * Represents the exception thrown when an add command is malformed.
 */
public class InvalidAddException extends ChatGpatException {
    /**
     * Constructs a new InvalidAddException with the given message.
     *
     * @param message The detail message.
     */
    public InvalidAddException(String message) {
        super(message);
    }
}
