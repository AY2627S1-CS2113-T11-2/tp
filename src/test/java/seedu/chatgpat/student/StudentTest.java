package seedu.chatgpat.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.chatgpat.exception.InvalidIndexException;
import seedu.chatgpat.exception.TargetException;
import seedu.chatgpat.exception.GpaException;

import java.util.ArrayList;

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


    @Test
    public void targetGPA_normalCase_correctOutput() {
        String expected = String.format("Current GPA: 4.25 over 8 MCs.%n") +
                "To reach 4.5 after 8 more MCs, "
                + "you need an average grade point of 4.75 (About A)";

        String actual = student.targetGPA(4.5, 8);

        assertEquals(expected, actual);
    }

    @Test
    public void targetGPA_alreadySecured_correctOutput() {
        String expected = String.format("Current GPA: 4.25 over 8 MCs.%n") +
                "You already secured your target GPA!";

        String actual = student.targetGPA(2.0, 8);

        assertEquals(expected, actual);
    }

    @Test
    public void targetGPA_unreachable_correctOutput() {
        String expected = String.format("Current GPA: 4.25 over 8 MCs.%n") +
                "Your target GPA is unreachable! "
                + "The best you can reach is 4.5";

        String actual = student.targetGPA(5.0, 4);

        assertEquals(expected, actual);
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
    @Test
    public void targetGPA_noGradedModules_throwsGpaException() {
        Student emptyStudent = new Student();

        assertThrows(GpaException.class,
                () -> emptyStudent.targetGPA(4.5, 8));
    }

    @Test
    public void filterModule_validSemester_returnsFilteredModule() {
        student.planModule(1,new Semester(2,2));
        student.planModule(2,new Semester(3,2));
        ArrayList<Integer> expected = new ArrayList<>(List.of(1));
        assertEquals(expected,student.filterModule(new  Semester(2,2)));
    }

}

