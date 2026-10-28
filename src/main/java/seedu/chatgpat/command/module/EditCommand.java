package seedu.chatgpat.command.module;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.EditException;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Represents the command that edits the code, credits and/or grade of a module.
 */
public class EditCommand extends Command {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("edit(\\s.*)?");
    private static final Pattern ARGUMENT_PATTERN =
            Pattern.compile("edit\\s+(\\d+)((?:\\s+[mcg]/\\S+)*)");
    private static final Pattern FIELD_PATTERN = Pattern.compile("([mcg])/(\\S+)");
    private static final String MESSAGE_USAGE =
            "Usage: edit INDEX [m/MODULE_CODE] [c/CREDITS] [g/GRADE]";
    private static final String MESSAGE_SUCCESS = "Updated module %d:";

    private final int index;
    private final String newCode;
    private final Integer newCredit;
    private final Grade newGrade;

    /**
     * Constructs an EditCommand. A null field is left unchanged.
     *
     * @param index     The 1-based index of the module to edit.
     * @param newCode   The new module code, or null.
     * @param newCredit The new credit value, or null.
     * @param newGrade  The new grade, or null.
     */
    public EditCommand(int index, String newCode, Integer newCredit, Grade newGrade) {
        this.index = index;
        this.newCode = newCode;
        this.newCredit = newCredit;
        this.newGrade = newGrade;
    }

    /**
     * Parses the user input into an EditCommand if it starts with the edit keyword.
     *
     * @param  userQuery The trimmed user input.
     * @return           The parsed EditCommand, or empty if the input is not an edit command.
     * @throws EditException
     *         If the input is an edit command but its arguments are malformed.
     */
    public static Optional<Command> parse(String userQuery) {
        if (!COMMAND_PATTERN.matcher(userQuery).matches()) {
            return Optional.empty();
        }
        Matcher matcher = ARGUMENT_PATTERN.matcher(userQuery);
        if (!matcher.matches()) {
            throw new EditException(MESSAGE_USAGE);
        }
        int parsedIndex = Integer.parseInt(matcher.group(1));
        String code = null;
        Integer credit = null;
        Grade grade = null;

        Matcher fieldMatcher = FIELD_PATTERN.matcher(matcher.group(2));
        while (fieldMatcher.find()) {
            String prefix = fieldMatcher.group(1);
            String value = fieldMatcher.group(2);
            if (prefix.equals("m")) {
                code = value;
            } else if (prefix.equals("c")) {
                credit = parseCredit(value);
            } else {
                grade = parseGrade(value);
            }
        }
        return Optional.of(new EditCommand(parsedIndex, code, credit, grade));
    }

    private static Integer parseCredit(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new EditException("Credits must be a positive integer.");
        }
    }

    private static Grade parseGrade(String value) {
        try {
            return Grade.fromLabel(value);
        } catch (IllegalArgumentException e) {
            throw new EditException("Invalid grade: " + value);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Edits the module and returns a response showing the updated module.
     */
    @Override
    public Response execute(Student student) {
        Module module = student.editModule(index, newCode, newCredit, newGrade);
        return Response.forDataChange(String.format(MESSAGE_SUCCESS, index), module);
    }
}
