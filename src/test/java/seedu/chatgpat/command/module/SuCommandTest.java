package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.exception.InvalidFormatException;

/**
 * Tests how {@link SuCommand} parses user input.
 */
public class SuCommandTest {

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
}
