package seedu.chatgpat.service;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.chatgpat.command.Response;

/**
 * Handles taking the user inputs and printing the responses.
 */
public class ChatInterface {
    private static final int BAR_LENGTH = 72;
    private static final String EXIT_QUERY = "exit";
    private static final String DIVIDER = "_".repeat(BAR_LENGTH) + "\n\n> ";
    private static final String BANNER =
        """

         ██████╗██╗  ██╗ █████╗ ████████╗ ██████╗ ██████╗  █████╗ ████████╗
        ██╔════╝██║  ██║██╔══██╗╚══██╔══╝██╔════╝ ██╔══██╗██╔══██╗╚══██╔══╝
        ██║     ███████║███████║   ██║   ██║  ███╗██████╔╝███████║   ██║
        ██║     ██╔══██║██╔══██║   ██║   ██║   ██║██╔═══╝ ██╔══██║   ██║
        ╚██████╗██║  ██║██║  ██║   ██║   ╚██████╔╝██║     ██║  ██║   ██║
         ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝     ╚═╝  ╚═╝   ╚═╝
        """;

    private final Scanner scanner = new Scanner(System.in);
    private final Logger logger = Logger.getLogger(ChatInterface.class.getName());

    /**
     * Prints startup warnings before the first command prompt.
     *
     * @param warnings Problems encountered while loading saved modules.
     */
    public void printWelcome(List<String> warnings) {
        System.out.print(BANNER + "\nHow may I help you?\n");
        printWarnings(warnings);
        System.out.print(DIVIDER);
    }

    /**
     * Formats and prints the response answering user input.
     * Prints another command prompt only when the application will keep running.
     *
     * @param response The response answering user input.
     */
    public void printResponse(Response response) {
        printResponse(response, List.of());
    }

    /**
     * Displays a command result and any save failure before printing the next prompt.
     *
     * @param response The command result.
     * @param warnings Storage errors to display alongside the result.
     */
    public void printResponse(Response response, List<String> warnings) {
        if (response.hasModule()) {
            System.out.print("\n" + response.message() + "\n  " + response.module() + "\n");
        } else {
            System.out.print("\n" + response.message() + "\n");
        }
        printWarnings(warnings);
        if (!response.isExit()) {
            System.out.print(DIVIDER);
        }
    }

    /** Displays storage diagnostics here so persistence never writes to the console. */
    private void printWarnings(List<String> warnings) {
        for (String warning : warnings) {
            System.out.println("Warning: " + warning);
        }
    }

    /**
     * Prints a generic error message and logs the given exception.
     *
     * @param e The exception to log.
     */
    public void printError(Exception e) {
        System.out.print("\nSorry, something went wrong with that command. Please check it and try again.\n");
        logger.log(Level.WARNING, "Unchecked exception caught.", e);
        System.out.print(DIVIDER);
    }

    /**
     * Cleans and retrieves the user input.
     * If the input has ended (for example, the user pressed Ctrl+D, or Ctrl+Z on Windows),
     * there is nothing more to read, so the exit command is returned to end the program normally.
     *
     * @return Trimmed user input, or the exit command if the input has ended.
     */
    public String getUserQuery() {
        if (!scanner.hasNextLine()) {
            return EXIT_QUERY;
        }
        return scanner.nextLine().trim();
    }
}
