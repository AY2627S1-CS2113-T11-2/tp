package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.InvalidIndexException;
import seedu.chatgpat.exception.PlanException;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Semester;
import seedu.chatgpat.student.Student;

/** Tests the command paired with plan, including rejected requests that must not save. */
public class UnplanCommandTest {
    @Test
    public void parse_validCommand_returnsCommand() {
        assertTrue(UnplanCommand.parse("unplan 1").orElseThrow() instanceof UnplanCommand);
        assertTrue(UnplanCommand.parse("unplan\t2").isPresent());
    }

    @Test
    public void parse_otherCommand_returnsEmpty() {
        assertTrue(UnplanCommand.parse("plan 1 y/2 s/1").isEmpty());
        assertTrue(UnplanCommand.parse("unplanned").isEmpty());
    }

    @Test
    public void parse_invalidArguments_throwsPlanException() {
        for (String query : new String[] {"unplan", "unplan x", "unplan -1", "unplan 1 extra",
            "unplan 999999999999999"}) {
            assertThrows(PlanException.class, () -> UnplanCommand.parse(query));
        }
    }

    @Test
    public void execute_plannedModule_removesOnlySemesterAndRequestsSave() {
        Student student = new Student();
        Module module = new Module("CS2113", "Software Engineering", "Team project", 4, Grade.A_MINUS, true);
        student.addModule(module);
        student.planModule(1, new Semester(2, 1));

        Response response = new UnplanCommand(1).execute(student);

        assertNull(module.getSemester());
        assertEquals(module, response.module());
        assertEquals(1, student.getModuleCount());
        assertTrue(module.isSu());
        assertEquals(Grade.A_MINUS, module.getGrade());
        assertTrue(response.isDataChanged());
        assertEquals("Removed module 1 from its semester plan:", response.message());
        assertThrows(PlanException.class, () -> new UnplanCommand(1).execute(student));
        assertThrows(InvalidIndexException.class, () -> new UnplanCommand(0).execute(student));
        assertThrows(InvalidIndexException.class, () -> new UnplanCommand(2).execute(student));
    }
}
