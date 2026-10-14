package seedu.chatgpat.exception;

/**
 * Represents the exception thrown when a GPA cannot be calculated.
 */
public class GpaException extends ChatGpatException {
    /**
     * Constructs a new GpaException with the given message.
     *
     * @param message The detail message.
     */
    public GpaException(String message) {
        super(message);
    }
}
