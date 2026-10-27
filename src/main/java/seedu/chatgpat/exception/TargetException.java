package seedu.chatgpat.exception;

/**
 * Represents an invalid target GPA command.
 * i.e. target GPA is not between 0.0 and 5.0 or remaining MCs <= 0
 */
public class TargetException extends ChatGpatException {
    public TargetException(String message) {
        super(message);
    }
}
