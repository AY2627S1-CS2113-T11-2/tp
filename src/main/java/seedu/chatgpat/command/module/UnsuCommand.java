package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.InvalidFormatException;
import seedu.chatgpat.parser.ParserUtil;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that removes SU on a module and includes it into GPA.
 */
public class UnsuCommand extends Command {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("unsu(\\s.*)?");
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile("unsu\\s+(\\d+)");
    private static final String MESSAGE_USAGE = "Usage: unsu INDEX";
    private static final String MESSAGE_SUCCESS =
            "Removed SU on module %d: %s\nRemaining SU quota: %d MCs.";

    private final int index;

    /**
     * Constructs an UnsuCommand with the given 1-based index.
     *
     * @param index The 1-based index of the module to remove SU from.
     */
    public UnsuCommand(int index) {
        this.index = index;
    }

    /**
     * Parses the user input into a UnsuCommand if it starts with the unsu keyword.
     *
     * @param userQuery The trimmed user input.
     * @return          The parsed UnsuCommand, or empty if the input is not a unsu command.
     * @throws InvalidFormatException
     *         If the input starts with unsu but the index is missing or is not a whole number.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!COMMAND_PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        Matcher matcher = ARGUMENT_PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            throw new InvalidFormatException(MESSAGE_USAGE);
        }
        int parsedIndex = ParserUtil.parseInteger(matcher.group(1), MESSAGE_USAGE);
        return Optional.of(new UnsuCommand(parsedIndex));
    }

    /**
     * {@inheritDoc}
     *
     * Removes SU on the module and returns a response showing the
     * remaining SU quota.
     */
    @Override
    public Response execute(Student student) {
        Module module = student.removeSu(index);
        int remaining = student.getRemainingSuQuota();
        String message = String.format(MESSAGE_SUCCESS, index, module, remaining);
        return Response.forDataChange(message, module);
    }
}
