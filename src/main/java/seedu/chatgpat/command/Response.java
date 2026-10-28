package seedu.chatgpat.command;

import seedu.chatgpat.student.Module;

/**
 * Represents the output answering the user input, to be printed on screen.
 *
 * @param message The main message.
 * @param module  The module attached.
 * @param isExit  Whether the application should exit after printing this response.
 * @param isDataChanged Whether this successful command requires the modules to be saved.
 */
public record Response(String message, Module module, boolean isExit, boolean isDataChanged) {

    /**
     * Constructs a response for a command that does not change stored data.
     *
     * @param message The main message.
     * @param module The optional module attachment.
     * @param isExit Whether to exit after displaying the response.
     */
    public Response(String message, Module module, boolean isExit) {
        this(message, module, isExit, false);
    }

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
     * Marks a successful data-changing command so ChatGpat saves before the next prompt.
     *
     * @param message The main message.
     * @param module The module changed or removed.
     * @return A response requesting a save and keeping the application running.
     */
    public static Response forDataChange(String message, Module module) {
        return new Response(message, module, false, true);
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
