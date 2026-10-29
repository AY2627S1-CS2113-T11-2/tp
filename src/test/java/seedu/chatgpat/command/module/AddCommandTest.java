
package seedu.chatgpat.command.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.DuplicateModuleException;
import seedu.chatgpat.exception.InvalidAddException;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Tests the {@link AddCommand} class.
 */
public class AddCommandTest {

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
     * Tests that a valid add command is parsed successfully.
     */
    @Test
    public void parse_validInput_returnsCommand() {
        assertTrue(AddCommand.parse(
                "add m/CS2113 c/4 g/A-").isPresent());
    }

    /**
     * Tests that unrelated or malformed inputs are not parsed.
     */
    @Test
    public void parse_invalidFormat_returnsEmpty() {
        assertTrue(AddCommand.parse("add").isEmpty());
        assertTrue(AddCommand.parse("add m/CS2113").isEmpty());
        assertTrue(AddCommand.parse(
                "add m/CS2113 c/4").isEmpty());
        assertTrue(AddCommand.parse(
                "add CS2113 4 A").isEmpty());
        assertTrue(AddCommand.parse("list").isEmpty());
        assertTrue(AddCommand.parse("").isEmpty());
    }

    /**
     * Tests that an invalid grade causes an InvalidAddException.
     */
    @Test
    public void parse_invalidGrade_throwsInvalidAddException() {
        assertThrows(InvalidAddException.class,
                () -> AddCommand.parse(
                        "add m/CS2113 c/4 g/Z"));
    }

    /**
     * Tests that zero credits are rejected.
     */
    @Test
    public void parse_zeroCredits_throwsInvalidAddException() {
        assertThrows(InvalidAddException.class,
                () -> AddCommand.parse(
                        "add m/CS2113 c/0 g/A"));
    }

    /**
     * Tests that an invalid credit number is rejected.
     */
    @Test
    public void parse_creditsOverflow_throwsInvalidAddException() {
        assertThrows(InvalidAddException.class,
                () -> AddCommand.parse(
                        "add m/CS2113 c/99999999999999999999 g/A"));
    }

    /**
     * Tests that a valid command adds a module successfully
     * and returns the correct response message.
     */
    @Test
    public void execute_validInput_addsModule() {
        Student student = new Student();

        Command command = AddCommand.parse(
                "add m/CS2113 c/4 g/A-").orElseThrow();

        Response response = command.execute(student);

        String expected = "New module added: "
                + "CS2113 (4 MCs, Grade: A-)"
                + "\nYou now have 1 module recorded.";

        assertEquals(expected, response.message());
        assertEquals(1, student.getModuleCount());
        assertEquals("CS2113", student.getModule(1).getModuleCode());
        assertEquals(4, student.getModule(1).getCredit());
        assertEquals(Grade.A_MINUS, student.getModule(1).getGrade());
    }

    /**
     * Tests that adding multiple modules updates the module
     * count and pluralization in the response correctly.
     */
    @Test
    public void execute_multipleModules_returnsCorrectCount() {
        Student student = new Student();

        student.addModule(module("CS2113", 4, Grade.A_MINUS));

        Command command = AddCommand.parse(
                "add m/MA1521 c/4 g/B+").orElseThrow();

        Response response = command.execute(student);

        String expected = "New module added: "
                + "MA1521 (4 MCs, Grade: B+)"
                + "\nYou now have 2 modules recorded.";

        assertEquals(expected, response.message());
        assertEquals(2, student.getModuleCount());
    }

    /**
     * Tests that adding a duplicate module throws
     * DuplicateModuleException without changing the module list.
     */
    @Test
    public void execute_duplicateModule_throwsException() {
        Student student = new Student();
        student.addModule(module("CS2113", 4, Grade.A));

        Command command = AddCommand.parse(
                "add m/CS2113 c/4 g/B").orElseThrow();

        DuplicateModuleException exception = assertThrows(
                DuplicateModuleException.class,
                () -> command.execute(student));

        assertEquals(
                "Module already exists: CS2113",
                exception.getMessage());

        assertEquals(1, student.getModuleCount());
    }

    /**
     * Tests that duplicate module codes are detected
     * regardless of letter case.
     */
    @Test
    public void execute_duplicateCodeDifferentCase_throwsException() {
        Student student = new Student();
        student.addModule(module("CS2113", 4, Grade.A));

        Command command = AddCommand.parse(
                "add m/cs2113 c/4 g/B").orElseThrow();

        assertThrows(DuplicateModuleException.class,
                () -> command.execute(student));

        assertEquals(1, student.getModuleCount());
    }
}
