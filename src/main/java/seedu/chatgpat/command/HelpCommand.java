package seedu.chatgpat.command;

import java.util.Optional;
import java.util.regex.Pattern;

import seedu.chatgpat.student.Student;

/**
 * Represents the command that displays the available commands and their formats.
 */
public class HelpCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("help(\\s.*)?");

    /**
     * Summary of the commands registered by default in CommandManager.
     * Update this summary when registering new commands.
     */
    private static final String MESSAGE_HELP = """
            Available commands:
              help     - Show this command summary.
              gpa      - Show GPA, excluding SU-ed modules and non-GPA grades.
              su INDEX - Apply SU to a module and show the remaining SU quota.
              exit     - Exit ChatGPat.

            INDEX is the module's position in your records, starting from 1.
            """.stripTrailing();

    /**
     * Parses a help command, ignoring extra arguments as specified in the user guide.
     *
     * @param userQuery The trimmed user input.
     * @return The parsed HelpCommand, or empty if the input does not match.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        return Optional.of(new HelpCommand());
    }

    /**
     * Returns the command summary while keeping the application running.
     *
     * @param student The student containing the module list.
     * @return The response containing the help text.
     */
    @Override
    public Response execute(Student student) {
        return new Response(MESSAGE_HELP);
    }
}
