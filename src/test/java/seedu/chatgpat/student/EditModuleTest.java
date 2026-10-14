package seedu.chatgpat.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.chatgpat.exception.EditException;
import seedu.chatgpat.exception.InvalidIndexException;

/**
 * Tests the functionality of editing modules in a student's record.
 */
public class EditModuleTest {
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
    public void editModule_gradeOnly_updatesGradeAndKeepsOtherFields() {
        Module edited = student.editModule(2, null, null, Grade.A);
        assertEquals("MA1521 | 4 MCs | Grade: A", edited.toString());
    }

    @Test
    public void editModule_creditsAndGrade_updatesBoth() {
        Module edited = student.editModule(1, null, 5, Grade.B_PLUS);
        assertEquals("CS2113 | 5 MCs | Grade: B+", edited.toString());
    }

    @Test
    public void editModule_codeOnly_updatesCode() {
        Module edited = student.editModule(1, "CS2040", null, null);
        assertEquals("CS2040 | 4 MCs | Grade: A-", edited.toString());
    }

    @Test
    public void editModule_noFieldsGiven_throwsEditException() {
        assertThrows(EditException.class, () -> student.editModule(1, null, null, null));
    }

    @Test
    public void editModule_invalidIndex_throwsInvalidIndexException() {
        assertThrows(InvalidIndexException.class, () -> student.editModule(3, null, null, Grade.A));
    }

    @Test
    public void editModule_nonPositiveCredits_throwsAndKeepsModule() {
        assertThrows(EditException.class, () -> student.editModule(1, null, 0, null));
        assertEquals("CS2113 | 4 MCs | Grade: A-", student.getModule(1).toString());
    }

    @Test
    public void editModule_duplicateCode_throwsAndKeepsModule() {
        assertThrows(EditException.class, () -> student.editModule(1, "MA1521", null, null));
        assertEquals("CS2113 | 4 MCs | Grade: A-", student.getModule(1).toString());
    }
}
