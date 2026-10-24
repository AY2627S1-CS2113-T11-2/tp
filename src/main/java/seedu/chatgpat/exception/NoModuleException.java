package seedu.chatgpat.exception;

/**
 * Represents the exception thrown when there are no modules to display.
 */
public class NoModuleException extends ChatGpatException {

    /**
     * Constructs a new NoModuleException.
     */
    public NoModuleException() {
        super("No modules found. Please add a module first.");
    }
}
