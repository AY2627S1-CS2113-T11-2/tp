package seedu.chatgpat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;

/**
 * Tests how {@link ChatInterface} reads user input and prints responses.
 */
public class ChatInterfaceTest {
    private static final String PROMPT = "> ";

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

    /**
     * Runs the given printing action and returns everything it printed to the terminal.
     */
    private String captureOutput(Runnable printingAction) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            printingAction.run();
        } finally {
            System.setOut(originalOut);
        }
        return output.toString(StandardCharsets.UTF_8);
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

    @Test
    public void printResponse_withModule_printsMessageThenModuleThenPrompt() {
        ChatInterface ui = createUiReading("");
        Module module = new Module("CS2113", "CS2113", "", 4, Grade.A_MINUS, false);

        String output = captureOutput(() -> ui.printResponse(new Response("Removed module 1:", module)));

        assertTrue(output.contains("Removed module 1:\n  CS2113 | 4 MCs | Grade: A-"));
        assertTrue(output.endsWith(PROMPT));
    }

    @Test
    public void printResponse_withWarnings_printsEachWarning() {
        ChatInterface ui = createUiReading("");

        String output = captureOutput(() -> ui.printResponse(
                new Response("Done."), List.of("Could not save modules.")));

        assertTrue(output.contains("Done."));
        assertTrue(output.contains("Warning: Could not save modules."));
    }

    @Test
    public void printResponse_exitResponse_printsNoPrompt() {
        ChatInterface ui = createUiReading("");

        String output = captureOutput(() -> ui.printResponse(new Response("Goodbye!", null, true)));

        assertTrue(output.contains("Goodbye!"));
        assertFalse(output.contains(PROMPT));
    }

    @Test
    public void printError_unexpectedException_printsApologyWithoutDetails() {
        ChatInterface ui = createUiReading("");

        String output = captureOutput(() -> ui.printError(new IllegalStateException("internal detail")));

        assertTrue(output.contains("Sorry, something went wrong with that command."));
        assertFalse(output.contains("internal detail"));
        assertTrue(output.endsWith(PROMPT));
    }

    @Test
    public void printWelcome_withWarnings_printsGreetingAndWarnings() {
        ChatInterface ui = createUiReading("");

        String output = captureOutput(() -> ui.printWelcome(List.of("Skipped line 2.")));

        assertTrue(output.contains("How may I help you?"));
        assertTrue(output.contains("Warning: Skipped line 2."));
        assertTrue(output.endsWith(PROMPT));
    }
}
