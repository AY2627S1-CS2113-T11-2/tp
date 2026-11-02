package seedu.chatgpat.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.exception.InvalidFormatException;

/**
 * Tests the {@link ParserUtil} class.
 */
public class ParserUtilTest {
    private static final String USAGE = "Usage: test";

    @Test
    public void parseInteger_wholeNumber_returnsNumber() {
        assertEquals(12, ParserUtil.parseInteger("12", USAGE));
    }

    @Test
    public void parseInteger_invalidNumber_throwsWithUsage() {
        InvalidFormatException exception = assertThrows(InvalidFormatException.class,
                () -> ParserUtil.parseInteger("abc", USAGE));
        assertEquals(USAGE, exception.getMessage());
        assertThrows(InvalidFormatException.class, () -> ParserUtil.parseInteger("2.5", USAGE));
        assertThrows(InvalidFormatException.class, () -> ParserUtil.parseInteger("99999999999", USAGE));
    }

    @Test
    public void parseDecimal_decimalNumber_returnsNumber() {
        assertEquals(4.5, ParserUtil.parseDecimal("4.5", USAGE));
    }

    @Test
    public void parseDecimal_invalidOrSpecialValue_throwsWithUsage() {
        assertThrows(InvalidFormatException.class, () -> ParserUtil.parseDecimal("abc", USAGE));
        assertThrows(InvalidFormatException.class, () -> ParserUtil.parseDecimal("NaN", USAGE));
        assertThrows(InvalidFormatException.class, () -> ParserUtil.parseDecimal("Infinity", USAGE));
    }
}
