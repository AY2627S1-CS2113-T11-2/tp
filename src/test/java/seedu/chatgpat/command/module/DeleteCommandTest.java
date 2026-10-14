package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.InvalidIndexException;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Tests the {@link DeleteCommand} class.
 */
public class DeleteCommandTest {

    @Test
    public void parse_validInput_returnsCommand() {
        assertTrue(DeleteCommand.parse("delete 2").isPresent());
    }

    @Test
    public void parse_missingIndex_returnsEmpty() {
        assertTrue(DeleteCommand.parse("delete").isEmpty());
    }

    @Test
    public void parse_nonNumericIndex_returnsEmpty() {
        assertTrue(DeleteCommand.parse("delete abc").isEmpty());
    }

    @Test
    public void parse_otherCommand_returnsEmpty() {
        assertTrue(DeleteCommand.parse("su 2").isEmpty());
    }

    @Test
    public void execute_validIndex_returnsResponseWithRemovedModule() {
        Student student = new Student();
        student.addingModule(new Module("CS2113", "test", "none", 4, Grade.A_MINUS, false));

        Response response = new DeleteCommand(1).execute(student);

        assertEquals("Removed module 1:", response.message());
        assertEquals("CS2113", response.module().getModuleCode());
    }

    @Test
    public void execute_validIndex_removesModuleFromStudent() {
        Student student = new Student();
        student.addingModule(new Module("CS2113", "test", "none", 4, Grade.A_MINUS, false));

        new DeleteCommand(1).execute(student);

        assertThrows(InvalidIndexException.class, () -> student.getModule(1));
    }

    @Test
    public void execute_emptyList_throwsInvalidIndexException() {
        Student student = new Student();
        assertThrows(InvalidIndexException.class, () -> new DeleteCommand(1).execute(student));
    }
}
