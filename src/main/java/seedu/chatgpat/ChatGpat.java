package seedu.chatgpat;

import java.util.List;

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
        //assert false : "dummy assertion set to fail";
        ChatGpat chatGpat = new ChatGpat();
        chatGpat.run();

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
