package seedu.chatgpat.exception;

/**
 * Represents the exception thrown when an SU operation cannot be completed.
 */
public class SuException extends ChatGpatException {
    /**
     * Constructs a new SuException with the given message.
     *
     * @param message The detail message.
     */
    public SuException(String message) {
        super(message);
    }
}
