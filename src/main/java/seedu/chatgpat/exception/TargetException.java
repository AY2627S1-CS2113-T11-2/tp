package seedu.chatgpat.exception;

/**
 * Represents an invalid target GPA command.
 */
public class TargetException extends ChatGpatException {

  public TargetException(String message) {
    super(message);
  }
}