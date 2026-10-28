package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that deletes a module from the student's record.
 */
public class DeleteCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("delete\\s+(\\d+)");
    private static final String MESSAGE_SUCCESS = "Removed module %d:";

    private final int index;

    /**
     * Constructs a DeleteCommand with the given 1-based index.
     *
     * @param index The 1-based index of the module to delete.
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Parses the user input into a DeleteCommand if it matches the delete format.
     *
     * @param userQuery The trimmed user input.
     * @return          The parsed DeleteCommand, or empty if the input does not match.
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        int parsedIndex = Integer.parseInt(matcher.group(1));
        return Optional.of(new DeleteCommand(parsedIndex));
    }

    /**
     * {@inheritDoc}
     *
     * Deletes the module and returns a response showing the module that was removed.
     */
    @Override
    public Response execute(Student student) {
        Module removed = student.deleteModule(index);
        return Response.forDataChange(String.format(MESSAGE_SUCCESS, index), removed);
    }
}
