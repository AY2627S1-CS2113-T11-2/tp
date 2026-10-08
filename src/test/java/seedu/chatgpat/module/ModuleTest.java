package seedu.chatgpat.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Module} class.
 */
public class ModuleTest {

    /**
     * Tests that a valid module is constructed correctly.
     */
    @Test
    public void constructor_validModule_success() {
        Module module = new Module("CS2113", 4, "A-");
        assertEquals("CS2113", module.getModuleCode());
        assertEquals(4, module.getCredits());
        assertEquals("A-", module.getGrade());
        assertFalse(module.isSu());
    }

    /**
     * Tests that {@code toString} returns the expected formatted string.
     */
    @Test
    public void toString_returnsFormattedString() {
        Module module = new Module("CS2113", 4, "A-");
        assertEquals("CS2113 | 4 MCs | Grade: A-", module.toString());
    }

    /**
     * Tests that {@code toString} includes the SU suffix when the module is SU-ed.
     */
    @Test
    public void toString_suModule_includesSuSuffix() {
        Module module = new Module("CS2113", 4, "A-");
        module.setSu(true);
        assertEquals("CS2113 | 4 MCs | Grade: A- | SU", module.toString());
    }
}
