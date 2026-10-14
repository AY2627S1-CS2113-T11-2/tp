package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.EditException;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Tests the {@link EditCommand} class.
 */
public class EditCommandTest {

    @Test
    public void parse_validInput_returnsCommand() {
        assertTrue(EditCommand.parse("edit 2 c/4 g/A").isPresent());
    }

    @Test
    public void parse_otherCommand_returnsEmpty() {
        assertTrue(EditCommand.parse("su 2").isEmpty());
    }

    @Test
    public void parse_missingIndex_throwsEditException() {
        assertThrows(EditException.class, () -> EditCommand.parse("edit"));
    }

    @Test
    public void parse_invalidGrade_throwsEditException() {
        assertThrows(EditException.class, () -> EditCommand.parse("edit 1 g/Z"));
    }

    @Test
    public void parse_nonNumericCredits_throwsEditException() {
        assertThrows(EditException.class, () -> EditCommand.parse("edit 1 c/abc"));
    }

    @Test
    public void execute_validEdit_returnsResponseWithEditedModule() {
        Student student = new Student();
        student.addingModule(new Module("CS2113", "test", "none", 4, Grade.A_MINUS, false));

        Response response = new EditCommand(1, null, 5, Grade.B_PLUS).execute(student);

        assertEquals("Updated module 1:", response.message());
        assertEquals("CS2113 | 5 MCs | Grade: B+", response.module().toString());
    }
}
