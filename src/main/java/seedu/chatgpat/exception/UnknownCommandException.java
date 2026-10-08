package seedu.chatgpat.exception;

/**
 * Represents the exception thrown when user input cannot be parsed into any known command.
 */
public class UnknownCommandException extends ChatGpatException {
    /**
     * Constructs a new UnknownCommandException with the unknown command included.
     *
     * @param command The unknown command entered by the user.
     */
    public UnknownCommandException(String command) {
        super("Unknown command detected: " + command);
    }
}
