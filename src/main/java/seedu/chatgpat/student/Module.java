package seedu.chatgpat.student;

/**
 * Stores a module's details, original grade, SU status and optional semester plan.
 */
public class Module {

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
                  int credit,Grade grade,boolean isSuApplied) {
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
