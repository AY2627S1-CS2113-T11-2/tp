package seedu.chatgpat.command.module;

import java.util.Locale;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.SemesterSummary;
import seedu.chatgpat.student.Student;

/**
 * Shows module counts, MC totals, and GPA by semester, followed by unplanned totals.
 */
public class SummaryCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("summary(\\s.*)?");

    /**
     * Parses a summary command, ignoring extra arguments like other commands with no parameters.
     *
     * @param userQuery The trimmed user input.
     * @return The parsed command, or empty if the input does not match.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        return Optional.of(new SummaryCommand());
    }

    /**
     * Returns one line per occupied semester in chronological order, then an unplanned line.
     * Semesters with no GPA-contributing credits show {@code GPA n/a}.
     *
     * @param student The student whose modules are summarised.
     * @return The summary response, keeping the application running.
     */
    @Override
    public Response execute(Student student) {
        StringJoiner lines = new StringJoiner("\n");
        for (SemesterSummary summary : student.getSemesterSummaries()) {
            String label = summary.semester() == null ? "Unplanned" : summary.semester().toString();
            String moduleLabel = summary.moduleCount() == 1 ? "module" : "modules";
            String line = String.format(Locale.ROOT, "%s | %d %s | %d MCs",
                    label, summary.moduleCount(), moduleLabel, summary.totalCredits());
            if (summary.semester() != null) {
                String gpa = summary.gpa().isPresent()
                        ? String.format(Locale.ROOT, "%.2f", summary.gpa().getAsDouble()) : "n/a";
                line += " | GPA " + gpa;
            }
            lines.add(line);
        }
        return new Response(lines.toString());
    }
}
