package seedu.chatgpat.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.chatgpat.exception.InvalidIndexException;

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
        student.addingModule(new Module("CS2113", "test", "none", 4, Grade.A_MINUS, false));
        student.addingModule(new Module("MA1521", "test", "none", 4, Grade.B_PLUS, false));
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
}
