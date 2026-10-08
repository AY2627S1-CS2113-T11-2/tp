package seedu.chatgpat.command;

import seedu.chatgpat.module.Module;

/**
 * Represents the output answering the user input, to be printed on screen.
 *
 * @param message The main message.
 * @param module  The module attached.
 */
public record Response(String message, Module module) {

    /**
     * Constructs a new Response without a module attached.
     *
     * @param message The main message.
     */
    public Response(String message) {
        this(message, null);
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
