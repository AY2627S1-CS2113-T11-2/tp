package seedu.chatgpat.command;

import java.util.Optional;
import java.util.regex.Pattern;

import seedu.chatgpat.student.Student;

/**
 * Represents the command that ends the application.
 */
public class ExitCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("exit(\\s.*)?");
    private static final String MESSAGE_GOODBYE = "Goodbye!";

    /**
     * Parses an exit command, ignoring extra arguments as specified in the user guide.
     *
     * @param userQuery The trimmed user input.
     * @return The parsed ExitCommand, or empty if the input does not match.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        return Optional.of(new ExitCommand());
    }

    /**
     * Returns a farewell response that tells the main loop to stop.
     *
     * @param student The student containing the module list.
     * @return The response requesting application exit.
     */
    @Override
    public Response execute(Student student) {
        return new Response(MESSAGE_GOODBYE, null, true);
    }
}
