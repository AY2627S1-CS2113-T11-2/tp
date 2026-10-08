package seedu.chatgpat.module;

/**
 * Represents a module record with a module code, modular credits, grade, and SU status.
 */
public class Module {
    private String moduleCode;
    private int credits;
    private String grade;
    private boolean isSu;

    /**
     * Constructs a new Module.
     *
     * @param moduleCode The module code, e.g. CS2113.
     * @param credits    The modular credits. Must be positive.
     * @param grade      The grade, e.g. A-.
     */
    public Module(String moduleCode, int credits, String grade) {
        this.moduleCode = moduleCode;
        this.credits = credits;
        this.grade = grade;
        this.isSu = false;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public boolean isSu() {
        return isSu;
    }

    public void setSu(boolean su) {
        isSu = su;
    }

    @Override
    public String toString() {
        return String.format(
            "%s | %d MCs | Grade: %s%s",
            moduleCode,
            credits,
            grade,
            isSu ? " | SU" : ""
        );
    }
}
