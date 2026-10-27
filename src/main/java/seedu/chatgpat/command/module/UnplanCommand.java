package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.PlanException;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that removes a module from its semester plan.
 */
public class UnplanCommand extends Command {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("unplan(\\s.*)?");
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile("unplan\\s+(\\d+)");
    private static final String MESSAGE_USAGE = "Usage: unplan INDEX";
    private static final String MESSAGE_SUCCESS = "Removed module %d from its semester plan:";

    private final int index;

    /**
     * Constructs an UnplanCommand with the given 1-based index.
     *
     * @param index The 1-based index of the module to unplan.
     */
    public UnplanCommand(int index) {
        this.index = index;
    }

    /**
     * Parses the user input into an UnplanCommand if it starts with the unplan keyword.
     *
     * @param  userQuery The trimmed user input.
     * @return           The parsed UnplanCommand, or empty if the input is not an unplan command.
     * @throws PlanException
     *         If the input is an unplan command but its arguments are malformed.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!COMMAND_PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        Matcher matcher = ARGUMENT_PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            throw new PlanException(MESSAGE_USAGE);
        }
        return Optional.of(new UnplanCommand(parseIndex(matcher.group(1))));
    }

    private static int parseIndex(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new PlanException("Index must be a number, got: " + value);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Removes the module's semester and returns a response showing the updated module.
     */
    @Override
    public Response execute(Student student) {
        Module module = student.unplanModule(index);
        return new Response(String.format(MESSAGE_SUCCESS, index), module);
    }
}
