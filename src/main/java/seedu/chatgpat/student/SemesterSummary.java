package seedu.chatgpat.student;

import java.util.OptionalDouble;

/**
 * A snapshot of the module totals and GPA for a semester or the unplanned group.
 *
 * @param semester     The semester, or null for unplanned modules.
 * @param moduleCount  The number of modules, including SU-ed modules and non-GPA grades.
 * @param totalCredits The total MCs, including SU-ed modules and non-GPA grades.
 * @param gpa          The credit-weighted GPA, or empty for unplanned modules or no contributing credits.
 */
public record SemesterSummary(Semester semester, int moduleCount, int totalCredits, OptionalDouble gpa) {
}
