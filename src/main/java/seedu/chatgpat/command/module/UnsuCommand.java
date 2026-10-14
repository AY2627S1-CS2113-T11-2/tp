package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that removes SU on a module and includes it into GPA.
 */
public class UnsuCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("unsu\\s+(\\d+)");
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
     * Parses the user input into a UnsuCommand if it matches the unsu format.
     *
     * @param userQuery The trimmed user input.
     * @return          The parsed UnsuCommand, or empty if the input does not match.
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        int parsedIndex = Integer.parseInt(matcher.group(1));
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
        return new Response(message, module);
    }
}
