package seedu.duke;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
// This is used to test add module and list module
public class ModuleListTest {

    private List<Module> modules;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        modules = new ArrayList<>();
        // Redirect System.out to capture output for testing the list command
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        // Restore standard System.out after each test
        System.setOut(originalOut);
    }


    // --- Tests for Listing Modules ---

    @Test
    public void listModules_emptyList_printsEmptyMessage() {
        printModuleList(modules);

        String expectedOutput = "No modules recorded yet." + System.lineSeparator();
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    @Test
    public void listModules_nonEmptyList_printsFormattedModules() {
        Student student = new Student("Jerry","0001",32);
        student.addingModule(new Module("CS2113","test","none",4,Grade.A_MINUS,true));
        student.addingModule(new Module("MA1521", "test","none",4, Grade.B_PLUS,true));

        student.printModuleList();

        String expectedOutput = "Here are your recorded modules:" + System.lineSeparator() +
                "1. CS2113 | 4 MCs | Grade: A-" + System.lineSeparator() +
                "2. MA1521 | 4 MCs | Grade: B+" + System.lineSeparator();

        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    // --- Helper Method Under Test ---

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