package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that shows the remaining SU quota and lists
 * every module that has been SU-ed.
 */
public class QuotaCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("quota(\\s.*)?");
    private static final String MESSAGE_EMPTY = "No modules have been SU-ed yet.";
    private static final String MESSAGE_LIST_HEADER = "Modules SU-ed so far:";

    /**
     * Parses the user input into a QuotaCommand if it matches the quota format.
     * Extraneous parameters are ignored, matching the User Guide.
     *
     * @param userQuery The trimmed user input.
     * @return          The parsed QuotaCommand, or empty if the input does not match.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        return Optional.of(new QuotaCommand());
    }

    /**
     * {@inheritDoc}
     *
     * Shows the remaining SU quota and lists every module that has been SU-ed.
     */
    @Override
    public Response execute(Student student) {
        int remaining = student.getRemainingSuQuota();
        String message = String.format(
                "Remaining SU quota: %d MCs.\n%s", remaining, buildSuList(student));
        return new Response(message);
    }

    /**
     * Builds the portion of the response that lists the SU-ed modules,
     * numbered by their position in the full module list.
     *
     * @param student The student whose modules are inspected.
     * @return        The formatted list, or an empty-state message.
     */
    private String buildSuList(Student student) {
        StringBuilder lines = new StringBuilder();

        for (int i = 1; i <= student.getModuleCount(); i++) {
            Module module = student.getModule(i);
            if (module.isSu()) {
                if (lines.length() > 0) {
                    lines.append("\n");
                }
                lines.append("  ").append(i)
                     .append(". ").append(module);
            }
        }
        if (lines.length() == 0) {
            return MESSAGE_EMPTY;
        }
        return MESSAGE_LIST_HEADER + "\n" + lines;
    }
}
