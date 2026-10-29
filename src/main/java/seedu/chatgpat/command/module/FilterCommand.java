package seedu.chatgpat.command.module;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Semester;
import seedu.chatgpat.student.Student;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FilterCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile(
            "filter\\s+y/(\\d+)\\s+s/(\\d+)");
    private final int year;
    private final int semester;
    public FilterCommand(int year, int semester) {
        this.year = year;
        this.semester = semester;
    }
    /**
     * Parses the user input into a filterCommand if it matches the filter format.
     * @param userQuery The trimmed user input.
     * @return          The parsed filterCommand, or empty if the input does not match.
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);

        if (!matcher.matches()) {
            return Optional.empty();
        }

        int year = Integer.parseInt(matcher.group(1));
        int semester = Integer.parseInt(matcher.group(2));

        return Optional.of(new FilterCommand(year, semester));
    }

    /**
     * Builds the response text for a filtered list of modules.
     *
     * @param header       The line shown before the matching modules.
     * @param emptyMessage The message shown when no module matches.
     * @param indexes      The 1-based indexes, in the full list, of the matching modules.
     * @param student      The student whose modules are listed.
     * @return The formatted message.
     */
    static String formatResult(String header, String emptyMessage,
                               List<Integer> indexes, Student student) {
        if (indexes.isEmpty()) {
            return emptyMessage;
        }
        StringBuilder message = new StringBuilder(header + String.format("%n"));
        for (int index : indexes) {
            message.append(String.format("%d. ", index))
                    .append(student.getModule(index))
                    .append(String.format("%n"));
        }
        return message.toString();
    }

    @Override
    public Response execute(Student student) {
        Semester semester = new Semester(this.year, this.semester);
        String label = semester.toString();
        String message = formatResult(
                String.format("Modules in %s:", label),
                String.format("No modules found in %s.", label),
                student.filterModule(semester),
                student);
        return new Response(message);
    }
}
