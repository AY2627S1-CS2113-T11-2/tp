package seedu.chatgpat.exception;

/**
 * Represents the exception thrown when a known command is typed in the wrong format.
 * Its message is the command's usage, so that the user can see how to type it correctly.
 */
public class InvalidFormatException extends ChatGpatException {
    /**
     * Constructs a new InvalidFormatException with the given usage message.
     *
     * @param usage The correct format of the command, for example {@code Usage: delete INDEX}.
     */
    public InvalidFormatException(String usage) {
        super(usage);
    }
}
