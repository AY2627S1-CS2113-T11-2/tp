package seedu.duke;

public class Module {

    private String moduleCode;
    private String moduleName;
    private String moduleDescription;
    private int credit;
    private Grade grade;
    private boolean isSuApplied;
    public Module(String moduleCode, String moduleName, String moduleDescription, int credit,Grade grade,boolean isSuApplied) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.moduleDescription = moduleDescription;
        this.grade = grade;
        this.credit = credit;
        this.isSuApplied = false;
    }
    public String getModuleCode() {
        return moduleCode;
    }
    public String getModuleName() {
        return moduleName;
    }
    public void showModuleDescription() {
        System.out.println(moduleDescription);
    }
    public int getCredit() {
        return credit;
    }
    public Grade getGrade() {
        return grade;
    }
    @Override
    public String toString() {
        String suTag = isSuApplied ? " [S/U]" : "";
        return String.format("%s | %d MCs | Grade: %s%s",
                moduleCode, credit, grade.getLabel(), suTag);
    }

}
