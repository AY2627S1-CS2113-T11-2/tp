
package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Tests the {@link ListCommand} class.
 */
public class ListCommandTest {

    /**
     * Creates a module with the given code, credits, and grade.
     *
     * @param code The module code.
     * @param credits The modular credits.
     * @param grade The module grade.
     * @return A new module.
     */
    private Module module(String code, int credits, Grade grade) {
        return new Module(code, code, "Description", credits, grade, false);
    }

    /**
     * Tests that a valid list command is parsed successfully.
     */
    @Test
    public void parse_validInput_returnsCommand() {
        assertTrue(ListCommand.parse("list").isPresent());
    }

    /**
     * Tests that extraneous arguments are ignored.
     */
    @Test
    public void parse_extraneousArguments_returnsCommand() {
        assertTrue(ListCommand.parse("list all").isPresent());
        assertTrue(ListCommand.parse("list 123").isPresent());
    }

    /**
     * Tests that unrelated or malformed inputs are not parsed.
     */
    @Test
    public void parse_invalidInput_returnsEmpty() {
        assertTrue(ListCommand.parse("lists").isEmpty());
        assertTrue(ListCommand.parse("lis").isEmpty());
        assertTrue(ListCommand.parse("add").isEmpty());
        assertTrue(ListCommand.parse("").isEmpty());
    }

    /**
     * Tests that listing an empty student's modules
     * returns the correct empty-state message.
     */
    @Test
    public void execute_emptyStudent_returnsEmptyMessage() {
        Student student = new Student();

        Command command = ListCommand.parse("list").orElseThrow();
        Response response = command.execute(student);

        assertEquals("No modules recorded yet.", response.message());
    }

    /**
     * Tests that a single module is listed with
     * the correct index and formatting.
     */
    @Test
    public void execute_singleModule_returnsCorrectOutput() {
        Student student = new Student();
        student.addModule(module("CS2113", 4, Grade.A_MINUS));

        Command command = ListCommand.parse("list").orElseThrow();
        Response response = command.execute(student);

        String expected = "Here are your recorded modules:"
                + "\n  1. CS2113 | 4 MCs | Grade: A-";

        assertEquals(expected, response.message());
    }

    /**
     * Tests that multiple modules are listed
     * in their original insertion order.
     */
    @Test
    public void execute_multipleModules_returnsCorrectOutput() {
        Student student = new Student();

        student.addModule(module("CS2113", 4, Grade.A_MINUS));
        student.addModule(module("MA1521", 4, Grade.B_PLUS));
        student.addModule(module("CS3231", 4, Grade.A));

        Command command = ListCommand.parse("list").orElseThrow();
        Response response = command.execute(student);

        String expected = "Here are your recorded modules:"
                + "\n  1. CS2113 | 4 MCs | Grade: A-"
                + "\n  2. MA1521 | 4 MCs | Grade: B+"
                + "\n  3. CS3231 | 4 MCs | Grade: A";

        assertEquals(expected, response.message());
    }

    /**
     * Tests that deleting a module updates the
     * indexes displayed by the list command.
     */
    @Test
    public void execute_afterDeletion_returnsUpdatedIndexes() {
        Student student = new Student();

        student.addModule(module("CS2113", 4, Grade.A_MINUS));
        student.addModule(module("MA1521", 4, Grade.B_PLUS));
        student.addModule(module("CS3231", 4, Grade.A));

        student.deleteModule(2);

        Command command = ListCommand.parse("list").orElseThrow();
        Response response = command.execute(student);

        String expected = "Here are your recorded modules:"
                + "\n  1. CS2113 | 4 MCs | Grade: A-"
                + "\n  2. CS3231 | 4 MCs | Grade: A";

        assertEquals(expected, response.message());
    }
}
