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
 * Tests the {@link SuCommand} class. The student starts with the default S/U quota of 28 MCs.
 */
public class SuCommandTest {
    private Student student;

    @BeforeEach
    public void setUp() {
        student = new Student();
        student.addModule(new Module("MA1521", "MA1521", "", 4, Grade.B_PLUS, false));
        student.addModule(new Module("CP3880", "CP3880", "", 30, Grade.A, false));
    }

    @Test
    public void parse_validInput_returnsCommand() {
        assertTrue(SuCommand.parse("su 2").isPresent());
    }

    @Test
    public void parse_otherCommand_returnsEmpty() {
        assertTrue(SuCommand.parse("summary").isEmpty());
        assertTrue(SuCommand.parse("list").isEmpty());
    }

    @Test
    public void parse_missingOrInvalidIndex_throwsInvalidFormatException() {
        assertThrows(InvalidFormatException.class, () -> SuCommand.parse("su"));
        assertThrows(InvalidFormatException.class, () -> SuCommand.parse("su abc"));
        assertThrows(InvalidFormatException.class, () -> SuCommand.parse("su 99999999999"));
    }

    @Test
    public void execute_moduleFitsQuota_appliesSuAndShowsRemainingQuota() {
        Response response = new SuCommand(1).execute(student);

        assertTrue(student.getModule(1).isSu());
        assertTrue(response.isDataChanged());
        assertEquals("Applied SU on module 1: MA1521 | 4 MCs | Grade: B+ [S/U]\nRemaining SU quota: 24 MCs.",
                response.message());
    }

    @Test
    public void execute_moduleAlreadySu_throwsSuException() {
        new SuCommand(1).execute(student);

        SuException exception = assertThrows(SuException.class, () -> new SuCommand(1).execute(student));
        assertEquals("Module 1 is already SU-ed.", exception.getMessage());
    }

    @Test
    public void execute_creditsAboveRemainingQuota_throwsSuExceptionAndKeepsModule() {
        SuException exception = assertThrows(SuException.class, () -> new SuCommand(2).execute(student));

        assertEquals("Insufficient SU quota. Remaining: 28 MCs, required: 30 MCs.", exception.getMessage());
        assertEquals(28, student.getRemainingSuQuota());
        assertFalse(student.getModule(2).isSu());
    }
}
