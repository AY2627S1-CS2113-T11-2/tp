package seedu.chatgpat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.chatgpat.command.Response;
import seedu.chatgpat.service.ChatInterface;
import seedu.chatgpat.storage.Storage;
import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Semester;

/** Exercises startup and automatic saving through the actual application command loop. */
public class ChatGpatTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    public void run_eachDataCommand_savesBeforeDisplayingResponse() {
        Path file = temporaryDirectory.resolve("data/modules.txt");
        ScriptedUi ui = new ScriptedUi(file, "add m/CS2113 c/4 g/A-", "edit 1 m/CS2114 c/5 g/B+",
                "su 1", "unsu 1", "plan 1 y/2 s/1", "unplan 1", "delete 1", "exit");

        new ChatGpat(ui, new Storage(file)).run();

        assertTrue(ui.warnings.isEmpty());
        assertEquals(7, ui.savedModules.size());
        Module added = ui.savedModules.get(0).get(0);
        assertEquals("CS2113", added.getModuleCode());
        assertEquals(4, added.getCredit());
        assertEquals(Grade.A_MINUS, added.getGrade());
        Module edited = ui.savedModules.get(1).get(0);
        assertEquals("CS2114", edited.getModuleCode());
        assertEquals(5, edited.getCredit());
        assertEquals(Grade.B_PLUS, edited.getGrade());
        assertEquals("CS2113", edited.getModuleName());
        assertTrue(ui.savedModules.get(2).get(0).isSu());
        assertFalse(ui.savedModules.get(3).get(0).isSu());
        assertEquals(new Semester(2, 1), ui.savedModules.get(4).get(0).getSemester());
        assertNull(ui.savedModules.get(5).get(0).getSemester());
        assertTrue(ui.savedModules.get(6).isEmpty());
        assertTrue(ui.responses.getLast().isExit());
    }

    @Test
    public void run_newSession_restoresSavedData() {
        Path file = temporaryDirectory.resolve("modules.txt");
        ScriptedUi first = new ScriptedUi(file, "add m/CS2113 c/4 g/A-", "plan 1 y/2 s/1", "su 1", "exit");
        new ChatGpat(first, new Storage(file)).run();
        ScriptedUi second = new ScriptedUi(file, "list", "quota", "exit");

        new ChatGpat(second, new Storage(file)).run();

        assertTrue(second.warnings.isEmpty());
        assertTrue(second.responses.get(0).message().contains("CS2113 | 4 MCs | Grade: A- [S/U] | Y2S1"));
        assertTrue(second.responses.get(1).message().contains("24"));
    }

    @Test
    public void run_badLine_passesWarningsToUiAndLoadsOtherModules() throws IOException {
        Path file = temporaryDirectory.resolve("modules.txt");
        Files.writeString(file, "CS2113 | 4 | A-\ninvalid\nMA1521 | 4 | B+ | 1\n");
        ScriptedUi ui = new ScriptedUi(file, "list", "exit");

        new ChatGpat(ui, new Storage(file)).run();

        assertEquals(1, ui.warnings.size());
        assertTrue(ui.warnings.get(0).contains("Skipped line 2"));
        assertTrue(ui.responses.get(0).message().contains("1. CS2113"));
        assertTrue(ui.responses.get(0).message().contains("2. MA1521"));
    }

    @Test
    public void run_readOnlyAndRejectedCommands_doesNotRewriteFile() throws IOException {
        Path file = temporaryDirectory.resolve("modules.txt");
        String original = "  CS2113|4|A-\n";
        Files.writeString(file, original);
        ScriptedUi ui = new ScriptedUi(file, "list", "gpa", "help", "summary", "quota", "target 4.0 r/4",
                "add m/CS2113 c/4 g/A", "edit 1 c/0", "delete 2", "su 2", "unsu 1",
                "plan 1 y/5 s/1", "unplan 1", "unknown", "exit");

        new ChatGpat(ui, new Storage(file)).run();

        assertTrue(ui.savedModules.isEmpty());
        assertEquals(original, Files.readString(file));
    }

    @Test
    public void run_missingFileAndNoChanges_doesNotCreateFile() {
        Path file = temporaryDirectory.resolve("data/modules.txt");
        ScriptedUi ui = new ScriptedUi(file, "list", "exit");

        new ChatGpat(ui, new Storage(file)).run();

        assertTrue(ui.warnings.isEmpty());
        assertEquals("No modules recorded yet.", ui.responses.getFirst().message());
        assertFalse(Files.exists(file.getParent()));
    }

    @Test
    public void run_saveFailure_reportsErrorAndKeepsAcceptingCommands() throws IOException {
        Path blocker = temporaryDirectory.resolve("data");
        Files.writeString(blocker, "keep this");
        Path file = blocker.resolve("modules.txt");
        ScriptedUi ui = new ScriptedUi(file, "add m/CS2113 c/4 g/A-", "list", "exit");

        new ChatGpat(ui, new Storage(file)).run();

        assertTrue(ui.warnings.stream().anyMatch(warning -> warning.contains("Could not save modules")));
        assertTrue(ui.responses.get(1).message().contains("CS2113 | 4 MCs | Grade: A-"));
        assertTrue(ui.responses.getLast().isExit());
        assertEquals("keep this", Files.readString(blocker));
    }

    /** Captures UI output and reads the real file at each successful data-changing response. */
    private static class ScriptedUi extends ChatInterface {
        private final Path file;
        private final Queue<String> queries;
        private final List<String> warnings = new ArrayList<>();
        private final List<Response> responses = new ArrayList<>();
        private final List<List<Module>> savedModules = new ArrayList<>();

        ScriptedUi(Path file, String... queries) {
            this.file = file;
            this.queries = new ArrayDeque<>(List.of(queries));
        }

        @Override
        public String getUserQuery() {
            assertFalse(queries.isEmpty(), "The application should stop after exit.");
            return queries.remove();
        }

        @Override
        public void printWelcome(List<String> warnings) {
            this.warnings.addAll(warnings);
        }

        @Override
        public void printResponse(Response response, List<String> warnings) {
            responses.add(response);
            this.warnings.addAll(warnings);
            if (response.isDataChanged() && warnings.isEmpty()) {
                assertTrue(Files.isRegularFile(file), "A successful change must be saved before its response.");
                Storage.LoadResult saved = new Storage(file).load();
                assertTrue(saved.warnings().isEmpty());
                savedModules.add(saved.modules());
            }
        }

        @Override
        public void printError(Exception error) {
            fail("Unexpected application error", error);
        }
    }
}
