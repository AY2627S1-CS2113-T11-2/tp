package seedu.duke;

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

    private final String label; //Letter Grade
    private final double gradePoint; // corresponding gradePoint
    private final boolean affectsGpa; // affectGPA or not

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

    public boolean isAffectsGpa() {
        return affectsGpa;
    }

    // Change Sring letter grade to enum grade
    public static Grade fromLabel(String input) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException("Grade cannot be null");
        }
        String cleanInput = input.trim().toUpperCase();
        for (Grade g : Grade.values()) {
            if (g.label.equalsIgnoreCase(cleanInput)) {
                return g;
            }
        }
        throw new IllegalArgumentException("Invalid grade format: " + input);
    }
}