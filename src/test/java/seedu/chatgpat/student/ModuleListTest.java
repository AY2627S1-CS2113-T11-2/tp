package seedu.chatgpat.student;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.module.ListCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the list response; printing belongs to ChatInterface.
 */
public class ModuleListTest {

    /**
     * Tests listing modules when the recorded list is empty.
     */
    @Test
    public void listModules_emptyList_returnsEmptyMessage() {
        assertEquals("No modules recorded yet.", new ListCommand().execute(new Student()).message());
    }

    /**
     * Tests listing modules when modules exist in the student's profile.
     */
    @Test
    public void listModules_nonEmptyList_returnsFormattedModules() {
        Student student = new Student("Jerry", "0001", 32);
        student.addModule(new Module("CS2113", "test", "none", 4, Grade.A_MINUS, false));
        student.addModule(new Module("MA1521", "test", "none", 4, Grade.B_PLUS, false));

        assertEquals("Here are your recorded modules:\n"
                + "  1. CS2113 | 4 MCs | Grade: A-\n"
                + "  2. MA1521 | 4 MCs | Grade: B+", new ListCommand().execute(student).message());
    }
}

