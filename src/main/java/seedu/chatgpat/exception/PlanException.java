package seedu.chatgpat.exception;

/**
 * Represents the exception thrown when a module cannot be planned for a semester.
 */
public class PlanException extends ChatGpatException {
    /**
     * Constructs a new PlanException with the given message.
     *
     * @param message The detail message.
     */
    public PlanException(String message) {
        super(message);
    }
}
