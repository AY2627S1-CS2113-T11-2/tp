package seedu.chatgpat.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.student.Student;

/**
 * Tests parsing and execution of the exit command.
 */
public class ExitCommandTest {
    @Test
    public void parse_exitCommand_returnsCommand() {
        assertTrue(ExitCommand.parse("exit").isPresent());
    }

    @Test
    public void parse_extraArguments_returnsCommand() {
        assertTrue(ExitCommand.parse("exit 123").isPresent());
        assertTrue(ExitCommand.parse("exit\tanything").isPresent());
    }

    @Test
    public void parse_invalidInput_returnsEmpty() {
        assertTrue(ExitCommand.parse("").isEmpty());
        assertTrue(ExitCommand.parse("exits").isEmpty());
        assertTrue(ExitCommand.parse("exit123").isEmpty());
        assertTrue(ExitCommand.parse("gpa").isEmpty());
    }

    @Test
    public void execute_exitCommand_returnsFarewellAndExitRequest() {
        Command command = ExitCommand.parse("exit").orElseThrow();

        Response response = command.execute(new Student());

        assertTrue(response.isExit());
        assertEquals("Goodbye!", response.message());
        assertFalse(response.hasModule());
    }
}
