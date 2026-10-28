package seedu.chatgpat.command.module;

import seedu.chatgpat.command.Command;
import seedu.chatgpat.command.Response;
import seedu.chatgpat.student.Semester;
import seedu.chatgpat.student.Student;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FilterCommand extends Command {
    private static final Pattern PATTERN = Pattern.compile(
            "filter\\s+y/(\\d+)\\s+s/(\\d+)");
    private final int year;
    private final int semester;
    public FilterCommand(int year, int semester) {
        this.year = year;
        this.semester = semester;
    }
    /**
     * Parses the user input into a filterCommand if it matches the filter format.
     * @param userQuery The trimmed user input.
     * @return          The parsed filterCommand, or empty if the input does not match.
     */
    public static Optional<Command> parse(String userQuery) {
        Matcher matcher = PATTERN.matcher(userQuery);

        if (!matcher.matches()) {
            return Optional.empty();
        }

        int year = Integer.parseInt(matcher.group(1));
        int semester = Integer.parseInt(matcher.group(2));

        return Optional.of(new FilterCommand(year, semester));
    }
    @Override
    public Response execute(Student student){
        String message = "";
        Semester semester = new Semester(this.year, this.semester);
        ArrayList<Integer> filteredIndexes = student.filterModule(semester);
        if (filteredIndexes.isEmpty()) {
            message += String.format("No modules found in Y%dS%d.", this.year, this.semester);
        }else{
            message += String.format("Modules in Y%dS%d:%n", this.year, this.semester);
            for (int i : filteredIndexes) {
                message += String.format("%d. ",i) + student.getModule(i).toString()+String.format("%n");
            }
        }
        return new Response(message);
    }
}
