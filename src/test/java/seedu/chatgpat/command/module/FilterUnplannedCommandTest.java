package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Semester;
import seedu.chatgpat.student.Student;

/**
 * Tests the {@link FilterUnplannedCommand} class and the unplanned filter in {@link Student}.
 */
public class FilterUnplannedCommandTest {
    private final String sep = System.lineSeparator();
    private Student student;

    /**
     * Creates a student with one planned module (number 1) and two unplanned modules (numbers 2 and 3).
     */
    @BeforeEach
    public void setUp() {
        student = new Student();
        student.addModule(new Module("CS2113", "test", "none", 4, Grade.A_MINUS, false));
        student.addModule(new Module("CS3231", "test", "none", 4, Grade.B_MINUS, false));
        student.addModule(new Module("MA1521", "test", "none", 4, Grade.B_PLUS, false));
        student.planModule(1, new Semester(2, 2));
    }

    @Test
    public void parse_validInput_returnsCommand() {
        assertTrue(FilterUnplannedCommand.parse("filter unplanned").isPresent());
    }

    @Test
    public void parse_otherInput_returnsEmpty() {
        assertTrue(FilterUnplannedCommand.parse("filter y/2 s/2").isEmpty());
        assertTrue(FilterUnplannedCommand.parse("filter").isEmpty());
        assertTrue(FilterUnplannedCommand.parse("filter planned").isEmpty());
        assertTrue(FilterUnplannedCommand.parse("filter unplanned extra").isEmpty());
        assertTrue(FilterUnplannedCommand.parse("list").isEmpty());
    }

    @Test
    public void filterUnplannedModules_mixedModules_returnsFullListNumbers() {
        assertEquals(List.of(2, 3), student.filterUnplannedModules());
    }

    @Test
    public void execute_mixedModules_listsOnlyUnplannedWithFullListNumbers() {
        Command command = FilterUnplannedCommand.parse("filter unplanned").orElseThrow();

        Response response = command.execute(student);

        String expected = "Modules not planned for any semester:" + sep
                + "2. CS3231 | 4 MCs | Grade: B-" + sep
                + "3. MA1521 | 4 MCs | Grade: B+" + sep;
        assertEquals(expected, response.message());
    }

    @Test
    public void execute_allPlanned_returnsAllPlannedMessage() {
        student.planModule(2, new Semester(3, 1));
        student.planModule(3, new Semester(3, 2));

        Response response = new FilterUnplannedCommand().execute(student);

        assertEquals("All modules are planned.", response.message());
    }

    @Test
    public void execute_noModules_returnsNoModulesMessage() {
        Response response = new FilterUnplannedCommand().execute(new Student());

        assertEquals("No modules recorded yet.", response.message());
    }

    @Test
    public void execute_afterUnplan_moduleAppearsWithItsNumber() {
        student.unplanModule(1);

        Response response = new FilterUnplannedCommand().execute(student);

        String expected = "Modules not planned for any semester:" + sep
                + "1. CS2113 | 4 MCs | Grade: A-" + sep
                + "2. CS3231 | 4 MCs | Grade: B-" + sep
                + "3. MA1521 | 4 MCs | Grade: B+" + sep;
        assertEquals(expected, response.message());
    }
}
