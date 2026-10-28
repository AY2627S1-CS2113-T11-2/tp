package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.InvalidAddException;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that adds a module to the student's records.
 */
public class AddCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile(
            "add\\s+m/(\\S+)\\s+c/(\\d+)\\s+g/(\\S+)");
    private static final String MESSAGE_SUCCESS =
            "New module added: %s (%d MCs, Grade: %s)\nYou now have %d module%s recorded.";

    private final String moduleCode;
    private final int credits;
    private final Grade grade;

    /**
     * Constructs an AddCommand with the given module details.
     *
     * @param moduleCode The module code.
     * @param credits    The modular credits. Must be positive.
     * @param grade      The grade.
     */
    public AddCommand(String moduleCode, int credits, Grade grade) {
        this.moduleCode = moduleCode;
        this.credits = credits;
        this.grade = grade;
    }

    /**
     * Parses the user input into an AddCommand if it matches the add format.
     *
     * @param  userQuery The trimmed user input.
     * @return           The parsed AddCommand, or empty if the input does not match.
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String moduleCode = matcher.group(1);
        String creditsStr = matcher.group(2);
        String gradeStr = matcher.group(3);

        int credits = parseCredits(creditsStr);
        Grade grade = parseGrade(gradeStr);

        return Optional.of(new AddCommand(moduleCode, credits, grade));
    }

    /**
     * Parses the credits string into a positive integer.
     *
     * @param  creditsStr The raw credits field.
     * @return            The parsed credits.
     * @throws InvalidAddException
     *         If the string is not an integer or is not positive.
     */
    private static int parseCredits(String creditsStr) {
        int credits;
        try {
            credits = Integer.parseInt(creditsStr);
        } catch (NumberFormatException e) {
            throw new InvalidAddException(
                    "Credits must be a positive integer, got: " + creditsStr);
        }
        if (credits <= 0) {
            throw new InvalidAddException(
                    "Credits must be a positive integer, got: " + creditsStr);
        }
        return credits;
    }

    /**
     * Parses the grade string into a {@link Grade}.
     *
     * @param  gradeStr The raw grade field.
     * @return          The parsed grade.
     * @throws InvalidAddException
     *         If the string does not correspond to a known grade.
     */
    private static Grade parseGrade(String gradeStr) {
        try {
            return Grade.fromLabel(gradeStr);
        } catch (IllegalArgumentException e) {
            throw new InvalidAddException("Invalid grade: " + gradeStr);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Adds the module to the student's records and returns a response
     * showing the new module count.
     */
    @Override
    public Response execute(Student student) {
        Module module = new Module(moduleCode, moduleCode, "", credits, grade, false);
        student.addModule(module);
        int count = student.getModuleCount();
        String plural = count == 1 ? "" : "s";
        String message = String.format(
                MESSAGE_SUCCESS,
                module.getModuleCode(), module.getCredit(),
                module.getGrade().getLabel(), count, plural);
        return new Response(message, module);
    }
}
