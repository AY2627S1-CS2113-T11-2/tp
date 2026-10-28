package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
 * Tests the {@link UnplanCommand} class, including rejected requests
 * that must not save changes.
 */
public class UnplanCommandTest {
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
    public void parse_validCommand_returnsCommand() {
        assertTrue(UnplanCommand.parse("unplan 1").orElseThrow() instanceof UnplanCommand);
        assertTrue(UnplanCommand.parse("unplan\t2").isPresent());
    }

    @Test
    public void parse_otherCommand_returnsEmpty() {
        assertTrue(UnplanCommand.parse("plan 1 y/2 s/1").isEmpty());
        assertTrue(UnplanCommand.parse("unplanned").isEmpty());
        assertTrue(UnplanCommand.parse("unsu 1").isEmpty());
    }

    @Test
    public void parse_missingIndex_throwsPlanException() {
        assertThrows(PlanException.class, () -> UnplanCommand.parse("unplan"));
    }

    @Test
    public void parse_nonNumericIndex_throwsPlanException() {
        assertThrows(PlanException.class, () -> UnplanCommand.parse("unplan x"));
        assertThrows(PlanException.class, () -> UnplanCommand.parse("unplan abc"));
    }

    @Test
    public void parse_negativeIndex_throwsPlanException() {
        assertThrows(PlanException.class, () -> UnplanCommand.parse("unplan -1"));
    }

    @Test
    public void parse_extraArguments_throwsPlanException() {
        assertThrows(PlanException.class, () -> UnplanCommand.parse("unplan 1 extra"));
        assertThrows(PlanException.class, () -> UnplanCommand.parse("unplan 1 2"));
    }

    @Test
    public void parse_indexOverflow_throwsPlanException() {
        assertThrows(PlanException.class, () -> UnplanCommand.parse("unplan 999999999999999"));
    }

    @Test
    public void execute_plannedModule_removesOnlySemesterAndRequestsSave() {
        Student student = new Student();
        Module module = new Module("CS2113", "Software Engineering", "Team project",
                4, Grade.A_MINUS, true);
        student.addModule(module);
        student.planModule(1, new Semester(2, 1));

        Response response = new UnplanCommand(1).execute(student);

        assertNull(module.getSemester());
        assertEquals(module, response.module());
        assertEquals("CS2113 | 4 MCs | Grade: A-", response.module().toString());
        assertEquals(1, student.getModuleCount());
        assertTrue(module.isSu());
        assertEquals(Grade.A_MINUS, module.getGrade());
        assertTrue(response.isDataChanged());
        assertEquals("Removed module 1 from its semester plan:", response.message());
    }

    @Test
    public void execute_notPlanned_throwsPlanException() {
        assertThrows(PlanException.class, () -> new UnplanCommand(1).execute(student));
    }

    @Test
    public void execute_invalidIndex_throwsInvalidIndexException() {
        assertThrows(InvalidIndexException.class, () -> new UnplanCommand(0).execute(student));
        assertThrows(InvalidIndexException.class, () -> new UnplanCommand(2).execute(student));
    }
}
