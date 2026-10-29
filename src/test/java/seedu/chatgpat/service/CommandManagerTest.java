package seedu.chatgpat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.exception.UnknownCommandException;
import seedu.chatgpat.parser.Parser;
import seedu.chatgpat.student.Student;

/**
 * Tests the {@link CommandManager} class.
 */
public class CommandManagerTest {

    /**
     * A dummy command used for testing purposes.
     */
    private static class DummyCommand extends Command {
        @Override
        public Response execute(Student student) {
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
                new Student(),
                List.of(dummyParser));

        Response response = manager.processQuery("dummy");
        assertEquals("dummy executed", response.message());
        assertFalse(response.isExit());
    }

    @Test
    public void processQuery_exitCommand_requestsExit() {
        CommandManager manager = new CommandManager(new Student());

        Response response = manager.processQuery("exit");

        assertTrue(response.isExit());
        assertEquals("Goodbye!", response.message());
    }

    @Test
    public void processQuery_helpCommand_listsAvailableCommandsAndKeepsRunning() {
        CommandManager manager = new CommandManager(new Student());

        Response response = manager.processQuery("help");

        assertTrue(response.message().contains("help -"));
        assertTrue(response.message().contains("add m/MODULE_CODE c/CREDITS g/GRADE -"));
        assertTrue(response.message().contains("list -"));
        assertTrue(response.message().contains("edit INDEX [m/MODULE_CODE] [c/CREDITS] [g/GRADE] -"));
        assertTrue(response.message().contains("delete INDEX -"));
        assertTrue(response.message().contains("gpa -"));
        assertTrue(response.message().contains("summary -"));
        assertTrue(response.message().contains("su INDEX -"));
        assertTrue(response.message().contains("unsu INDEX -"));
        assertTrue(response.message().contains("quota -"));
        assertTrue(response.message().contains("target TARGET_GPA r/REMAINING_MCS -"));
        assertTrue(response.message().contains("plan INDEX y/YEAR s/SEM -"));
        assertTrue(response.message().contains("unplan INDEX -"));
        assertTrue(response.message().contains("filter y/YEAR s/SEM -"));
        assertTrue(response.message().contains("filter unplanned -"));
        assertTrue(response.message().contains("exit -"));
        assertFalse(response.isExit());
        assertFalse(response.hasModule());
    }

    @Test
    public void processQuery_helpWithExtraArguments_returnsSameSummary() {
        CommandManager manager = new CommandManager(new Student());
        Response expected = manager.processQuery("help");

        assertEquals(expected, manager.processQuery("help 123"));
        assertEquals(expected, manager.processQuery("help\tanything"));
    }

    @Test
    public void processQuery_helpLikeWord_throwsUnknownCommandException() {
        CommandManager manager = new CommandManager(new Student());

        assertThrows(UnknownCommandException.class, () -> manager.processQuery("helpful"));
        assertThrows(UnknownCommandException.class, () -> manager.processQuery("help123"));
    }

    @Test
    public void processQuery_summary_reflectsChangesToModules() {
        CommandManager manager = new CommandManager(new Student());
        manager.processQuery("add m/CS2113 c/4 g/A-");
        manager.processQuery("add m/MA1521 c/4 g/B+");
        manager.processQuery("plan 1 y/2 s/1");

        assertEquals("""
                Y2S1 | 1 module | 4 MCs | GPA 4.50
                Unplanned | 1 module | 4 MCs""", manager.processQuery("summary").message());

        manager.processQuery("plan 1 y/1 s/2");
        manager.processQuery("plan 2 y/1 s/2");
        manager.processQuery("edit 2 c/2 g/A");
        manager.processQuery("su 1");

        assertEquals("""
                Y1S2 | 2 modules | 6 MCs | GPA 5.00
                Unplanned | 0 modules | 0 MCs""", manager.processQuery("summary").message());

        manager.processQuery("unsu 1");
        assertEquals("""
                Y1S2 | 2 modules | 6 MCs | GPA 4.67
                Unplanned | 0 modules | 0 MCs""", manager.processQuery("summary").message());

        manager.processQuery("delete 2");
        manager.processQuery("delete 1");
        assertEquals("Unplanned | 0 modules | 0 MCs", manager.processQuery("summary").message());
    }

    /**
     * Tests that an unknown command throws an {@link UnknownCommandException}.
     */
    @Test
    public void processQuery_unknownCommand_throwsUnknownCommandException() {
        CommandManager manager = new CommandManager(
                new Student(),
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
                    public Response execute(Student student) {
                        return new Response("second");
                    }
                })
                : Optional.empty();

        CommandManager manager = new CommandManager(
                new Student(),
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
                new Student(),
                List.of());

        assertThrows(UnknownCommandException.class, () -> manager.processQuery("anything"));
    }

    /**
     * Tests that the exception message contains the unknown command.
     */
    @Test
    public void processQuery_unknownCommand_exceptionMessageContainsCommand() {
        CommandManager manager = new CommandManager(
                new Student(),
                List.of(input -> Optional.empty()));

        UnknownCommandException exception = assertThrows(
                UnknownCommandException.class,
                () -> manager.processQuery("badcommand"));
        assertEquals(true, exception.getMessage().contains("badcommand"));
    }
}
