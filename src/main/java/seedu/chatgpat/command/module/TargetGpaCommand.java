
package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.InvalidFormatException;
import seedu.chatgpat.exception.TargetException;
import seedu.chatgpat.parser.ParserUtil;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that calculates the average grade point
 * required to achieve a target GPA after completing the remaining MCs.
 */
public class TargetGpaCommand extends Command {

    private static final Pattern COMMAND_PATTERN = Pattern.compile("target(\\s.*)?");
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile(
            "target\\s+(\\S+)\\s+r/(\\S+)");
    private static final String MESSAGE_USAGE = "Usage: target TARGET_GPA r/REMAINING_MCS "
            + "(TARGET_GPA is a number such as 4.5, REMAINING_MCS is a whole number such as 40)";

    private final double target;
    private final int remainingMcs;

    /**
     * Constructs a TargetGpaCommand with the given target GPA
     * and remaining modular credits.
     *
     * @param target The GPA that the student wants to achieve.
     * @param remainingMcs The number of remaining modular credits.
     */
    public TargetGpaCommand(double target, int remainingMcs) {
        this.target = target;
        this.remainingMcs = remainingMcs;
    }

    /**
     * Parses the user input into a TargetGpaCommand if it starts with the target keyword.
     *
     * @param userQuery The trimmed user input.
     * @return The parsed TargetGpaCommand, or empty if the input is not a target command.
     * @throws InvalidFormatException If the input starts with target but a field is missing
     *     or is not a valid number.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!COMMAND_PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        Matcher matcher = ARGUMENT_PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            throw new InvalidFormatException(MESSAGE_USAGE);
        }
        double parsedTarget = ParserUtil.parseDecimal(matcher.group(1), MESSAGE_USAGE);
        int parsedRemainingMcs = ParserUtil.parseInteger(matcher.group(2), MESSAGE_USAGE);
        return Optional.of(
                new TargetGpaCommand(parsedTarget, parsedRemainingMcs));
    }

    /**
     * Calculates the required average grade point using the student's
     * existing academic records and returns the result as a response.
     *
     * @param student The student whose academic records are used.
     * @return A response containing the target GPA calculation result.
     * @throws TargetException If the target GPA or remaining MCs
     *         are outside the valid range.
     */
    @Override
    public Response execute(Student student) {
        String message = student.getTargetGpaMessage(target, remainingMcs);
        return new Response(message);
    }
}
