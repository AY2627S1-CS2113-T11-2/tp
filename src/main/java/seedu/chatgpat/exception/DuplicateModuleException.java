package seedu.chatgpat.exception;

/**
 * Represents the exception thrown when a module with the same code already exists.
 */
public class DuplicateModuleException extends ChatGpatException {
    /**
     * Constructs a new DuplicateModuleException with the given module code.
     *
     * @param moduleCode The module code that already exists.
     */
    public DuplicateModuleException(String moduleCode) {
        super("Module already exists: " + moduleCode);
    }
}
