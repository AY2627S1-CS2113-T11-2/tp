package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that applies SU on a module and excludes it from GPA.
 */
public class SuCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("su\\s+(\\d+)");
    private static final String MESSAGE_SUCCESS =
            "Applied SU on module %d: %s\nRemaining SU quota: %d MCs.";

    private final int index;

    /**
     * Constructs a SuCommand with the given 1-based index.
     *
     * @param index The 1-based index of the module to SU.
     */
    public SuCommand(int index) {
        this.index = index;
    }

    /**
     * Parses the user input into a SuCommand if it matches the SU format.
     *
     * @param userQuery The trimmed user input.
     * @return          The parsed SuCommand, or empty if the input does not match.
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        int parsedIndex = Integer.parseInt(matcher.group(1));
        return Optional.of(new SuCommand(parsedIndex));
    }

    /**
     * {@inheritDoc}
     *
     * Applies SU on the module and returns a response showing the
     * remaining SU quota.
     */
    @Override
    public Response execute(Student student) {
        Module module = student.applySu(index);
        int remaining = student.getRemainingSuQuota();
        String message = String.format(MESSAGE_SUCCESS, index, module, remaining);
        return Response.forDataChange(message, module);
    }
}
