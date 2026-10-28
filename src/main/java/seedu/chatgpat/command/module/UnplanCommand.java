package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.PlanException;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/** Removes a module's semester assignment while keeping the module in the records. */
public class UnplanCommand extends Command {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("unplan(\\s.*)?");
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile("unplan\\s+(\\d+)");
    private static final String MESSAGE_USAGE = "Usage: unplan INDEX";
    private final int index;

    /**
     * Constructs the command for the given 1-based module index.
     *
     * @param index The module to unplan.
     */
    public UnplanCommand(int index) {
        this.index = index;
    }

    /**
     * Parses an unplan command, reporting invalid arguments as a planning error.
     *
     * @param userQuery The trimmed input.
     * @return The command, or empty if another command should handle the input.
     * @throws PlanException If the index is missing, malformed or too large to parse.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!COMMAND_PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        Matcher matcher = ARGUMENT_PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            throw new PlanException(MESSAGE_USAGE);
        }
        try {
            return Optional.of(new UnplanCommand(Integer.parseInt(matcher.group(1))));
        } catch (NumberFormatException e) {
            throw new PlanException(MESSAGE_USAGE);
        }
    }

    @Override
    public Response execute(Student student) {
        Module module = student.unplanModule(index);
        return Response.forDataChange("Removed module " + index + " from its semester plan:", module);
    }
}
