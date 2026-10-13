package seedu.duke;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the functionality of deleting modules from a student's record.
 */
public class StudentTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final String sep = System.lineSeparator();
    private Student student;

    /**
     * Redirects output and creates a student with two modules before each test.
     */
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        student = new Student("Jerry", "0001", 32);
        student.addingModule(new Module("CS2113", "test", "none", 4, Grade.A_MINUS, false));
        student.addingModule(new Module("MA1521", "test", "none", 4, Grade.B_PLUS, false));
    }

    /**
     * Restores standard output after each test.
     */
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void deleteModule_validIndex_printsRemovalMessage() {
        student.deleteModule(2);
        String expected = "Removed module: MA1521 (4 MCs, Grade: B+)" + sep
                + "You now have 1 module recorded." + sep;
        assertEquals(expected, outputStreamCaptor.toString());
    }

    @Test
    public void deleteModule_validIndex_moduleNoLongerListed() {
        student.deleteModule(1);
        outputStreamCaptor.reset();
        student.printModuleList();
        String expected = "Here are your recorded modules:" + sep
                + "1. MA1521 | 4 MCs | Grade: B+" + sep;
        assertEquals(expected, outputStreamCaptor.toString());
    }

    @Test
    public void deleteModule_indexTooLarge_printsInvalidIndexMessage() {
        student.deleteModule(3);
        assertEquals("Invalid module index: 3" + sep, outputStreamCaptor.toString());
    }

    @Test
    public void deleteModule_zeroIndex_printsInvalidIndexMessage() {
        student.deleteModule(0);
        assertEquals("Invalid module index: 0" + sep, outputStreamCaptor.toString());
    }
}
