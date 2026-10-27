
package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.TargetException;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that calculates the average grade point
 * required to achieve a target GPA after completing the remaining MCs.
 */
public class TargetGpaCommand extends Command {

    private static final Pattern PATTERN = Pattern.compile(
            "target\\s+(\\S+)\\s+r/(\\S+)");
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
     * Parses the user input into a TargetGpaCommand if it matches
     * the target command format.
     *
     * @param userQuery The trimmed user input.
     * @return The parsed TargetGpaCommand, or empty if the input
     *     does not match the target command format.
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);

        if (!matcher.matches()) {
            return Optional.empty();
        }
        double parsedTarget;
        int parsedRemainingMcs;
        parsedTarget = Double.parseDouble(matcher.group(1));
        parsedRemainingMcs = Integer.parseInt(matcher.group(2));
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
        String message = student.targetGPA(target, remainingMcs);
        return new Response(message);
    }
}
