package seedu.chatgpat.storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.chatgpat.student.Grade;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Semester;

/**
 * Reads and writes UTF-8 module records without printing anything.
 * Fields are code, MCs, grade, SU flag, semester, name and description, in that order.
 * New optional fields must be appended so older records can use defaults.
 */
public class Storage {
    public static final Path DEFAULT_FILE_PATH = Path.of("data", "modules.txt");
    private static final Pattern SEMESTER_PATTERN = Pattern.compile("Y([1-4])S([1-2])",
            Pattern.CASE_INSENSITIVE);
    private final Path file;

    /**
     * Contains successfully loaded modules and warnings for the UI to display.
     *
     * @param modules The valid modules in file order, with duplicate codes excluded.
     * @param warnings The file errors and malformed-line warnings, including line numbers.
     */
    public record LoadResult(List<Module> modules, List<String> warnings) {
        public LoadResult {
            modules = List.copyOf(modules);
            warnings = List.copyOf(warnings);
        }
    }

    /**
     * Constructs storage for the given file; no file is created until the first save.
     *
     * @param file The data file, relative to the working directory or absolute.
     */
    public Storage(Path file) {
        this.file = file.toAbsolutePath();
    }

    /**
     * Loads valid records, skipping blank lines and reporting each malformed line.
     * A missing file means an empty record; other read failures become warnings.
     *
     * @return The loaded modules and any warnings, without changing the file.
     */
    public LoadResult load() {
        List<Module> modules = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        Set<String> codes = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                // Some text editors include a UTF-8 byte order mark at the start of the file.
                if (lineNumber == 1 && line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }
                if (line.isBlank()) {
                    continue;
                }
                try {
                    Module module = parseModule(line);
                    if (!codes.add(module.getModuleCode())) {
                        throw new IllegalArgumentException("Duplicate module code: " + module.getModuleCode());
                    }
                    modules.add(module);
                } catch (IllegalArgumentException e) {
                    warnings.add("Skipped line " + lineNumber + " in " + file + ": " + e.getMessage());
                }
            }
        } catch (NoSuchFileException e) {
            // First run: the directory and file will be created by save().
        } catch (IOException | SecurityException e) {
            warnings.add("Could not load modules from " + file + ": " + e.getMessage());
        }
        return new LoadResult(modules, warnings);
    }

    /**
     * Saves all modules, writing a sibling temporary file before replacing the old file.
     * Atomic replacement is used where supported to avoid partially written records.
     *
     * @param modules The complete module list in display order.
     * @return An error message on failure, or empty on success. In-memory data is unchanged.
     */
    public Optional<String> save(List<Module> modules) {
        Path temporaryFile = null;
        try {
            List<String> lines = new ArrayList<>();
            for (Module module : modules) {
                lines.add(formatModule(module));
            }
            Files.createDirectories(file.getParent());
            temporaryFile = Files.createTempFile(file.getParent(), ".modules-", ".tmp");
            Files.write(temporaryFile, lines, StandardCharsets.UTF_8);
            try {
                Files.move(temporaryFile, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporaryFile, file, StandardCopyOption.REPLACE_EXISTING);
            }
            return Optional.empty();
        } catch (IOException | SecurityException e) {
            return Optional.of("Could not save modules to " + file + ": " + e.getMessage()
                    + "\nChanges are only in memory. Fix the file or folder access and run a data-changing command"
                    + " to retry before exiting.");
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException | SecurityException e) {
                    // Cleanup must not hide the save result or interrupt the command loop.
                }
            }
        }
    }

    /**
     * Parses the three required fields and supplies defaults for omitted optional fields.
     */
    private Module parseModule(String line) {
        List<String> fields = splitFields(line);
        if (fields.size() < 3 || fields.size() > 7) {
            throw new IllegalArgumentException("Expected 3 to 7 fields: code | MCs | grade | SU | semester | name"
                    + " | description.");
        }
        String code = fields.get(0);
        if (code.isBlank() || code.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("Module code must be non-empty and contain no whitespace.");
        }
        int credits;
        try {
            credits = Integer.parseInt(fields.get(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("MCs must be a positive integer.");
        }
        if (credits <= 0) {
            throw new IllegalArgumentException("MCs must be a positive integer.");
        }
        Grade grade = Grade.fromLabel(fields.get(2));
        String su = optionalField(fields, 3, "0");
        if (!su.equals("0") && !su.equals("1")) {
            throw new IllegalArgumentException("SU flag must be 0 or 1.");
        }
        Module module = new Module(code, optionalField(fields, 5, code), optionalField(fields, 6, ""),
                credits, grade, su.equals("1"));
        String semester = optionalField(fields, 4, "-");
        if (!semester.equals("-")) {
            Matcher matcher = SEMESTER_PATTERN.matcher(semester);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Semester must be Y1S1 to Y4S2, or - for unplanned.");
            }
            module.setSemester(new Semester(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))));
        }
        return module;
    }

    /** Returns the default for an omitted or empty optional field. */
    private String optionalField(List<String> fields, int index, String defaultValue) {
        return index >= fields.size() || fields.get(index).isEmpty() ? defaultValue : fields.get(index);
    }

    /** Splits only unescaped pipes, trimming separator padding before decoding each field. */
    private List<String> splitFields(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char current = line.charAt(i);
            if (current == '\\') {
                field.append(current);
                if (++i >= line.length()) {
                    throw new IllegalArgumentException("Incomplete escape at end of line.");
                }
                field.append(line.charAt(i));
            } else if (current == '|') {
                fields.add(decodeField(field.toString().trim()));
                field.setLength(0);
            } else {
                field.append(current);
            }
        }
        fields.add(decodeField(field.toString().trim()));
        return fields;
    }

    /** Restores escaped delimiters and whitespace without introducing physical record breaks. */
    private String decodeField(String field) {
        StringBuilder decoded = new StringBuilder();
        for (int i = 0; i < field.length(); i++) {
            char current = field.charAt(i);
            if (current != '\\') {
                decoded.append(current);
                continue;
            }
            if (++i >= field.length()) {
                throw new IllegalArgumentException("Incomplete escape in field.");
            }
            char escaped = field.charAt(i);
            decoded.append(switch (escaped) {
            case '\\', '|' -> escaped;
            case 'n' -> '\n';
            case 'r' -> '\r';
            case 't' -> '\t';
            case 's' -> ' ';
            default -> throw new IllegalArgumentException("Unknown escape: \\" + escaped);
            });
        }
        return decoded.toString();
    }

    /** Serializes every persisted module field in the documented, append-only field order. */
    private String formatModule(Module module) {
        String semester = module.getSemester() == null ? "-" : module.getSemester().toString();
        return String.join(" | ", encodeField(module.getModuleCode()), Integer.toString(module.getCredit()),
                module.getGrade().getLabel(), module.isSu() ? "1" : "0", semester,
                encodeField(module.getModuleName()), encodeField(module.getModuleDescription()));
    }

    /** Escapes reserved characters and boundary spaces so text survives a save/load round trip. */
    private String encodeField(String field) {
        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < field.length(); i++) {
            char current = field.charAt(i);
            encoded.append(switch (current) {
            case '\\' -> "\\\\";
            case '|' -> "\\|";
            case '\n' -> "\\n";
            case '\r' -> "\\r";
            case '\t' -> "\\t";
            case ' ' -> i == 0 || i == field.length() - 1 ? "\\s" : " ";
            default -> Character.toString(current);
            });
        }
        return encoded.toString();
    }
}
