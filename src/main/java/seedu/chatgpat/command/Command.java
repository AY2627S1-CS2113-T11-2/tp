package seedu.chatgpat.command;

import seedu.chatgpat.student.Student;

/**
 * Represents the template for commands.
 */
public abstract class Command {
    /**
     * Represents the template for execution of the command.
     *
     * @param  student The student containing the module list.
     * @return         The response, to be printed on screen.
     */
    public abstract Response execute(Student student);
}
