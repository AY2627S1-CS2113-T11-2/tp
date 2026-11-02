package seedu.chatgpat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * Tests how {@link ChatInterface} reads user input.
 */
public class ChatInterfaceTest {

    /**
     * Creates a ChatInterface that reads the given text instead of the keyboard.
     */
    private ChatInterface createUiReading(String input) {
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            return new ChatInterface();
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    public void getUserQuery_lineWithSpaces_returnsTrimmedLine() {
        ChatInterface ui = createUiReading("  list  \n");

        assertEquals("list", ui.getUserQuery());
    }

    @Test
    public void getUserQuery_inputEnded_returnsExit() {
        ChatInterface ui = createUiReading("");

        assertEquals("exit", ui.getUserQuery());
    }

    @Test
    public void getUserQuery_afterLastLine_returnsExit() {
        ChatInterface ui = createUiReading("list\n");

        assertEquals("list", ui.getUserQuery());
        assertEquals("exit", ui.getUserQuery());
    }
}
