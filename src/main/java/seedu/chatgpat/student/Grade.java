package seedu.chatgpat.student;

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

    /**
     * Change String letter grade to enum Grade
     * @param input
     * @return corresponding enum Grade
     * @throws IllegalArgumentException if the inout is null or in invalid format
     */
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

    /**
     * Round up the grade points to the corresponding string letter grade.
     * @param gradePoint gradePoint of a module
     * @return sting letter grade just greater than the corresponding grade point
     * @throws IllegalArgumentException if grade point is not between 0.0 and 5.0.
     */
    public static String estimateLetterGrade(double gradePoint) {
        if(gradePoint < 0.0 || gradePoint > 5.0) {
            throw new IllegalArgumentException("grade point must be between 0.0 and 5.0");
        }
        if(gradePoint > 4.5) {
            return "A";
        } else if (gradePoint > 4.0) {
            return "A-";
        } else if (gradePoint > 3.5) {
            return "B+";
        }  else if (gradePoint > 3.0) {
            return "B";
        } else if (gradePoint > 2.5) {
            return "B-";
        }else if (gradePoint > 2.0) {
            return "C+";
        }else if (gradePoint > 1.5) {
            return "C";
        }else if (gradePoint >  1.0) {
            return "D+";
        }else if (gradePoint >  0.0) {
            return "D";
        }else{
            return "F";
        }
    }

}


