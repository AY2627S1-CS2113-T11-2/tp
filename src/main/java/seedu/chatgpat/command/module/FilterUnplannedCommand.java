package seedu.chatgpat.command.module;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that lists only the modules that are not planned for any semester.
 */
public class FilterUnplannedCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("filter\\s+unplanned");
    private static final String MESSAGE_HEADER = "Modules not planned for any semester:";
    private static final String MESSAGE_ALL_PLANNED = "All modules are planned.";
    private static final String MESSAGE_NO_MODULES = "No modules recorded yet.";

    /**
     * Parses the user input into a FilterUnplannedCommand if it matches the format.
     *
     * @param userQuery The trimmed user input.
     * @return The parsed command, or empty if the input does not match.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        return Optional.of(new FilterUnplannedCommand());
    }

    /**
     * {@inheritDoc}
     *
     * Lists the modules that have no semester, each with its number in the full list.
     */
    @Override
    public Response execute(Student student) {
        List<Integer> indexes = student.filterUnplannedModules();
        String emptyMessage = (student.getModuleCount() == 0)
                ? MESSAGE_NO_MODULES
                : MESSAGE_ALL_PLANNED;
        return new Response(FilterCommand.formatResult(MESSAGE_HEADER, emptyMessage, indexes, student));
    }
}
