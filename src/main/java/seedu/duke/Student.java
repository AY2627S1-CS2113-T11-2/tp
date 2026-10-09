package seedu.duke;

import java.util.ArrayList;

public class Student {
    private String name;
    private String id;
    private ArrayList<Module> moduleList = new ArrayList<Module>();
    private int suQuota;
    Student(String name, String id,int suQuota) {
        this.name = name;
        this.id = id;
        this.suQuota = suQuota;
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
    public void addingModule(Module module) {
        if(isModuleExists(moduleList,module.getModuleCode())) {
            System.out.println("Module already exists!");
            return;
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


}
