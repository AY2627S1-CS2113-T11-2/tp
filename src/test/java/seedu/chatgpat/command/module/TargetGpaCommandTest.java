
package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.TargetException;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Tests the {@link TargetGpaCommand} class.
 */
public class TargetGpaCommandTest {

    /**
     * Creates a module with the given code, credits, and grade.
     *
     * @param code The module code.
     * @param credits The modular credits.
     * @param grade The grade.
     * @return A new module.
     */
    private Module module(String code, int credits, Grade grade) {
        return new Module(code, code, "Description", credits, grade, false);
    }

    /**
     * Creates a student with a GPA of 4.25 over 8 MCs.
     *
     * @return A student with two graded modules.
     */
    private Student createStudent() {
        Student student = new Student();
        student.addModule(module("CS2113", 4, Grade.A_MINUS));
        student.addModule(module("MA1521", 4, Grade.B_PLUS));
        return student;
    }

    /**
     * Tests that a valid target command is parsed successfully.
     */
    @Test
    public void parse_validInput_returnsCommand() {
        assertTrue(TargetGpaCommand.parse("target 4.5 r/8").isPresent());
    }

    /**
     * Tests that malformed command formats are not parsed.
     */
    @Test
    public void parse_invalidFormat_returnsEmpty() {
        assertTrue(TargetGpaCommand.parse("target").isEmpty());
        assertTrue(TargetGpaCommand.parse("target 4.5").isEmpty());
        assertTrue(TargetGpaCommand.parse("target 4.5 8").isEmpty());
        assertTrue(TargetGpaCommand.parse("targets 4.5 r/8").isEmpty());
        assertTrue(TargetGpaCommand.parse("").isEmpty());
    }

    /**
     * Tests that non-numeric values cause parsing to fail.
     */
    @Test
    public void parse_invalidNumber_throwsNumberFormatException() {
        assertThrows(NumberFormatException.class,
                () -> TargetGpaCommand.parse("target abc r/8"));

        assertThrows(NumberFormatException.class,
                () -> TargetGpaCommand.parse("target 4.5 r/abc"));

        assertThrows(NumberFormatException.class,
                () -> TargetGpaCommand.parse("target 4.5 r/2.5"));
    }

    /**
     * Tests the required average grade point for a valid target.
     */
    @Test
    public void execute_validTarget_returnsRequiredAverage() {
        Student student = createStudent();
        Command command = TargetGpaCommand.parse(
                "target 4.5 r/8").orElseThrow();

        Response response = command.execute(student);

        assertEquals(String.format("Current GPA: 4.25 over 8 MCs.%n") +
                        "To reach 4.5 after 8 more MCs, "
                        + "you need an average grade point of "
                        + "4.75 (About A)",
                response.message());
    }

    /**
     * Tests that an already secured target produces the correct message.
     */
    @Test
    public void execute_alreadySecured_returnsMessage() {
        Student student = createStudent();
        Command command = TargetGpaCommand.parse(
                "target 2.0 r/8").orElseThrow();

        Response response = command.execute(student);

        assertEquals(String.format("Current GPA: 4.25 over 8 MCs.%n") +
                "You already secured your target GPA!",
                response.message());
    }

    /**
     * Tests that an unreachable target reports the best achievable GPA.
     */
    @Test
    public void execute_unreachableTarget_returnsBestGpa() {
        Student student = createStudent();
        Command command = TargetGpaCommand.parse(
                "target 5.0 r/4").orElseThrow();

        Response response = command.execute(student);

        assertEquals(String.format("Current GPA: 4.25 over 8 MCs.%n") +
                "Your target GPA is unreachable! "
                        + "The best you can reach is 4.5",
                response.message());
    }

    /**
     * Tests that SU-ed modules are excluded from GPA calculation.
     */
    @Test
    public void execute_suModuleExcluded() {
        Student student = new Student();
        student.addModule(module("CS2113", 4, Grade.A));
        student.addModule(module("MA1521", 4, Grade.F));
        student.applySu(2);

        Command command = TargetGpaCommand.parse(
                "target 4.5 r/4").orElseThrow();

        Response response = command.execute(student);
        System.out.println(response.message());
        assertTrue(response.message().contains(String.format("Current GPA: 5.00 over 4 MCs.%n") +
                "To reach 4.5 after 4 more MCs, you need an average grade point of 4.00 (About B+)"));
    }

    /**
     * Tests that CS and CU grades are excluded from GPA calculation.
     */
    @Test
    public void execute_nonGpaGradesExcluded() {
        Student student = new Student();
        student.addModule(module("CS2113", 4, Grade.A));
        student.addModule(module("GEQ1000", 4, Grade.CS));
        student.addModule(module("GEQ1001", 4, Grade.CU));

        Command command = TargetGpaCommand.parse(
                "target 4.5 r/4").orElseThrow();

        Response response = command.execute(student);
        assertTrue(response.message().contains(String.format("Current GPA: 5.00 over 4 MCs.%n") +
                "To reach 4.5 after 4 more MCs, you need an average grade point of 4.00 (About B+)"));
    }

    /**
     * Tests that a target GPA above 5.0 is rejected.
     */
    @Test
    public void execute_targetAboveFive_throwsTargetException() {
        Student student = createStudent();
        Command command = TargetGpaCommand.parse(
                "target 5.5 r/8").orElseThrow();

        assertThrows(TargetException.class,
                () -> command.execute(student));
    }

    /**
     * Tests that a negative target GPA is rejected.
     */
    @Test
    public void execute_negativeTarget_throwsTargetException() {
        Student student = createStudent();
        Command command = TargetGpaCommand.parse(
                "target -1.0 r/8").orElseThrow();

        assertThrows(TargetException.class,
                () -> command.execute(student));
    }

    /**
     * Tests that zero remaining MCs are rejected.
     */
    @Test
    public void execute_zeroRemainingMcs_throwsTargetException() {
        Student student = createStudent();
        Command command = TargetGpaCommand.parse(
                "target 4.5 r/0").orElseThrow();

        assertThrows(TargetException.class,
                () -> command.execute(student));
    }

    /**
     * Tests that negative remaining MCs are rejected.
     */
    @Test
    public void execute_negativeRemainingMcs_throwsTargetException() {
        Student student = createStudent();
        Command command = TargetGpaCommand.parse(
                "target 4.5 r/-4").orElseThrow();

        assertThrows(TargetException.class,
                () -> command.execute(student));
    }
}
