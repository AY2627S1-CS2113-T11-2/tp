package seedu.chatgpat.student;

/**
 * Stores a module's details, original grade, SU status and optional semester plan.
 */
public class Module {
    /** The fewest MCs a module can be worth. */
    public static final int MIN_CREDITS = 1;
    /** The most MCs a module can be worth. The limit keeps GPA totals realistic and prevents overflow. */
    public static final int MAX_CREDITS = 40;
    /** The message shown when a number of MCs is outside the allowed range. */
    public static final String MESSAGE_INVALID_CREDITS =
            String.format("Credits must be a whole number from %d to %d.", MIN_CREDITS, MAX_CREDITS);

    private String moduleCode;
    private String moduleName;
    private String moduleDescription;
    private int credit;
    private Grade grade;
    private boolean isSuApplied;
    private Semester semester;

    /**
     * Constructs a module, preserving its SU status when restoring saved records.
     * The semester is initially unplanned and can be assigned with {@link #setSemester(Semester)}.
     */
    public Module(String moduleCode, String moduleName, String moduleDescription,
                  int credit, Grade grade, boolean isSuApplied) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.moduleDescription = moduleDescription;
        this.grade = grade;
        this.credit = credit;
        this.isSuApplied = isSuApplied;
    }
    public String getModuleCode() {
        return moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public int getCredit() {
        return credit;
    }

    /**
     * Returns whether the given number of MCs is allowed for a module.
     *
     * @param credit The number of MCs to check.
     * @return True if it is from {@link #MIN_CREDITS} to {@link #MAX_CREDITS}.
     */
    public static boolean isValidCredit(int credit) {
        return credit >= MIN_CREDITS && credit <= MAX_CREDITS;
    }

    public Grade getGrade() {
        return grade;
    }

    /**
     * Returns whether this module has been SU-ed.
     *
     * @return True if this module is SU-ed, false otherwise.
     */
    public boolean isSu() {
        return isSuApplied;
    }

    /**
     * Sets the SU status of this module.
     *
     * @param isSuApplied The new SU status.
     */
    public void setSu(boolean isSuApplied) {
        this.isSuApplied = isSuApplied;
    }

    /**
     * Returns the semester this module is planned for.
     *
     * @return The semester, or null if the module is not planned.
     */
    public Semester getSemester() {
        return semester;
    }

    /**
     * Sets the semester this module is planned for.
     *
     * @param semester The new semester, or null to mark the module as unplanned.
     */
    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        String suTag = isSuApplied ? " [S/U]" : "";
        String semesterTag = (semester == null) ? "" : " | " + semester;
        return String.format("%s | %d MCs | Grade: %s%s%s",
                moduleCode, credit, grade.getLabel(), suTag, semesterTag);
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
