package seedu.chatgpat;

import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.ChatGpatException;
import seedu.chatgpat.service.ChatInterface;
import seedu.chatgpat.service.CommandManager;
import seedu.chatgpat.service.ModuleManager;

/**
 * Represents a CLI program for student's grades management.
 */
public class ChatGpat {
    private final ChatInterface ui;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;

    /**
     * Constructs a new ChatGpat.
     */
    public ChatGpat() {
        ui = new ChatInterface();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager(moduleManager);
    }

    /**
     * Starts the new ChatGpat program.
     */
    public static void main(String[] args) {
        ChatGpat chatGpat = new ChatGpat();
        chatGpat.run();
    }

    /**
     * Starts the program.
     * Loops between user inputs and giving responses.
     */
    private void run() {
        ui.printWelcome();
        String userQuery;
        Response response;

        while (true) {
            try {
                userQuery = ui.getUserQuery();
                response = commandManager.processQuery(userQuery);
                ui.printResponse(response);

            } catch (ChatGpatException e) {
                response = new Response(e.getMessage());
                ui.printResponse(response);

            } catch (Exception e) {
                ui.printError(e);
            }
        }
    }
}
