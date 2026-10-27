package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.InvalidIndexException;
import seedu.chatgpat.exception.PlanException;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Semester;
import seedu.chatgpat.student.Student;

/**
 * Tests the {@link PlanCommand} class.
 */
public class PlanCommandTest {
    private Student student;

    /**
     * Creates a student with one unplanned module before each test.
     */
    @BeforeEach
    public void setUp() {
        student = new Student();
        student.addModule(new Module("CS2113", "test", "none", 4, Grade.A_MINUS, false));
    }

    @Test
    public void parse_validInput_returnsCommand() {
        assertTrue(PlanCommand.parse("plan 1 y/2 s/1").isPresent());
    }

    @Test
    public void parse_otherCommand_returnsEmpty() {
        assertTrue(PlanCommand.parse("unplan 1").isEmpty());
        assertTrue(PlanCommand.parse("list").isEmpty());
    }

    @Test
    public void parse_missingFields_throwsPlanException() {
        assertThrows(PlanException.class, () -> PlanCommand.parse("plan 1 y/2"));
    }

    @Test
    public void parse_nonNumericYear_throwsPlanException() {
        assertThrows(PlanException.class, () -> PlanCommand.parse("plan 1 y/abc s/1"));
    }

    @Test
    public void parse_yearOutOfRange_throwsPlanException() {
        assertThrows(PlanException.class, () -> PlanCommand.parse("plan 1 y/5 s/1"));
    }

    @Test
    public void parse_termOutOfRange_throwsPlanException() {
        assertThrows(PlanException.class, () -> PlanCommand.parse("plan 1 y/2 s/3"));
    }

    @Test
    public void execute_validIndex_returnsPlannedModule() {
        Response response = new PlanCommand(1, new Semester(2, 1)).execute(student);

        assertEquals("Planned module 1 for Y2S1:", response.message());
        assertEquals("CS2113 | 4 MCs | Grade: A- | Y2S1", response.module().toString());
    }

    @Test
    public void execute_alreadyPlanned_movesModule() {
        new PlanCommand(1, new Semester(2, 1)).execute(student);
        Response response = new PlanCommand(1, new Semester(3, 2)).execute(student);

        assertEquals("CS2113 | 4 MCs | Grade: A- | Y3S2", response.module().toString());
    }

    @Test
    public void execute_invalidIndex_throwsInvalidIndexException() {
        assertThrows(InvalidIndexException.class,
                () -> new PlanCommand(2, new Semester(2, 1)).execute(student));
    }
}
