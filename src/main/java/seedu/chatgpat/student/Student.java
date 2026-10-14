package seedu.chatgpat.student;

import java.util.ArrayList;
import java.util.logging.Logger;

import seedu.chatgpat.exception.DuplicateModuleException;
import seedu.chatgpat.exception.EditException;
import seedu.chatgpat.exception.GpaException;
import seedu.chatgpat.exception.InvalidIndexException;
import seedu.chatgpat.exception.SuException;

public class Student {
    private static final Logger logger =
        Logger.getLogger(Student.class.getName());

    private String name;
    private String id;
    private ArrayList<Module> moduleList = new ArrayList<Module>();
    private int suQuota;
    Student(String name, String id,int suQuota) {
        this.name = name;
        this.id = id;
        this.suQuota = suQuota;
    }

    /**
     * Constructs a placeholding Student with the default set of params.
     */
    public Student() {
        this("John Doe", "A0000000B", 28);
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public int getSuQuota() {
        return suQuota;
    }
    public boolean isModuleExists(ArrayList<Module> modules, String newCode) {
        for (Module m : modules) {
            if (m.getModuleCode().equalsIgnoreCase(newCode)) {
                return true; // find duplicates
            }
        }
        return false;
    }

    /**
     * Adds a module to the student's record.
     *
     * @param  module The module to add.
     * @throws DuplicateModuleException
     *         If a module with the same code already exists.
     */
    public void addModule(Module module) throws DuplicateModuleException {
        if (isModuleExists(moduleList, module.getModuleCode())) {
            logger.warning(() -> String.format(
                    "Rejected adding duplicate module: %s", module.getModuleCode()));
            throw new DuplicateModuleException(module.getModuleCode());
        }
        moduleList.add(module);
    }

    public void printModuleList() {
        if (moduleList.isEmpty()) {
            System.out.println("No modules recorded yet.");
            return;
        }

        System.out.println("Here are your recorded modules:");
        for (int i = 0; i < moduleList.size(); i++) {
            Module module = moduleList.get(i);
            // String.format outputs "1. CS2113 | 4 MCs | Grade: A-"
            System.out.println((i + 1) + ". " + module.toString());
        }
    }

    /**
     * Deletes the module at the given 1-based index.
     *
     * @param  index The 1-based index of the module to delete.
     * @return       The module that was deleted.
     * @throws InvalidIndexException
     *         If the index is out of range.
     */
    public Module deleteModule(int index) {
        Module module = getModule(index);
        moduleList.remove(index - 1);
        return module;
    }

    /**
     * Returns the module at the given 1-based index.
     *
     * @param  index The 1-based index of the module.
     * @return       The module at the given index.
     * @throws InvalidIndexException
     *         If the index is out of range.
     */
    public Module getModule(int index) {
        if (index < 1 || index > moduleList.size()) {
            logger.warning(() -> String.format(
                    "Rejected getModule(%d); module list size is %d",
                    index, moduleList.size()));
            throw new InvalidIndexException(index, moduleList.size());
        }
        Module module = moduleList.get(index - 1);
        assert module != null : "Module at a valid index should not be null";
        return module;
    }

    /**
     * Returns the total modular credits currently SU-ed.
     *
     * @return The total SU-ed modular credits.
     */
    public int getUsedSuMcs() {
        int total = 0;
        for (Module module : moduleList) {
            if (module.isSu()) {
                total += module.getCredit();
            }
        }
        return total;
    }

    /**
     * Returns the remaining SU quota in modular credits.
     *
     * @return The remaining SU quota.
     */
    public int getRemainingSuQuota() {
        return suQuota - getUsedSuMcs();
    }

    /**
     * Applies SU on the module at the given 1-based index.
     * The module is excluded from GPA calculation afterwards.
     *
     * @param  index The 1-based index of the module to SU.
     * @return       The module that was SU-ed.
     * @throws InvalidIndexException
     *         If the index is out of range.
     * @throws SuException
     *         If the module is already SU-ed, or if the remaining quota is insufficient.
     */
    public Module applySu(int index) {
        Module module = getModule(index);
        if (module.isSu()) {
            logger.warning(() -> String.format(
                    "Rejected SU on module %d (%s): already SU-ed",
                    index, module.getModuleCode()));
            throw new SuException(String.format(
                    "Module %d is already SU-ed.", index));
        }
        int remainingQuota = getRemainingSuQuota();
        if (module.getCredit() > remainingQuota) {
            logger.warning(() -> String.format(
                    "Rejected SU on module %d (%s): needs %d MCs but only %d left",
                    index, module.getModuleCode(),
                    module.getCredit(), remainingQuota));
            throw new SuException(String.format(
                    "Insufficient SU quota. Remaining: %d MCs, required: %d MCs.",
                    remainingQuota, module.getCredit()));
        }
        module.setSu(true);
        assert module.isSu() : "Module should be SU-ed after applySu";
        return module;
    }

    /**
     * Returns the number of modules currently recorded.
     *
     * @return The module count.
     */
    public int getModuleCount() {
        return moduleList.size();
    }

    /**
     * Removes SU on the module at the given 1-based index.
     * The module is included into GPA calculation afterwards.
     *
     * @param  index The 1-based index of the module to SU.
     * @return       The module that has SU removed.
     * @throws InvalidIndexException
     *         If the index is out of range.
     * @throws SuException
     *         If the module is already not SU-ed.
     */
    public Module removeSu(int index) {
        Module module = getModule(index);
        if (!module.isSu()) {
            logger.warning(() -> String.format(
                    "Rejected removing SU on module %d (%s): already not SU-ed",
                    index, module.getModuleCode()));
            throw new SuException(String.format(
                    "Module %d is already not SU-ed.", index));
        }
        module.setSu(false);
        assert !module.isSu() : "Module should not be SU-ed after removeSu";
        return module;
    }

    /**
     * Calculates the student's GPA, excluding modules that do not affect
     * GPA (SU-ed modules, and modules with grades such as CS or CU).
     *
     * @return The calculated GPA.
     * @throws GpaException If no module contributes to GPA.
     */
    public double calculateGpa() {
        double totalGradePoints = 0.0;
        int totalCredits = 0;
        for (Module module : moduleList) {
            if (module.isSu()) {
                continue;
            }
            Grade grade = module.getGrade();
            if (!grade.isAffectsGpa()) {
                continue;
            }
            totalGradePoints += grade.getGradePoint() * module.getCredit();
            totalCredits += module.getCredit();
        }
        if (totalCredits == 0) {
            logger.warning("Rejected GPA calculation: no module contributes to GPA");
            throw new GpaException(
                    "No modules contribute to GPA. Add a graded module first.");
        }
        double gpa = totalGradePoints / totalCredits;
        assert gpa >= 0.0 && gpa <= 5.0 : "GPA should be within [0, 5]";
        return gpa;
    }

    /**
     * Edits the module at the given 1-based index.
     * A null argument leaves the corresponding field unchanged.
     *
     * @param  index     The 1-based index of the module to edit.
     * @param  newCode   The new module code, or null to keep the current one.
     * @param  newCredit The new credit value, or null to keep the current one.
     * @param  newGrade  The new grade, or null to keep the current one.
     * @return           The edited module.
     * @throws InvalidIndexException
     *         If the index is out of range.
     * @throws EditException
     *         If no field is given, the credit is not positive, or the new code already exists.
     */
    public Module editModule(int index, String newCode, Integer newCredit, Grade newGrade) {
        Module module = getModule(index);
        if (newCode == null && newCredit == null && newGrade == null) {
            throw new EditException("Provide at least one field to edit.");
        }
        if (newCredit != null && newCredit <= 0) {
            throw new EditException("Credits must be a positive integer.");
        }
        if (newCode != null && !newCode.equalsIgnoreCase(module.getModuleCode())
                && isModuleExists(moduleList, newCode)) {
            throw new EditException("Module already exists: " + newCode);
        }

        if (newCode != null) {
            module.setModuleCode(newCode);
        }
        if (newCredit != null) {
            module.setCredit(newCredit);
        }
        if (newGrade != null) {
            module.setGrade(newGrade);
        }
        return module;
    }
}
