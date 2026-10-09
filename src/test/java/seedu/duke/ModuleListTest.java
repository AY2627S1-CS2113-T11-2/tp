package seedu.duke;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the functionality of adding and listing modules.
 */
public class ModuleListTest {

    private List<Module> modules;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Sets up stream redirection prior to executing each test.
     */
    @BeforeEach
    public void setUp() {
        modules = new ArrayList<>();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    /**
     * Restores standard system output following each test execution.
     */
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Tests listing modules when the recorded list is empty.
     */
    @Test
    public void listModules_emptyList_printsEmptyMessage() {
        printModuleList(modules);

        String expectedOutput = "No modules recorded yet." + System.lineSeparator();
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    /**
     * Tests listing modules when modules exist in the student's profile.
     */
    @Test
    public void listModules_nonEmptyList_printsFormattedModules() {
        Student student = new Student("Jerry", "0001", 32);
        student.addingModule(new Module("CS2113", "test", "none", 4, Grade.A_MINUS, true));
        student.addingModule(new Module("MA1521", "test", "none", 4, Grade.B_PLUS, true));

        student.printModuleList();

        String expectedOutput = "Here are your recorded modules:" + System.lineSeparator()
                + "1. CS2113 | 4 MCs | Grade: A-" + System.lineSeparator()
                + "2. MA1521 | 4 MCs | Grade: B+" + System.lineSeparator();

        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    /**
     * Helper method to print the module list to standard output.
     *
     * @param moduleList The list of modules to be printed.
     */
    private void printModuleList(List<Module> moduleList) {
        if (moduleList.isEmpty()) {
            System.out.println("No modules recorded yet.");
            return;
        }

        System.out.println("Here are your recorded modules:");
        for (int i = 0; i < moduleList.size(); i++) {
            System.out.println((i + 1) + ". " + moduleList.get(i).toString());
        }
    }
}

