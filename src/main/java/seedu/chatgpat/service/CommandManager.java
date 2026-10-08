package seedu.chatgpat.service;

import java.util.List;
import java.util.Optional;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.UnknownCommandException;
import seedu.chatgpat.parser.Parser;

/**
 * Manages the parsing and execution of user commands.
 */
public class CommandManager {
    private final ModuleManager moduleManager;
    private final List<Parser<Command>> parsers;

    /**
     * Constructs a CommandManager with the default set of parsers.
     *
     * @param moduleManager The module manager containing the module list.
     */
    public CommandManager(ModuleManager moduleManager) {
        this(moduleManager, List.of());
    }

    /**
     * Constructs a CommandManager with a custom list of parsers.
     * This constructor is primarily for testing.
     *
     * @param moduleManager The module manager containing the module list.
     * @param parsers       The list of parsers to use for command resolution.
     */
    public CommandManager(ModuleManager moduleManager, List<Parser<Command>> parsers) {
        this.moduleManager = moduleManager;
        this.parsers = parsers;
    }

    /**
     * Parses the user input into a command, executes it, and returns the response.
     *
     * @param  userQuery Trimmed user input.
     * @return           The response answering the user input.
     * @throws UnknownCommandException
     *         If no parser can handle the input.
     */
    public Response processQuery(String userQuery) {
        for (Parser<Command> parser : parsers) {
            Optional<Command> maybeCommand = parser.parse(userQuery);
            if (maybeCommand.isPresent()) {
                Command command = maybeCommand.get();
                return command.execute(moduleManager);
            }
        }
        throw new UnknownCommandException(userQuery);
    }
}
