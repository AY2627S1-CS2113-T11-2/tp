package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.PlanException;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Semester;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that assigns a module to a semester.
 */
public class PlanCommand extends Command {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("plan(\\s.*)?");
    private static final Pattern ARGUMENT_PATTERN =
            Pattern.compile("plan\\s+(\\d+)\\s+y/(\\S+)\\s+s/(\\S+)");
    private static final String MESSAGE_USAGE = "Usage: plan INDEX y/YEAR s/SEM";
    private static final String MESSAGE_SUCCESS = "Planned module %d for %s:";

    private final int index;
    private final Semester semester;

    /**
     * Constructs a PlanCommand with the given index and semester.
     *
     * @param index    The 1-based index of the module to plan.
     * @param semester The semester to plan the module for.
     */
    public PlanCommand(int index, Semester semester) {
        this.index = index;
        this.semester = semester;
    }

    /**
     * Parses the user input into a PlanCommand if it starts with the plan keyword.
     *
     * @param  userQuery The trimmed user input.
     * @return           The parsed PlanCommand, or empty if the input is not a plan command.
     * @throws PlanException
     *         If the input is a plan command but its arguments are malformed or out of range.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!COMMAND_PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        Matcher matcher = ARGUMENT_PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            throw new PlanException(MESSAGE_USAGE);
        }
        int parsedIndex = parseNumber(matcher.group(1), "Index");
        int year = parseNumber(matcher.group(2), "Year");
        int term = parseNumber(matcher.group(3), "Semester");
        return Optional.of(new PlanCommand(parsedIndex, new Semester(year, term)));
    }

    private static int parseNumber(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new PlanException(fieldName + " must be a number, got: " + value);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Plans the module for the semester and returns a response showing the updated module.
     */
    @Override
    public Response execute(Student student) {
        Module module = student.planModule(index, semester);
        return Response.forDataChange(String.format(MESSAGE_SUCCESS, index, semester), module);
    }
}
