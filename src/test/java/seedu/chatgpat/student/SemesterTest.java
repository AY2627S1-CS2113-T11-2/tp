package seedu.chatgpat.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.exception.PlanException;

/**
 * Tests the {@link Semester} record.
 */
public class SemesterTest {

    @Test
    public void toString_validValues_formatsAsYearAndSemester() {
        assertEquals("Y2S1", new Semester(2, 1).toString());
    }

    @Test
    public void constructor_yearTooLow_throwsPlanException() {
        assertThrows(PlanException.class, () -> new Semester(0, 1));
    }

    @Test
    public void constructor_yearTooHigh_throwsPlanException() {
        assertThrows(PlanException.class, () -> new Semester(5, 1));
    }

    @Test
    public void constructor_termTooLow_throwsPlanException() {
        assertThrows(PlanException.class, () -> new Semester(1, 0));
    }

    @Test
    public void constructor_termTooHigh_throwsPlanException() {
        assertThrows(PlanException.class, () -> new Semester(1, 3));
    }

    @Test
    public void equals_sameValues_areEqual() {
        assertEquals(new Semester(1, 2), new Semester(1, 2));
    }
}
