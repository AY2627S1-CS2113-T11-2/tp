package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.InvalidFormatException;
import seedu.chatgpat.exception.SuException;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Tests the {@link UnsuCommand} class. The student starts with module 1 S/U-ed.
 */
public class UnsuCommandTest {
    private Student student;

    @BeforeEach
    public void setUp() {
        student = new Student();
        student.addModule(new Module("MA1521", "MA1521", "", 4, Grade.B_PLUS, true));
        student.addModule(new Module("CS2113", "CS2113", "", 4, Grade.A_MINUS, false));
    }

    @Test
    public void parse_validInput_returnsCommand() {
        assertTrue(UnsuCommand.parse("unsu 2").isPresent());
    }

    @Test
    public void parse_otherCommand_returnsEmpty() {
        assertTrue(UnsuCommand.parse("summary").isEmpty());
        assertTrue(UnsuCommand.parse("list").isEmpty());
    }

    @Test
    public void parse_missingOrInvalidIndex_throwsInvalidFormatException() {
        assertThrows(InvalidFormatException.class, () -> UnsuCommand.parse("unsu"));
        assertThrows(InvalidFormatException.class, () -> UnsuCommand.parse("unsu abc"));
        assertThrows(InvalidFormatException.class, () -> UnsuCommand.parse("unsu 99999999999"));
    }

    @Test
    public void execute_suModule_removesSuAndGivesCreditsBack() {
        Response response = new UnsuCommand(1).execute(student);

        assertFalse(student.getModule(1).isSu());
        assertTrue(response.isDataChanged());
        assertEquals("Removed SU on module 1: MA1521 | 4 MCs | Grade: B+\nRemaining SU quota: 28 MCs.",
                response.message());
    }

    @Test
    public void execute_moduleNotSu_throwsSuException() {
        SuException exception = assertThrows(SuException.class, () -> new UnsuCommand(2).execute(student));

        assertEquals("Module 2 is already not SU-ed.", exception.getMessage());
    }
}
