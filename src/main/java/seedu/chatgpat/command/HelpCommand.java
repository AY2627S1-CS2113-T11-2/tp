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
              help - Show this command summary.
              add m/MODULE_CODE c/CREDITS g/GRADE - Add a module to your records.
              list - List all recorded modules.
              edit INDEX [m/MODULE_CODE] [c/CREDITS] [g/GRADE] - Edit a module's details.
              delete INDEX - Delete a module.
              gpa - Show GPA, excluding SU-ed modules and non-GPA grades.
              summary - Show module counts, MCs, and GPA by semester, plus unplanned totals.
              su INDEX - Apply SU to a module and show the remaining SU quota.
              unsu INDEX - Remove SU from a module and show the remaining SU quota.
              quota - Show remaining SU quota and the modules you have SU-ed so far.
              target TARGET_GPA r/REMAINING_MCS - Show the average grade needed to reach a target GPA.
              plan INDEX y/YEAR s/SEM - Plan a module for a year and semester.
              unplan INDEX - Remove a module's semester assignment.
              filter y/YEAR s/SEM - Show the modules planned for a semester.
              filter unplanned - Show the modules not planned for any semester.
              exit - Exit ChatGPat.

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
