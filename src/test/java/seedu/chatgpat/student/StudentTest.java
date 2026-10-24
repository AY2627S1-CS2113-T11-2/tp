package seedu.chatgpat.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.chatgpat.exception.InvalidIndexException;
import seedu.chatgpat.exception.TargetException;


/**
 * Tests the functionality of deleting modules from a student's record.
 */
public class StudentTest {
    private Student student;

    /**
     * Creates a student with two modules before each test.
     */
    @BeforeEach
    public void setUp() {
        student = new Student("Jerry", "0001", 32);
        student.addModule(new Module("CS2113", "test", "none", 4, Grade.A_MINUS, false));
        student.addModule(new Module("MA1521", "test", "none", 4, Grade.B_PLUS, false));
    }

    @Test
    public void deleteModule_validIndex_returnsRemovedModule() {
        Module removed = student.deleteModule(2);
        assertEquals("MA1521", removed.getModuleCode());
    }

    @Test
    public void deleteModule_validIndex_moduleNoLongerListed() {
        student.deleteModule(1);
        assertEquals("MA1521", student.getModule(1).getModuleCode());
        assertThrows(InvalidIndexException.class, () -> student.getModule(2));
    }

    @Test
    public void deleteModule_indexTooLarge_throwsInvalidIndexException() {
        assertThrows(InvalidIndexException.class, () -> student.deleteModule(3));
    }

    @Test
    public void deleteModule_zeroIndex_throwsInvalidIndexException() {
        assertThrows(InvalidIndexException.class, () -> student.deleteModule(0));
    }

    private String captureTargetOutput(double target, int remainingMcs) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(
                    output, true, StandardCharsets.UTF_8));

            student.targetGPA(target, remainingMcs);

            return output.toString(StandardCharsets.UTF_8);
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void targetGPA_normalCase_correctOutput() {
        String output = captureTargetOutput(4.5, 8);
        System.out.println("Actual output:");
        System.out.println(output);
        assertTrue(output.contains("Current GPA:4.25"));
        assertTrue(output.contains(
                "average grade point of 4.75"));
        assertTrue(output.contains("(About A)"));
    }

    @Test
    public void targetGPA_alreadySecured_correctOutput() {

        String output = captureTargetOutput(2.0, 8);

        assertTrue(output.contains(
                "You already secured your target GPA!"));
    }

    @Test
    public void targetGPA_unreachable_correctOutput() {

        String output = captureTargetOutput(5.0, 4);

        assertTrue(output.contains(
                "Your target GPA is unreachable!"));
        assertTrue(output.contains(
                "The best you can reach is 4.5"));
    }

    @Test
    public void targetGPA_targetAboveFive_throwsException() {
        assertThrows(TargetException.class,
                () -> student.targetGPA(5.5, 8));
    }

    @Test
    public void targetGPA_negativeTarget_throwsException() {
        assertThrows(TargetException.class,
                () -> student.targetGPA(-1.0, 8));
    }

    @Test
    public void targetGPA_zeroRemainingMcs_throwsException() {
        assertThrows(TargetException.class,
                () -> student.targetGPA(4.5, 0));
    }

    @Test
    public void targetGPA_negativeRemainingMcs_throwsException() {
        assertThrows(TargetException.class,
                () -> student.targetGPA(4.5, -4));
    }
}
