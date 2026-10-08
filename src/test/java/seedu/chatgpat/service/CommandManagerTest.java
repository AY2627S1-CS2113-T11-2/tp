package seedu.chatgpat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.UnknownCommandException;
import seedu.chatgpat.parser.Parser;

/**
 * Tests the {@link CommandManager} class.
 */
public class CommandManagerTest {

    /**
     * A dummy command used for testing purposes.
     */
    private static class DummyCommand extends Command {
        @Override
        public Response execute(ModuleManager moduleManager) {
            return new Response("dummy executed");
        }
    }

    /**
     * Tests that a known command is parsed and executed successfully.
     */
    @Test
    public void processQuery_knownCommand_returnsResponse() {
        Parser<Command> dummyParser = input -> input.equals("dummy")
                ? Optional.of(new DummyCommand())
                : Optional.empty();

        CommandManager manager = new CommandManager(
                new ModuleManager(),
                List.of(dummyParser));

        Response response = manager.processQuery("dummy");
        assertEquals("dummy executed", response.message());
    }

    /**
     * Tests that an unknown command throws an {@link UnknownCommandException}.
     */
    @Test
    public void processQuery_unknownCommand_throwsUnknownCommandException() {
        CommandManager manager = new CommandManager(
                new ModuleManager(),
                List.of(input -> Optional.empty()));

        assertThrows(UnknownCommandException.class, () -> manager.processQuery("nonsense"));
    }

    /**
     * Tests that the first matching parser is used when multiple parsers are present.
     */
    @Test
    public void processQuery_multipleParsers_usesFirstMatch() {
        Parser<Command> firstParser = input -> input.equals("test")
                ? Optional.of(new DummyCommand())
                : Optional.empty();
        Parser<Command> secondParser = input -> input.equals("test")
                ? Optional.of(new Command() {
                    @Override
                    public Response execute(ModuleManager moduleManager) {
                        return new Response("second");
                    }
                })
                : Optional.empty();

        CommandManager manager = new CommandManager(
                new ModuleManager(),
                List.of(firstParser, secondParser));

        Response response = manager.processQuery("test");
        assertEquals("dummy executed", response.message());
    }

    /**
     * Tests that an empty parser list throws an {@link UnknownCommandException}.
     */
    @Test
    public void processQuery_emptyParserList_throwsUnknownCommandException() {
        CommandManager manager = new CommandManager(
                new ModuleManager(),
                List.of());

        assertThrows(UnknownCommandException.class, () -> manager.processQuery("anything"));
    }

    /**
     * Tests that the exception message contains the unknown command.
     */
    @Test
    public void processQuery_unknownCommand_exceptionMessageContainsCommand() {
        CommandManager manager = new CommandManager(
                new ModuleManager(),
                List.of(input -> Optional.empty()));

        UnknownCommandException exception = assertThrows(
                UnknownCommandException.class,
                () -> manager.processQuery("badcommand"));
        assertEquals(true, exception.getMessage().contains("badcommand"));
    }
}
