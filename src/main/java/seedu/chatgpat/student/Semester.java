package seedu.chatgpat.student;

import seedu.chatgpat.exception.PlanException;

/**
 * Represents a semester, identified by the year of study and the semester number.
 *
 * @param year The year of study, from MIN_YEAR to MAX_YEAR.
 * @param term The semester number, from MIN_TERM to MAX_TERM.
 */
public record Semester(int year, int term) {
    public static final int MIN_YEAR = 1;
    public static final int MAX_YEAR = 4;
    public static final int MIN_TERM = 1;
    public static final int MAX_TERM = 2;

    /**
     * Constructs a Semester after validating the year and semester number.
     *
     * @throws PlanException
     *         If the year or semester number is out of range.
     */
    public Semester {
        if (year < MIN_YEAR || year > MAX_YEAR) {
            throw new PlanException(String.format(
                    "Year must be between %d and %d, there is an invalid year: %d",
                    MIN_YEAR, MAX_YEAR, year));
        }
        if (term < MIN_TERM || term > MAX_TERM) {
            throw new PlanException(String.format(
                    "Semester must be %d or %d, there is an invalid semester: %d",
                    MIN_TERM, MAX_TERM, term));
        }
    }

    @Override
    public String toString() {
        return String.format("Y%dS%d", year, term);
    }
}
