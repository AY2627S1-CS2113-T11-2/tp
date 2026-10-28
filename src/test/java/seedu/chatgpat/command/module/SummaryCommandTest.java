package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Semester;
import seedu.chatgpat.student.Student;

/**
 * Tests semester ordering, totals, GPA exclusions, and formatting in {@link SummaryCommand}.
 */
public class SummaryCommandTest {
    private Student student;
    private SummaryCommand command;

    @BeforeEach
    public void setUp() {
        student = new Student();
        command = new SummaryCommand();
    }

    @Test
    public void parse_summaryWithExtraArguments_returnsCommand() {
        assertTrue(SummaryCommand.parse("summary").orElseThrow() instanceof SummaryCommand);
        assertTrue(SummaryCommand.parse("summary 123").isPresent());
        assertTrue(SummaryCommand.parse("summary\tanything").isPresent());
    }

    @Test
    public void parse_otherInputs_returnsEmpty() {
        assertTrue(SummaryCommand.parse("").isEmpty());
        assertTrue(SummaryCommand.parse("gpa").isEmpty());
        assertTrue(SummaryCommand.parse("summary123").isEmpty());
        assertTrue(SummaryCommand.parse("summaries").isEmpty());
    }

    @Test
    public void execute_noModules_returnsEmptyUnplannedLine() {
        Response response = command.execute(student);

        assertEquals("Unplanned | 0 modules | 0 MCs", response.message());
        assertFalse(response.isExit());
        assertFalse(response.hasModule());
    }

    @Test
    public void execute_onlyUnplannedModules_returnsTotalsWithoutGpa() {
        addModule(4, Grade.A, null);
        addModule(2, Grade.CS, null);
        student.applySu(1);

        assertEquals("Unplanned | 2 modules | 6 MCs", command.execute(student).message());
    }

    @Test
    public void execute_unsortedSemesters_groupsAndSortsWithWeightedGpa() {
        addModule(4, Grade.A_MINUS, new Semester(2, 1));
        addModule(2, Grade.B_PLUS, new Semester(1, 1));
        addModule(4, Grade.F, null);
        addModule(4, Grade.B_PLUS, new Semester(1, 2));
        addModule(4, Grade.B_PLUS, new Semester(2, 1));
        addModule(4, Grade.A, new Semester(1, 1));

        assertEquals("""
                Y1S1 | 2 modules | 6 MCs | GPA 4.67
                Y1S2 | 1 module | 4 MCs | GPA 4.00
                Y2S1 | 2 modules | 8 MCs | GPA 4.25
                Unplanned | 1 module | 4 MCs""", command.execute(student).message());
        // Display ordering must not change the module indexes used by other commands.
        assertEquals(new Semester(2, 1), student.getModule(1).getSemester());
        assertEquals(6, student.getModuleCount());
    }

    @Test
    public void execute_excludedGrades_countTowardsTotalsButNotGpa() {
        Semester semester = new Semester(1, 1);
        addModule(4, Grade.A, semester);
        addModule(2, Grade.B_PLUS, semester);
        addModule(6, Grade.F, semester);
        addModule(2, Grade.CS, semester);
        addModule(4, Grade.CU, semester);
        student.applySu(3);

        assertEquals("""
                Y1S1 | 5 modules | 18 MCs | GPA 4.67
                Unplanned | 0 modules | 0 MCs""", command.execute(student).message());
        assertEquals(28.0 / 6, student.calculateGpa(), 0.0001);
    }

    @Test
    public void execute_noContributingGrades_showsNaButFailingGradeShowsZero() {
        addModule(4, Grade.A, new Semester(1, 1));
        student.applySu(1);
        addModule(4, Grade.CS, new Semester(1, 2));
        addModule(2, Grade.CU, new Semester(1, 2));
        addModule(4, Grade.F, new Semester(2, 1));

        assertEquals("""
                Y1S1 | 1 module | 4 MCs | GPA n/a
                Y1S2 | 2 modules | 6 MCs | GPA n/a
                Y2S1 | 1 module | 4 MCs | GPA 0.00
                Unplanned | 0 modules | 0 MCs""", command.execute(student).message());
    }

    @Test
    public void execute_differentLocale_keepsDecimalPoint() {
        addModule(4, Grade.A_MINUS, new Semester(1, 1));
        Locale originalLocale = Locale.getDefault(Locale.Category.FORMAT);
        try {
            Locale.setDefault(Locale.Category.FORMAT, Locale.GERMANY);
            assertEquals("""
                    Y1S1 | 1 module | 4 MCs | GPA 4.50
                    Unplanned | 0 modules | 0 MCs""", command.execute(student).message());
        } finally {
            Locale.setDefault(Locale.Category.FORMAT, originalLocale);
        }
    }

    /**
     * Adds a uniquely named module and optionally assigns it to a semester.
     *
     * @param credits The module's MCs.
     * @param grade The module's grade.
     * @param semester The semester, or null to leave the module unplanned.
     */
    private void addModule(int credits, Grade grade, Semester semester) {
        int index = student.getModuleCount() + 1;
        student.addModule(new Module("TEST" + index, "test", "none", credits, grade, false));
        if (semester != null) {
            student.planModule(index, semester);
        }
    }
}
