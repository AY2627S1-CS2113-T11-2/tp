package seedu.chatgpat.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Semester;

/** Tests real text-file persistence in isolated temporary folders. */
public class StorageTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    public void saveThenLoad_allFields_preservesValuesAndOrder() throws IOException {
        Path file = temporaryDirectory.resolve("data/modules.txt");
        Storage storage = new Storage(file);
        Module planned = new Module("CS2113", "Software Engineering", "Team project", 4, Grade.A_MINUS, false);
        planned.setSemester(new Semester(2, 1));
        Module unplanned = new Module("MA1521", "Calculus", "微积分", 4, Grade.B_PLUS, true);

        assertTrue(storage.save(List.of(planned, unplanned)).isEmpty());
        assertEquals(List.of("CS2113 | 4 | A- | 0 | Y2S1 | Software Engineering | Team project",
                "MA1521 | 4 | B+ | 1 | - | Calculus | 微积分"), Files.readAllLines(file));

        Storage.LoadResult result = new Storage(file).load();
        assertTrue(result.warnings().isEmpty());
        assertEquals(2, result.modules().size());
        assertModuleEquals(planned, result.modules().get(0));
        assertModuleEquals(unplanned, result.modules().get(1));
    }

    @Test
    public void saveThenLoad_reservedCharacters_preservesTextOnOneLine() throws IOException {
        Path file = temporaryDirectory.resolve("modules.txt");
        Module module = new Module("CS|\\2113", "  Software | 工程  ",
                "First\nSecond\r\nC:\\notes\\\tEnd\\", 4, Grade.CS, true);
        Storage storage = new Storage(file);

        assertTrue(storage.save(List.of(module)).isEmpty());

        assertEquals(1, Files.readAllLines(file).size());
        Storage.LoadResult result = storage.load();
        assertTrue(result.warnings().isEmpty());
        assertModuleEquals(module, result.modules().get(0));
    }

    @Test
    public void load_missingFile_startsEmptyWithoutCreatingFile() {
        Path file = temporaryDirectory.resolve("missing/modules.txt");

        Storage.LoadResult result = new Storage(file).load();

        assertTrue(result.modules().isEmpty());
        assertTrue(result.warnings().isEmpty());
        assertFalse(Files.exists(file.getParent()));
    }

    @Test
    public void load_malformedLine_reportsLineNumberAndLoadsRemainingRecords() throws IOException {
        Path file = temporaryDirectory.resolve("modules.txt");
        String content = "CS2113 | 4 | A- | 0 | Y2S1\nnot a module\nMA1521 | 4 | B+ | 1 | -\n";
        Files.writeString(file, content);

        Storage.LoadResult result = new Storage(file).load();

        assertEquals(List.of("CS2113", "MA1521"),
                result.modules().stream().map(Module::getModuleCode).toList());
        assertEquals(1, result.warnings().size());
        assertTrue(result.warnings().get(0).contains("Skipped line 2"));
        assertEquals(content, Files.readString(file));
    }

    @Test
    public void load_olderAndEmptyOptionalFields_usesDefaults() throws IOException {
        Path file = temporaryDirectory.resolve("modules.txt");
        Files.writeString(file, """
                CS2113 | 4 | A-
                MA1521 | 4 | B+ | 1
                CS1010 | 4 | A | | | |
                GE1000 | 2 | CS | 0 | -
                """);

        Storage.LoadResult result = new Storage(file).load();

        assertTrue(result.warnings().isEmpty());
        assertEquals(4, result.modules().size());
        for (Module module : result.modules()) {
            assertNull(module.getSemester());
            assertEquals(module.getModuleCode(), module.getModuleName());
            assertEquals("", module.getModuleDescription());
        }
        assertFalse(result.modules().get(0).isSu());
        assertTrue(result.modules().get(1).isSu());
        assertFalse(result.modules().get(2).isSu());
    }

    @Test
    public void load_invalidFields_skipsEachBadLine() throws IOException {
        Path file = temporaryDirectory.resolve("modules.txt");
        List<String> invalidLines = List.of(
                "CS1000 | 4", " | 4 | A", "CS 1000 | 4 | A", "CS1000 | nope | A",
                "CS1000 | 0 | A", "CS1000 | -1 | A", "CS1000 | 999999999999 | A",
                "CS1000 | 4 | unknown", "CS1000 | 4 | A | true", "CS1000 | 4 | A | 0 | Y5S1",
                "CS1000 | 4 | A | 0 | Y1S3", "CS1000 | 4 | A | 0 | 2/1",
                "CS1000 | 4 | A | 0 | - | name | desc | extra", "CS1000 | 4 | A | 0 | - | bad\\q",
                "CS1000 | 4 | A | 0 | - | bad\\");
        Files.writeString(file, String.join("\n", invalidLines) + "\nGOOD | 4 | A\n");

        Storage.LoadResult result = new Storage(file).load();

        assertEquals(1, result.modules().size());
        assertEquals("GOOD", result.modules().get(0).getModuleCode());
        assertEquals(invalidLines.size(), result.warnings().size());
        for (int i = 0; i < invalidLines.size(); i++) {
            assertTrue(result.warnings().get(i).contains("Skipped line " + (i + 1) + " in "));
        }
    }

    @Test
    public void load_duplicateCode_keepsFirstRecordAndContinues() throws IOException {
        Path file = temporaryDirectory.resolve("modules.txt");
        Files.writeString(file, "CS2113 | 4 | A\ncs2113 | 2 | B\nMA1521 | 4 | B+\n");

        Storage.LoadResult result = new Storage(file).load();

        assertEquals(2, result.modules().size());
        assertEquals(Grade.A, result.modules().get(0).getGrade());
        assertEquals(1, result.warnings().size());
        assertTrue(result.warnings().get(0).contains("Skipped line 2"));
        assertTrue(result.warnings().get(0).contains("Duplicate module code"));
    }

    @Test
    public void load_bomAndBlankLines_ignoresPaddingButKeepsPhysicalLineNumbers() throws IOException {
        Path file = temporaryDirectory.resolve("modules.txt");
        Files.writeString(file, "\uFEFF  CS2113 | 4 | a- | 0 | y2s1  \n\n  \ninvalid\n");

        Storage.LoadResult result = new Storage(file).load();

        assertEquals(1, result.modules().size());
        assertEquals(new Semester(2, 1), result.modules().get(0).getSemester());
        assertEquals(1, result.warnings().size());
        assertTrue(result.warnings().get(0).contains("Skipped line 4"));
    }

    @Test
    public void load_directoryInsteadOfFile_returnsWarning() {
        Storage.LoadResult result = new Storage(temporaryDirectory).load();

        assertTrue(result.modules().isEmpty());
        assertEquals(1, result.warnings().size());
        assertTrue(result.warnings().get(0).contains("Could not load modules"));
    }

    @Test
    public void load_invalidUtf8_returnsWarningWithoutThrowing() throws IOException {
        Path file = temporaryDirectory.resolve("modules.txt");
        Files.write(file, new byte[] {(byte) 0xC3, (byte) 0x28});

        assertFalse(new Storage(file).load().warnings().isEmpty());
    }

    @Test
    public void save_parentIsFile_returnsErrorWithoutChangingExistingFile() throws IOException {
        Path blocker = temporaryDirectory.resolve("data");
        Files.writeString(blocker, "keep this");

        String error = new Storage(blocker.resolve("modules.txt")).save(List.of()).orElseThrow();

        assertTrue(error.contains("Could not save modules"));
        assertTrue(error.contains("Changes are only in memory"));
        assertEquals("keep this", Files.readString(blocker));
    }

    @Test
    public void save_replacementFails_preservesTargetAndCleansTemporaryFile() throws IOException {
        Path target = Files.createDirectory(temporaryDirectory.resolve("modules.txt"));
        Path existing = target.resolve("existing.txt");
        Files.writeString(existing, "keep this");

        assertTrue(new Storage(target).save(List.of()).isPresent());

        assertEquals("keep this", Files.readString(existing));
        try (var files = Files.list(temporaryDirectory)) {
            assertEquals(List.of(target), files.toList());
        }
    }

    @Test
    public void save_emptyList_clearsPreviouslySavedModules() throws IOException {
        Path file = temporaryDirectory.resolve("modules.txt");
        Storage storage = new Storage(file);
        assertTrue(storage.save(List.of(new Module("CS2113", "CS2113", "", 4, Grade.A, false))).isEmpty());

        assertTrue(storage.save(List.of()).isEmpty());

        assertEquals("", Files.readString(file));
        assertTrue(storage.load().modules().isEmpty());
    }

    /** Checks every persisted value, since Module deliberately has no value-based equals method. */
    private void assertModuleEquals(Module expected, Module actual) {
        assertEquals(expected.getModuleCode(), actual.getModuleCode());
        assertEquals(expected.getCredit(), actual.getCredit());
        assertEquals(expected.getGrade(), actual.getGrade());
        assertEquals(expected.isSu(), actual.isSu());
        assertEquals(expected.getSemester(), actual.getSemester());
        assertEquals(expected.getModuleName(), actual.getModuleName());
        assertEquals(expected.getModuleDescription(), actual.getModuleDescription());
    }
}
