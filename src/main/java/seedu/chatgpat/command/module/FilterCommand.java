package seedu.chatgpat.command.module;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.InvalidFormatException;
import seedu.chatgpat.parser.ParserUtil;
import seedu.chatgpat.student.Semester;
import seedu.chatgpat.student.Student;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Represents the command that shows the modules planned for one year and semester.
 * It must be registered after {@link FilterUnplannedCommand}, because it reports every other
 * input that starts with {@code filter} as a format error.
 */
public class FilterCommand extends Command {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("filter(\\s.*)?");
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile(
            "filter\\s+y/(\\d+)\\s+s/(\\d+)");
    private static final String MESSAGE_USAGE = "Usage: filter y/YEAR s/SEM, or filter unplanned";

    private final int year;
    private final int semester;

    /**
     * Constructs a FilterCommand for the given year and semester.
     *
     * @param year     The year of study.
     * @param semester The semester number.
     */
    public FilterCommand(int year, int semester) {
        this.year = year;
        this.semester = semester;
    }

    /**
     * Parses the user input into a FilterCommand if it starts with the filter keyword.
     *
     * @param userQuery The trimmed user input.
     * @return          The parsed FilterCommand, or empty if the input is not a filter command.
     * @throws InvalidFormatException
     *         If the input starts with filter but does not match {@code filter y/YEAR s/SEM}.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!COMMAND_PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        Matcher matcher = ARGUMENT_PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            throw new InvalidFormatException(MESSAGE_USAGE);
        }

        int year = ParserUtil.parseInteger(matcher.group(1), MESSAGE_USAGE);
        int semester = ParserUtil.parseInteger(matcher.group(2), MESSAGE_USAGE);

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
        assert indexes != null : "Student always returns a list of indexes, even if it is empty";
        if (indexes.isEmpty()) {
            return emptyMessage;
        }
        StringBuilder message = new StringBuilder(header);
        for (int index : indexes) {
            message.append(String.format("%n%d. ", index))
                    .append(student.getModule(index));
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
