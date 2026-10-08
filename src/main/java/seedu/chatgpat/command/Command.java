package seedu.chatgpat.command;

import seedu.chatgpat.service.ModuleManager;

/**
 * Represents the template for commands.
 */
public abstract class Command {
    /**
     * Represents the template for execution of the command.
     *
     * @param  moduleManager The module manager containing the module list.
     * @return               The response, to be printed on screen.
     */
    public abstract Response execute(ModuleManager moduleManager);
}
