package seedu.duke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Grade} enum.
 */
public class GradeTest {

    /**
     * Tests that each grade constant has the expected label, grade point, and GPA effect.
     */
    @Test
    public void gradeConstants_correctProperties() {
        assertEquals("A+", Grade.A_PLUS.getLabel());
        assertEquals(5.0, Grade.A_PLUS.getGradePoint(), 0.0);
        assertTrue(Grade.A_PLUS.isAffectsGpa());

        assertEquals("A", Grade.A.getLabel());
        assertEquals(5.0, Grade.A.getGradePoint(), 0.0);
        assertTrue(Grade.A.isAffectsGpa());

        assertEquals("A-", Grade.A_MINUS.getLabel());
        assertEquals(4.5, Grade.A_MINUS.getGradePoint(), 0.0);
        assertTrue(Grade.A_MINUS.isAffectsGpa());

        assertEquals("B+", Grade.B_PLUS.getLabel());
        assertEquals(4.0, Grade.B_PLUS.getGradePoint(), 0.0);
        assertTrue(Grade.B_PLUS.isAffectsGpa());

        assertEquals("B", Grade.B.getLabel());
        assertEquals(3.5, Grade.B.getGradePoint(), 0.0);
        assertTrue(Grade.B.isAffectsGpa());

        assertEquals("B-", Grade.B_MINUS.getLabel());
        assertEquals(3.0, Grade.B_MINUS.getGradePoint(), 0.0);
        assertTrue(Grade.B_MINUS.isAffectsGpa());

        assertEquals("C+", Grade.C_PLUS.getLabel());
        assertEquals(2.5, Grade.C_PLUS.getGradePoint(), 0.0);
        assertTrue(Grade.C_PLUS.isAffectsGpa());

        assertEquals("C", Grade.C.getLabel());
        assertEquals(2.0, Grade.C.getGradePoint(), 0.0);
        assertTrue(Grade.C.isAffectsGpa());

        assertEquals("D+", Grade.D_PLUS.getLabel());
        assertEquals(1.5, Grade.D_PLUS.getGradePoint(), 0.0);
        assertTrue(Grade.D_PLUS.isAffectsGpa());

        assertEquals("D", Grade.D.getLabel());
        assertEquals(1.0, Grade.D.getGradePoint(), 0.0);
        assertTrue(Grade.D.isAffectsGpa());

        assertEquals("F", Grade.F.getLabel());
        assertEquals(0.0, Grade.F.getGradePoint(), 0.0);
        assertTrue(Grade.F.isAffectsGpa());

        assertEquals("CS", Grade.CS.getLabel());
        assertEquals(0.0, Grade.CS.getGradePoint(), 0.0);
        assertFalse(Grade.CS.isAffectsGpa());

        assertEquals("CU", Grade.CU.getLabel());
        assertEquals(0.0, Grade.CU.getGradePoint(), 0.0);
        assertFalse(Grade.CU.isAffectsGpa());
    }

    /**
     * Tests that {@code fromLabel} returns the correct enum for valid labels.
     */
    @Test
    public void fromLabel_validLabels_returnsCorrectGrade() {
        assertEquals(Grade.A_PLUS, Grade.fromLabel("A+"));
        assertEquals(Grade.A, Grade.fromLabel("A"));
        assertEquals(Grade.A_MINUS, Grade.fromLabel("A-"));
        assertEquals(Grade.B_PLUS, Grade.fromLabel("B+"));
        assertEquals(Grade.B, Grade.fromLabel("B"));
        assertEquals(Grade.B_MINUS, Grade.fromLabel("B-"));
        assertEquals(Grade.C_PLUS, Grade.fromLabel("C+"));
        assertEquals(Grade.C, Grade.fromLabel("C"));
        assertEquals(Grade.D_PLUS, Grade.fromLabel("D+"));
        assertEquals(Grade.D, Grade.fromLabel("D"));
        assertEquals(Grade.F, Grade.fromLabel("F"));
        assertEquals(Grade.CS, Grade.fromLabel("CS"));
        assertEquals(Grade.CU, Grade.fromLabel("CU"));
    }

    /**
     * Tests that {@code fromLabel} is case-insensitive and trims surrounding whitespace.
     */
    @Test
    public void fromLabel_caseInsensitiveAndTrimmed_returnsCorrectGrade() {
        assertEquals(Grade.A_PLUS, Grade.fromLabel("a+"));
        assertEquals(Grade.A_PLUS, Grade.fromLabel(" A+ "));
        assertEquals(Grade.A_MINUS, Grade.fromLabel("a-"));
        assertEquals(Grade.B_PLUS, Grade.fromLabel(" b+ "));
        assertEquals(Grade.CS, Grade.fromLabel("cs"));
        assertEquals(Grade.CU, Grade.fromLabel(" cu "));
    }

    /**
     * Tests that {@code fromLabel} throws {@link IllegalArgumentException} for null input.
     */
    @Test
    public void fromLabel_nullInput_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Grade.fromLabel(null));
    }

    /**
     * Tests that {@code fromLabel} throws {@link IllegalArgumentException} for invalid labels.
     */
    @Test
    public void fromLabel_invalidLabels_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Grade.fromLabel("X"));
        assertThrows(IllegalArgumentException.class, () -> Grade.fromLabel(""));
        assertThrows(IllegalArgumentException.class, () -> Grade.fromLabel("   "));
        assertThrows(IllegalArgumentException.class, () -> Grade.fromLabel("A++"));
        assertThrows(IllegalArgumentException.class, () -> Grade.fromLabel("E"));
        assertThrows(IllegalArgumentException.class, () -> Grade.fromLabel("AB"));
        assertThrows(IllegalArgumentException.class, () -> Grade.fromLabel("1"));
    }
}
