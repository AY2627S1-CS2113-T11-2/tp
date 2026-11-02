package seedu.chatgpat.student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.logging.Logger;

import seedu.chatgpat.exception.DuplicateModuleException;
import seedu.chatgpat.exception.EditException;
import seedu.chatgpat.exception.GpaException;
import seedu.chatgpat.exception.InvalidIndexException;
import seedu.chatgpat.exception.PlanException;
import seedu.chatgpat.exception.SuException;
import seedu.chatgpat.exception.TargetException;

/**
 * Stores a student's modules and manages their grades, semester plans, and SU quota.
 */
public class Student {
    /** The highest grade point on the NUS scale, which is also the highest possible GPA. */
    private static final double MAX_GRADE_POINT = 5.0;
    private static final Logger logger =
            Logger.getLogger(Student.class.getName());

    private String name;
    private String id;
    private ArrayList<Module> moduleList = new ArrayList<Module>();
    private int suQuota;

    private record GpaData(double totalGradePoints, int totalCredits) {
    }

    Student(String name, String id, int suQuota) {
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

    /**
     * Returns whether a module with the given code is already recorded, ignoring letter case.
     *
     * @param code The module code to look for.
     * @return True if another module already uses the code.
     */
    private boolean hasModuleCode(String code) {
        for (Module module : moduleList) {
            if (module.getModuleCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a module to the student's record.
     *
     * @param module The module to add.
     * @throws DuplicateModuleException If a module with the same code already exists.
     */
    public void addModule(Module module) throws DuplicateModuleException {
        if (hasModuleCode(module.getModuleCode())) {
            logger.warning(() -> String.format(
                    "Rejected adding duplicate module: %s", module.getModuleCode()));
            throw new DuplicateModuleException(module.getModuleCode());
        }
        moduleList.add(module);
    }

    /**
     * Returns an unmodifiable copy of the module list in display order for saving.
     * Module objects retain their current field values.
     *
     * @return The modules in their current order.
     */
    public List<Module> getModules() {
        return List.copyOf(moduleList);
    }

    /**
     * Deletes the module at the given 1-based index.
     *
     * @param index The 1-based index of the module to delete.
     * @return The module that was deleted.
     * @throws InvalidIndexException If the index is out of range.
     */
    public Module deleteModule(int index) {
        Module module = getModule(index);
        moduleList.remove(index - 1);
        return module;
    }

    /**
     * Returns the module at the given 1-based index.
     *
     * @param index The 1-based index of the module.
     * @return The module at the given index.
     * @throws InvalidIndexException If the index is out of range.
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
     * @param index The 1-based index of the module to SU.
     * @return The module that was SU-ed.
     * @throws InvalidIndexException If the index is out of range.
     * @throws SuException           If the module is already SU-ed, or if the remaining quota is insufficient.
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
     * @param index The 1-based index of the module to SU.
     * @return The module that has SU removed.
     * @throws InvalidIndexException If the index is out of range.
     * @throws SuException           If the module is already not SU-ed.
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
     * Calculates total grade points and GPA-contributing credits for the given modules,
     * excluding modules that do not affect GPA (SU-ed modules, and modules with grades such as CS or CU).
     *
     * @param modules The modules to include in the calculation.
     * @return The weighted grade points and GPA-contributing credits.
     */
    private GpaData calculateGpaData(Iterable<Module> modules) {
        double totalGradePoints = 0.0;
        int totalCredits = 0;

        for (Module module : modules) {
            if (module.isSu()) {
                continue;
            }

            Grade grade = module.getGrade();
            if (!grade.affectsGpa()) {
                continue;
            }

            totalGradePoints += grade.getGradePoint() * module.getCredit();
            totalCredits += module.getCredit();
        }

        return new GpaData(totalGradePoints, totalCredits);
    }

    /**
     * Calculates the student's GPA, excluding modules that do not affect
     * GPA (SU-ed modules, and modules with grades such as CS or CU).
     *
     * @return The calculated GPA.
     * @throws GpaException If no module contributes to GPA.
     */
    public double calculateGpa() {
        GpaData data = calculateGpaData(moduleList);

        if (data.totalCredits() == 0) {
            logger.warning(
                    "Rejected GPA calculation: no module contributes to GPA");
            throw new GpaException(
                    "No modules contribute to GPA. Add a graded module first.");
        }

        double gpa = data.totalGradePoints() / data.totalCredits();

        assert gpa >= 0.0 && gpa <= MAX_GRADE_POINT
                : "GPA should be within [0, 5]";

        return gpa;
    }

    /**
     * Summarises occupied semesters in year and term order, followed by unplanned modules.
     * All modules count towards module and MC totals; GPA uses the same exclusions as {@link #calculateGpa()}.
     * The unplanned summary is always included and has no GPA.
     *
     * @return The semester summaries, with the unplanned summary last.
     */
    public List<SemesterSummary> getSemesterSummaries() {
        Comparator<Semester> order = Comparator.comparingInt(Semester::year).thenComparingInt(Semester::term);
        Map<Semester, List<Module>> groups = new TreeMap<>(Comparator.nullsLast(order));
        groups.put(null, new ArrayList<>());
        for (Module module : moduleList) {
            groups.computeIfAbsent(module.getSemester(), semester -> new ArrayList<>()).add(module);
        }

        List<SemesterSummary> summaries = new ArrayList<>();
        for (Map.Entry<Semester, List<Module>> entry : groups.entrySet()) {
            List<Module> modules = entry.getValue();
            int totalCredits = 0;
            for (Module module : modules) {
                totalCredits += module.getCredit();
            }
            GpaData data = calculateGpaData(modules);
            OptionalDouble gpa = OptionalDouble.empty();
            if (entry.getKey() != null && data.totalCredits() > 0) {
                gpa = OptionalDouble.of(data.totalGradePoints() / data.totalCredits());
            }
            summaries.add(new SemesterSummary(entry.getKey(), modules.size(), totalCredits, gpa));
        }
        return summaries;
    }

    /**
     * Edits the module at the given 1-based index.
     * A null argument leaves the corresponding field unchanged.
     *
     * @param index     The 1-based index of the module to edit.
     * @param newCode   The new module code, or null to keep the current one.
     * @param newCredit The new credit value, or null to keep the current one.
     * @param newGrade  The new grade, or null to keep the current one.
     * @return The edited module.
     * @throws InvalidIndexException If the index is out of range.
     * @throws EditException         If no field is given, the credits are not from 1 to 40, the new code already
     *                               exists, or the new credits of an S/U-ed module do not fit in the S/U quota.
     */
    public Module editModule(int index, String newCode, Integer newCredit, Grade newGrade) {
        Module module = getModule(index);
        if (newCode == null && newCredit == null && newGrade == null) {
            throw new EditException("Provide at least one field to edit.");
        }
        if (newCredit != null && !Module.isValidCredit(newCredit)) {
            throw new EditException(Module.MESSAGE_INVALID_CREDITS);
        }
        if (newCode != null && !newCode.equalsIgnoreCase(module.getModuleCode())
                && hasModuleCode(newCode)) {
            throw new EditException("Module already exists: " + newCode);
        }
        if (newCredit != null && module.isSu()) {
            checkSuQuotaAllowsCredit(index, module, newCredit);
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

    /**
     * Checks that changing the credits of an S/U-ed module keeps the S/U quota from going below zero.
     * The module's current credits are given back to the quota before the new credits are checked.
     *
     * @param index     The 1-based index of the module, used in the error message.
     * @param module    The S/U-ed module whose credits will change.
     * @param newCredit The new number of credits.
     * @throws EditException If the new credits do not fit in the S/U quota.
     */
    private void checkSuQuotaAllowsCredit(int index, Module module, int newCredit) {
        assert module.isSu() : "Only S/U-ed modules use the S/U quota";
        int availableQuota = getRemainingSuQuota() + module.getCredit();
        if (newCredit > availableQuota) {
            logger.warning(() -> String.format(
                    "Rejected editing module %d (%s) to %d MCs: only %d MCs of S/U quota available",
                    index, module.getModuleCode(), newCredit, availableQuota));
            throw new EditException(String.format(
                    "Module %d is S/U-ed, and only %d MCs of S/U quota are available for it. "
                            + "Remove the S/U with unsu %d first, or use fewer credits.",
                    index, availableQuota, index));
        }
    }

    /**
     * Calculates the average grade point needed over the remaining MCs to reach a target GPA,
     * and returns a message describing the result.
     * Example: with CS2113 (4 MCs, A-) and MA1521 (4 MCs, B+), the current GPA is 4.25 over 8 MCs.
     *
     * @param target       The GPA that the student wants to reach, greater than 0 and at most 5.0.
     * @param remainingMcs The number of MCs that the student still has to complete.
     * @return The message describing the current GPA and the average grade point needed.
     * @throws TargetException If the target or the remaining MCs are out of range.
     * @throws GpaException    If no recorded module contributes to the GPA.
     */
    public String getTargetGpaMessage(double target, int remainingMcs) {
        if (target <= 0 || target > MAX_GRADE_POINT) {
            logger.warning("Rejected target GPA calculation: target GPA is out of range.");
            throw new TargetException("Target GPA must be greater than 0 and at most 5.0.");
        }
        if (remainingMcs <= 0) {
            logger.warning("Rejected target GPA calculation: remainingMcs is non-positive.");
            throw new TargetException("Remaining MCs must be positive.");
        }
        GpaData data = calculateGpaData(moduleList);
        if (data.totalCredits() == 0) {
            throw new GpaException(
                    "No modules contribute to GPA. Add a graded module first.");
        }
        int currentMcs = data.totalCredits();
        double currentPoints = data.totalGradePoints();
        String currentGpaDataMessage = String.format("Current GPA: %.2f over %d MCs.%n", calculateGpa(), currentMcs);
        // The MCs are added as doubles, so a very large number of remaining MCs cannot overflow an int.
        double totalMcs = (double) currentMcs + remainingMcs;
        double averageGradePointsRequired = (target * totalMcs - currentPoints) / remainingMcs;
        if (averageGradePointsRequired <= 0.0) {
            return currentGpaDataMessage +
                    "You already secured your target GPA!";
        }
        if (averageGradePointsRequired > MAX_GRADE_POINT) {
            double bestAchievableGpa = (currentPoints + MAX_GRADE_POINT * remainingMcs) / totalMcs;
            return currentGpaDataMessage +
                    String.format("Your target GPA is unreachable! The best you can reach is %.2f.", bestAchievableGpa);
        }
        String estimatedLetterGrade = Grade.estimateLetterGrade(averageGradePointsRequired);
        return currentGpaDataMessage +
                "To reach " + target +
                " after " + remainingMcs +
                " more MCs, you need an average grade point of " +
                String.format("%.2f", averageGradePointsRequired) + " (About " + estimatedLetterGrade + ")";
    }

    /**
     * Assigns the module at the given 1-based index to a semester.
     * If the module is already planned, it is moved to the new semester.
     *
     * @param index    The 1-based index of the module to plan.
     * @param semester The semester to plan the module for.
     * @return The planned module.
     * @throws InvalidIndexException If the index is out of range.
     */
    public Module planModule(int index, Semester semester) {
        assert semester != null : "Semester to plan should not be null";
        Module module = getModule(index);
        module.setSemester(semester);
        logger.info(() -> String.format("Planned module %d (%s) for %s",
                index, module.getModuleCode(), semester));
        return module;
    }

    /**
     * Removes the semester assignment from a module.
     *
     * @param index The 1-based module index.
     * @return The module with no semester assigned.
     * @throws InvalidIndexException If the index is out of range.
     * @throws PlanException         If the module is already unplanned.
     */
    public Module unplanModule(int index) {
        Module module = getModule(index);
        if (module.getSemester() == null) {
            logger.warning(() -> String.format(
                    "Rejected unplan on module %d (%s): not planned",
                    index, module.getModuleCode()));
            throw new PlanException(String.format(
                    "Module %d is not planned.", index));
        }
        module.setSemester(null);
        assert module.getSemester() == null : "Module should be unplanned after unplanModule";
        return module;
    }

    /**
     * Returns the 1-based indexes of the modules planned for the given semester.
     * The indexes are positions in the full module list, so they can be used with other commands.
     *
     * @param semester The semester to filter by.
     * @return The indexes of the matching modules, in list order.
     */
    public ArrayList<Integer> filterModule(Semester semester) {
        return filterIndexes(module -> semester.equals(module.getSemester()));
    }

    /**
     * Returns the 1-based indexes of the modules that are not planned for any semester.
     * The indexes are positions in the full module list, so they can be used with other commands.
     *
     * @return The indexes of the unplanned modules, in list order.
     */
    public ArrayList<Integer> filterUnplannedModules() {
        return filterIndexes(module -> module.getSemester() == null);
    }

    /**
     * Returns the 1-based indexes of the modules that satisfy the given condition.
     *
     * @param condition The condition a module must satisfy.
     * @return The indexes of the matching modules, in list order.
     */
    private ArrayList<Integer> filterIndexes(Predicate<Module> condition) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < moduleList.size(); i++) {
            if (condition.test(moduleList.get(i))) {
                indexes.add(i + 1);
            }
        }
        return indexes;
    }
}

