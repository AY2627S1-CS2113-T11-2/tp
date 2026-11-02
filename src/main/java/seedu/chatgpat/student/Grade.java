package seedu.chatgpat.student;

/**
 * Represents a letter grade on the NUS 5-point scale, with its grade point and
 * whether it counts towards the GPA.
 */
public enum Grade {
    A_PLUS("A+", 5.0, true),
    A("A", 5.0, true),
    A_MINUS("A-", 4.5, true),
    B_PLUS("B+", 4.0, true),
    B("B", 3.5, true),
    B_MINUS("B-", 3.0, true),
    C_PLUS("C+", 2.5, true),
    C("C", 2.0, true),
    D_PLUS("D+", 1.5, true),
    D("D", 1.0, true),
    F("F", 0.0, true),
    CS("CS", 0.0, false),
    CU("CU", 0.0, false);

    private final String label;
    private final double gradePoint;
    private final boolean affectsGpa;

    Grade(String label, double gradePoint, boolean affectsGpa) {
        this.label = label;
        this.gradePoint = gradePoint;
        this.affectsGpa = affectsGpa;
    }

    public String getLabel() {
        return label;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    /**
     * Returns whether this grade counts towards the GPA. The S/U grades {@code CS} and {@code CU} do not.
     *
     * @return True if this grade counts towards the GPA.
     */
    public boolean affectsGpa() {
        return affectsGpa;
    }

    /**
     * Converts a letter grade typed by the user, such as {@code A-}, to a Grade.
     * Letter case and surrounding spaces are ignored.
     *
     * @param input The letter grade.
     * @return The matching Grade.
     * @throws IllegalArgumentException If the input is null or is not a known grade.
     */
    public static Grade fromLabel(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Grade cannot be null");
        }
        String cleanInput = input.trim();
        for (Grade grade : Grade.values()) {
            if (grade.label.equalsIgnoreCase(cleanInput)) {
                return grade;
            }
        }
        throw new IllegalArgumentException("Invalid grade format: " + input);
    }

    /**
     * Returns the lowest letter grade whose grade point is at least the given grade point.
     * For example, 4.25 gives {@code A-} (4.5), because {@code B+} (4.0) would not be enough.
     *
     * @param gradePoint The grade point needed, from 0.0 to 5.0.
     * @return The lowest letter grade that reaches the given grade point.
     * @throws IllegalArgumentException If the grade point is not between 0.0 and 5.0.
     */
    public static String estimateLetterGrade(double gradePoint) {
        if (gradePoint < F.gradePoint || gradePoint > A_PLUS.gradePoint) {
            throw new IllegalArgumentException("grade point must be between 0.0 and 5.0");
        }
        Grade[] grades = Grade.values();
        // The grades are declared from highest to lowest, so they are checked from the end (F) upwards.
        for (int i = grades.length - 1; i >= 0; i--) {
            Grade grade = grades[i];
            if (grade.affectsGpa && grade.gradePoint >= gradePoint) {
                return grade.label;
            }
        }
        throw new AssertionError("A+ has the highest grade point, so a grade is always found");
    }
}
