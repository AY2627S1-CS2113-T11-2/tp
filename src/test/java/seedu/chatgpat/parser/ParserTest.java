package seedu.chatgpat.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Parser} functional interface.
 */
public class ParserTest {

    /**
     * Tests that a parser lambda returning a non-empty Optional works as expected.
     */
    @Test
    public void parse_lambdaReturnsOptional_success() {
        Parser<String> parser = input -> Optional.of(input.toUpperCase());
        assertEquals(Optional.of("HELLO"), parser.parse("hello"));
    }

    /**
     * Tests that a parser lambda returning an empty Optional works as expected.
     */
    @Test
    public void parse_lambdaReturnsEmpty_returnsEmptyOptional() {
        Parser<String> parser = input -> Optional.empty();
        assertTrue(parser.parse("anything").isEmpty());
    }

    /**
     * Tests that a parser can return a value based on the input.
     */
    @Test
    public void parse_lambdaConditional_returnsCorrectOptional() {
        Parser<Integer> parser = input -> input.equals("one")
                ? Optional.of(1)
                : Optional.empty();
        assertEquals(Optional.of(1), parser.parse("one"));
        assertTrue(parser.parse("two").isEmpty());
    }
}
