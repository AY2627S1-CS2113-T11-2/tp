package seedu.chatgpat.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the functionality of editing modules in a student's record.
 */
public class EditModuleTest {
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
    public void editModule_gradeOnly_updatesGradeAndKeepsOtherFields() {
        student.editModule(2, null, null, Grade.A);
        assertEquals("Module 2 updated: MA1521 | 4 MCs | Grade: A" + sep,
                outputStreamCaptor.toString());
    }

    @Test
    public void editModule_creditsAndGrade_updatesBoth() {
        student.editModule(1, null, 5, Grade.B_PLUS);
        assertEquals("Module 1 updated: CS2113 | 5 MCs | Grade: B+" + sep,
                outputStreamCaptor.toString());
    }

    @Test
    public void editModule_codeOnly_updatesCode() {
        student.editModule(1, "CS2040", null, null);
        assertEquals("Module 1 updated: CS2040 | 4 MCs | Grade: A-" + sep,
                outputStreamCaptor.toString());
    }

    @Test
    public void editModule_noFieldsGiven_printsMessage() {
        student.editModule(1, null, null, null);
        assertEquals("Provide at least one field to edit." + sep, outputStreamCaptor.toString());
    }

    @Test
    public void editModule_invalidIndex_printsInvalidIndexMessage() {
        student.editModule(3, null, null, Grade.A);
        assertEquals("Invalid module index: 3" + sep, outputStreamCaptor.toString());
    }

    @Test
    public void editModule_nonPositiveCredits_printsErrorAndKeepsModule() {
        student.editModule(1, null, 0, null);
        assertEquals("Credits must be a positive integer." + sep, outputStreamCaptor.toString());

        outputStreamCaptor.reset();
        student.printModuleList();
        String expected = "Here are your recorded modules:" + sep
                + "1. CS2113 | 4 MCs | Grade: A-" + sep
                + "2. MA1521 | 4 MCs | Grade: B+" + sep;
        assertEquals(expected, outputStreamCaptor.toString());
    }

    @Test
    public void editModule_duplicateCode_printsAlreadyExistsAndKeepsModule() {
        student.editModule(1, "MA1521", null, null);
        assertEquals("Module already exists!" + sep, outputStreamCaptor.toString());

        outputStreamCaptor.reset();
        student.printModuleList();
        String expected = "Here are your recorded modules:" + sep
                + "1. CS2113 | 4 MCs | Grade: A-" + sep
                + "2. MA1521 | 4 MCs | Grade: B+" + sep;
        assertEquals(expected, outputStreamCaptor.toString());
    }
}
