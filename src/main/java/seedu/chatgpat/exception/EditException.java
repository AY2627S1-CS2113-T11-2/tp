package seedu.chatgpat.exception;

/**
 * Represents the exception thrown when a module cannot be edited.
 */
public class EditException extends ChatGpatException {
    /**
     * Constructs a new EditException with the given message.
     *
     * @param message The detail message.
     */
    public EditException(String message) {
        super(message);
    }
}
