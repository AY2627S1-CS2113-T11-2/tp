package seedu.chatgpat.command.module;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Semester;
import seedu.chatgpat.student.Student;

import seedu.chatgpat.exception.PlanException;

public class FilterCommandTest {
    private Student student;
    @BeforeEach
    public void setUp() {
        student = new Student();
        student.addModule(new Module("CS2113", "test", "none", 4, Grade.A_MINUS, false));
        student.addModule(new Module("CS3231", "test", "none", 4, Grade.B_MINUS, false));
        student.planModule(1, new Semester(2, 2));
        student.planModule(2, new Semester(3, 1));
    }
    /**
     * Tests that a valid filter command is parsed successfully.
     */
    @Test
    public void parse_validInput_returnsCommand() {
        assertTrue(FilterCommand.parse("filter y/2 s/2").isPresent());
    }

    /**
     * Tests that unrelated or malformed commands are rejected.
     */
    @Test
    public void parse_invalidInput_returnsEmpty() {
        assertTrue(FilterCommand.parse("filter").isEmpty());
        assertTrue(FilterCommand.parse("filter y/2").isEmpty());
        assertTrue(FilterCommand.parse("filter 2 2").isEmpty());
        assertTrue(FilterCommand.parse("list").isEmpty());
        assertTrue(FilterCommand.parse("").isEmpty());
    }
    /**
     * Tests that filtering returns only the module
     * belonging to the specified semester.
     */
    @Test
    public void execute_validInput_returnsResponseWithFilteredModules() {
        Command command = FilterCommand.parse(
                "filter y/2 s/2").orElseThrow();

        Response response = command.execute(student);

        String expected = "Modules in Y2S2:"
                + System.lineSeparator()
                + "1. CS2113 | 4 MCs | Grade: A- | Y2S2"
                + System.lineSeparator();

        assertEquals(expected, response.message());
    }

    /**
     * Tests that filtering returns empty message if
     * there is no modules in selected semester.
     */
    @Test
    public void execute_validInput_returnsResponseWithEmptyMessage() {
        Command command = FilterCommand.parse(
                "filter y/2 s/1").orElseThrow();
        Response response = command.execute(student);
        String expected = "No modules found in Y2S1.";
        assertEquals(expected, response.message());
    }

    /**
     * Tests that filtering an empty student's module list
     * returns the appropriate message.
     */
    @Test
    public void execute_emptyStudent_returnsMessage() {
        Student emptyStudent = new Student();

        Command command = FilterCommand.parse(
                "filter y/2 s/1").orElseThrow();

        Response response = command.execute(emptyStudent);

        String expected = "No modules found in Y2S1.";

        assertEquals(expected, response.message());
    }

    /**
     * Tests that filtering multiple matching modules
     * preserves their original indexes and output format.
     */
    @Test
    public void execute_multipleMatches_returnsCorrectModules() {
        student.addModule(new Module(
                "MA1521", "test", "none", 4, Grade.B_PLUS, false));

        student.planModule(3, new Semester(2, 2));

        Command command = FilterCommand.parse(
                "filter y/2 s/2").orElseThrow();

        Response response = command.execute(student);

        String expected = "Modules in Y2S2:"
                + System.lineSeparator()
                + "1. CS2113 | 4 MCs | Grade: A- | Y2S2"
                + System.lineSeparator()
                + "3. MA1521 | 4 MCs | Grade: B+ | Y2S2"
                + System.lineSeparator();

        assertEquals(expected, response.message());
    }

    /**
     * Tests that an invalid semester causes an exception.
     */
    @Test
    public void execute_invalidSemester_throwsException() {
        Command command = FilterCommand.parse(
                "filter y/2 s/9").orElseThrow();

        assertThrows(PlanException.class,
                () -> command.execute(student));
    }

    @Test
   public void execute_invalidYear_throwsException() {
        Command command = FilterCommand.parse(
                "filter y/9 s/1").orElseThrow();

        assertThrows(PlanException.class,
                () -> command.execute(student));
    }

    /**
     * Tests that unplanned modules are excluded when filtering
     * modules by a specific semester.
     */
    @Test
    public void execute_unplannedModules_excludedFromFilter() {
        // Add a module without assigning a semester.
        student.addModule(new Module(
                "MA1521", "test", "none", 4, Grade.B_PLUS, false));

        Command command = FilterCommand.parse(
                "filter y/2 s/2").orElseThrow();

        Response response = command.execute(student);

        String expected = "Modules in Y2S2:"
                + System.lineSeparator()
                + "1. CS2113 | 4 MCs | Grade: A- | Y2S2"
                + System.lineSeparator();

        assertEquals(expected, response.message());
    }
}
