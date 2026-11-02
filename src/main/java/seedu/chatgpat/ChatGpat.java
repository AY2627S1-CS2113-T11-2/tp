package seedu.chatgpat;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.ChatGpatException;
import seedu.chatgpat.service.ChatInterface;
import seedu.chatgpat.service.CommandManager;
import seedu.chatgpat.storage.Storage;
import seedu.chatgpat.student.Module;
import seedu.chatgpat.student.Student;

/**
 * Represents a CLI program for student's grades management.
 */
public class ChatGpat {
    /** File that log messages are written to, in the folder where the program is run. */
    private static final String LOG_FILE = "chatgpat.log";

    private final ChatInterface ui;
    private final Student student;
    private final CommandManager commandManager;
    private final Storage storage;

    /**
     * Constructs a new ChatGpat.
     */
    public ChatGpat() {
        this(new ChatInterface(), new Storage(Storage.DEFAULT_FILE_PATH));
    }

    /**
     * Supplies the UI and storage, allowing tests to use a temporary file and scripted input.
     *
     * @param ui The interface responsible for all user interaction.
     * @param storage The local module storage.
     */
    ChatGpat(ChatInterface ui, Storage storage) {
        this.ui = ui;
        this.storage = storage;
        student = new Student();
        commandManager = new CommandManager(student);
    }

    /**
     * Starts the new ChatGpat program.
     */
    public static void main(String[] args) {
        setUpLogging();
        ChatGpat chatGpat = new ChatGpat();
        chatGpat.run();
    }

    /**
     * Sends log messages to a log file instead of the terminal, so that they do not
     * clutter the conversation with the user. If the log file cannot be created,
     * for example in a write-protected folder, the program runs without logging.
     */
    private static void setUpLogging() {
        LogManager.getLogManager().reset();
        try {
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            Logger.getLogger("").addHandler(fileHandler);
        } catch (IOException | SecurityException e) {
            // Logging is only for troubleshooting, so the program continues without it.
        }
    }

    /**
     * Starts the program.
     * Loops between user inputs and giving responses until a command requests exit.
     */
    void run() {
        Storage.LoadResult loaded = storage.load();
        for (Module module : loaded.modules()) {
            student.addModule(module);
        }
        ui.printWelcome(loaded.warnings());
        String userQuery;
        Response response;

        while (true) {
            try {
                userQuery = ui.getUserQuery();
                response = commandManager.processQuery(userQuery);
                assert response != null : "Every command returns a response";
                List<String> warnings = response.isDataChanged()
                        ? storage.save(student.getModules()).stream().toList() : List.of();
                ui.printResponse(response, warnings);
                if (response.isExit()) {
                    return;
                }

            } catch (ChatGpatException e) {
                response = new Response(e.getMessage());
                ui.printResponse(response);

            } catch (Exception e) {
                ui.printError(e);
            }
        }
    }
}
