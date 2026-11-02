package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Tests the {@link QuotaCommand} class. The student starts with the default S/U quota of 28 MCs.
 */
public class QuotaCommandTest {
    private Student student;

    /**
     * Adds a module with the given code, credits and S/U status.
     */
    private void addModule(String code, int credits, boolean isSu) {
        student.addModule(new Module(code, code, "", credits, Grade.B, isSu));
    }

    @BeforeEach
    public void setUp() {
        student = new Student();
    }

    @Test
    public void parse_quotaWithOrWithoutExtraWords_returnsCommand() {
        assertTrue(QuotaCommand.parse("quota").isPresent());
        assertTrue(QuotaCommand.parse("quota now").isPresent());
    }

    @Test
    public void parse_otherWord_returnsEmpty() {
        assertTrue(QuotaCommand.parse("quotas").isEmpty());
        assertTrue(QuotaCommand.parse("").isEmpty());
    }

    @Test
    public void execute_noSuModules_showsFullQuotaAndEmptyMessage() {
        addModule("CS2113", 4, false);

        Response response = new QuotaCommand().execute(student);

        assertEquals("Remaining SU quota: 28 MCs.\nNo modules have been SU-ed yet.", response.message());
        assertFalse(response.isDataChanged());
    }

    @Test
    public void execute_twoSuModules_listsThemWithFullListNumbers() {
        addModule("MA1521", 4, true);
        addModule("CS2113", 4, false);
        addModule("GEA1000", 4, true);

        Response response = new QuotaCommand().execute(student);

        assertEquals("Remaining SU quota: 20 MCs.\nModules SU-ed so far:\n"
                + "  1. MA1521 | 4 MCs | Grade: B [S/U]\n"
                + "  3. GEA1000 | 4 MCs | Grade: B [S/U]", response.message());
    }
}
