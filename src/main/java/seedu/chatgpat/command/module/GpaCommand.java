package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that shows the student's calculated GPA,
 * excluding SU-ed modules.
 */
public class GpaCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile("gpa(\\s.*)?");
    private static final String MESSAGE_SUCCESS = "Your GPA is %.2f";

    /**
     * Parses the user input into a GpaCommand if it matches the gpa format.
     *
     * @param userQuery The trimmed user input.
     * @return          The parsed GpaCommand, or empty if the input does not match.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        return Optional.of(new GpaCommand());
    }

    /**
     * {@inheritDoc}
     *
     * Calculates the student's GPA, excluding SU-ed modules, and returns
     * the result as a response.
     */
    @Override
    public Response execute(Student student) {
        double gpa = student.calculateGpa();
        String message = String.format(MESSAGE_SUCCESS, gpa);
        return new Response(message);
    }
}
