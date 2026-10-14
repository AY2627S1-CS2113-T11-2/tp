package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.GpaException;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Tests the {@link GpaCommand} class.
 */
public class GpaCommandTest {

    /**
     * Creates a module with the given code, credits, and grade.
     *
     * @param code    The module code.
     * @param credits The modular credits.
     * @param grade   The grade.
     * @return A new module.
     */
    private Module module(String code, int credits, Grade grade) {
        return new Module(code, code, "Description", credits, grade, false);
    }

    /**
     * Tests that {@code gpa} is parsed into a command.
     */
    @Test
    public void parse_gpaCommand_returnsCommand() {
        assertTrue(GpaCommand.parse("gpa").isPresent());
    }

    /**
     * Tests that extraneous arguments after {@code gpa} are ignored.
     */
    @Test
    public void parse_extraneousArguments_returnsCommand() {
        assertTrue(GpaCommand.parse("gpa 123").isPresent());
        assertTrue(GpaCommand.parse("gpa anything").isPresent());
    }

    /**
     * Tests that malformed inputs are not parsed into a command.
     */
    @Test
    public void parse_invalidInput_returnsEmpty() {
        assertTrue(GpaCommand.parse("gpas").isEmpty());
        assertTrue(GpaCommand.parse("g").isEmpty());
        assertTrue(GpaCommand.parse("").isEmpty());
    }

    /**
     * Tests that an empty student cannot produce a GPA.
     */
    @Test
    public void execute_noModules_throwsGpaException() {
        Student student = new Student();
        Command command = GpaCommand.parse("gpa").orElseThrow();
        assertThrows(GpaException.class, () -> command.execute(student));
    }

    /**
     * Tests that only SU-ed modules also cause GPA calculation to fail.
     */
    @Test
    public void execute_onlySuModules_throwsGpaException() {
        Student student = new Student();
        student.addingModule(module("CS2113", 4, Grade.A));
        student.applySu(1);
        Command command = GpaCommand.parse("gpa").orElseThrow();
        assertThrows(GpaException.class, () -> command.execute(student));
    }

    /**
     * Tests that a single graded module produces its grade point as GPA.
     */
    @Test
    public void execute_singleModuleWithA_returnsFivePointZero() {
        Student student = new Student();
        student.addingModule(module("CS2113", 4, Grade.A));
        Command command = GpaCommand.parse("gpa").orElseThrow();
        Response response = command.execute(student);
        assertTrue(response.message().contains("5.00"));
    }

    /**
     * Tests that credits are weighted correctly in the GPA calculation.
     */
    @Test
    public void execute_mixedGrades_returnsWeightedAverage() {
        Student student = new Student();
        student.addingModule(module("CS2113", 4, Grade.A));   // 5.0 * 4 = 20
        student.addingModule(module("MA1521", 4, Grade.B));   // 3.5 * 4 = 14
        student.addingModule(module("ST2334", 2, Grade.A_MINUS)); // 4.5 * 2 = 9
        // total = 43 / 10 = 4.30
        Command command = GpaCommand.parse("gpa").orElseThrow();
        Response response = command.execute(student);
        assertTrue(response.message().contains("4.30"));
    }

    /**
     * Tests that SU-ed modules are excluded from the GPA calculation.
     */
    @Test
    public void execute_suModuleExcludedFromGpa() {
        Student student = new Student();
        student.addingModule(module("CS2113", 4, Grade.A));
        student.addingModule(module("MA1521", 4, Grade.F));
        student.applySu(2);
        // Only CS2113 counts: GPA = 5.00
        Command command = GpaCommand.parse("gpa").orElseThrow();
        Response response = command.execute(student);
        assertTrue(response.message().contains("5.00"));
    }

    /**
     * Tests that CS and CU grades are excluded from GPA.
     */
    @Test
    public void execute_nonGpaGradeExcluded() {
        Student student = new Student();
        student.addingModule(module("CS2113", 4, Grade.A));
        student.addingModule(module("GEQ1000", 4, Grade.CS));
        Command command = GpaCommand.parse("gpa").orElseThrow();
        Response response = command.execute(student);
        assertTrue(response.message().contains("5.00"));
    }
}
