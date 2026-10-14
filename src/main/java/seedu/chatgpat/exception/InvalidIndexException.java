package seedu.chatgpat.exception;

/**
 * Represents the exception thrown when a module index is out of range.
 */
public class InvalidIndexException extends ChatGpatException {
    /**
     * Constructs a new InvalidIndexException with the given index and list size.
     *
     * @param index The invalid 1-based index.
     * @param size  The current number of modules.
     */
    public InvalidIndexException(int index, int size) {
        super(String.format(
                "Invalid module index: %d. You currently have %d module(s).",
                index, size));
    }
}
