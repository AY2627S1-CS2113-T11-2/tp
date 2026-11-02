package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.exception.InvalidFormatException;

/**
 * Tests how {@link UnsuCommand} parses user input.
 */
public class UnsuCommandTest {

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
}
