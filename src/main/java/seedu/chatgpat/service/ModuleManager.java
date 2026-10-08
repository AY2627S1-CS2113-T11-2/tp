package seedu.chatgpat.service;

import java.util.ArrayList;
import java.util.List;

import seedu.chatgpat.module.Module;

/**
 * Manages the modification and viewing of the module list.
 */
public class ModuleManager {
    private List<Module> modules;

    /**
     * Constructs a new ModuleManager.
     */
    public ModuleManager() {
        modules = new ArrayList<Module>();
    }
}
