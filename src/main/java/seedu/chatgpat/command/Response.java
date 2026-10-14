package seedu.chatgpat.command;

import seedu.chatgpat.student.Module;

/**
 * Represents the output answering the user input, to be printed on screen.
 *
 * @param message The main message.
 * @param module  The module attached.
 * @param isExit  Whether the application should exit after printing this response.
 */
public record Response(String message, Module module, boolean isExit) {

    /**
     * Constructs a new Response without a module attached.
     *
     * @param message The main message.
     */
    public Response(String message) {
        this(message, null);
    }

    /**
     * Constructs a response that keeps the application running.
     *
     * @param message The main message.
     * @param module  The module attached, or null if there is no attachment.
     */
    public Response(String message, Module module) {
        this(message, module, false);
    }

    /**
     * Returns whether a module is attached in this response.
     *
     * @return Whether this response has an attachment.
     */
    public boolean hasModule() {
        return (module != null);
    }
}
