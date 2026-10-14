package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that lists every module currently recorded,
 * numbered for use with other commands.
 */
public class ListCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("list(\\s.*)?");
    private static final String MESSAGE_EMPTY = "No modules recorded yet.";
    private static final String MESSAGE_HEADER = "Here are your recorded modules:";

    /**
     * Parses the user input into a ListCommand if it matches the list format.
     * Extraneous parameters are ignored, matching the User Guide.
     *
     * @param userQuery The trimmed user input.
     * @return          The parsed ListCommand, or empty if the input does not match.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        return Optional.of(new ListCommand());
    }

    /**
     * {@inheritDoc}
     *
     * Lists every module currently recorded, numbered from 1, and
     * returns the result as a response.
     */
    @Override
    public Response execute(Student student) {
        String message = buildModuleList(student);
        return new Response(message);
    }

    /**
     * Builds the formatted module list, numbered from 1 so the same
     * index can be passed to {@code edit}, {@code delete}, {@code su},
     * and {@code unsu}.
     *
     * @param student The student whose modules are listed.
     * @return        The formatted list, or an empty-state message.
     */
    private String buildModuleList(Student student) {
        int count = student.getModuleCount();
        if (count == 0) {
            return MESSAGE_EMPTY;
        }
        StringBuilder lines = new StringBuilder();
        for (int i = 1; i <= count; i++) {
            Module module = student.getModule(i);
            if (lines.length() > 0) {
                lines.append("\n");
            }
            lines.append("  ").append(i)
                 .append(". ").append(module);
        }
        return MESSAGE_HEADER + "\n" + lines;
    }
}
