# Zhi Hou - Project Portfolio Page

## Overview

ChatGPAT is a desktop application designed to help university students manage their academic modules, track their grades, and plan their academic performance. It allows students to organize their modules, calculate their Grade Point Average (GPA), and set academic goals.
The application is written in Java and uses a Command Line Interface (CLI) for user interactions.

Given below are my contributions to the project.

* **New Feature**: Added the ability to delete a module, `delete INDEX` ([#19](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/19), [#25](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/25)).
    * What it does: removes the module at the given position in the list shown by `list`.
    * Justification: users add modules by mistake or stop tracking a module, and need a quick way to remove it.
    * Highlights: the index is validated before any data changes, so a bad command never leaves the list half-updated. Errors are `ChatGpatException` subclasses shown as normal responses instead of crashing the program.

* **New Feature**: Added the ability to edit a module, `edit INDEX [m/MODULE_CODE] [c/CREDITS] [g/GRADE]` ([#20](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/20)).
    * What it does: changes any combination of a module's code, credits and grade.
    * Justification: users often mistype a grade or credit value and should not have to delete and re-add the module.
    * Highlights: the fields can be given in any combination, with a usage message if none are given, which made the parser more involved than the other index-based commands. Problems are reported with a new `EditException`.

* **New Feature**: Added the ability to plan and unplan modules, `plan INDEX y/YEAR s/SEM` and `unplan INDEX` ([#38](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/38), [#42](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/42)).
    * What it does: assigns a module to a semester, or removes it from its semester again.
    * Justification: students plan their modules across four years of study, so the app should record where each module belongs.
    * Highlights: I added the `Semester` value type, and a module's semester is optional, so unplanned modules are handled throughout the app. Year (1 to 4) and semester (1 to 2) are range-checked and rejected with a new `PlanException`.

* **New Feature**: Added the ability to list unplanned modules, `filter unplanned` ([#50](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/50)).
    * What it does: lists the modules not planned for any semester yet, without changing the list.
    * Justification: it shows students at a glance which modules still need a semester.
    * Highlights: matching modules keep their **original** index from `list`, so the numbers work directly with `delete`, `edit` and `plan`. Renumbering the results would make follow-up commands act on the wrong module. It uses a predicate-based helper in `Student`, so other filters can reuse it.


* **Code contributed**: [RepoSense link](https://nus-cs2113-ay2627-s1.github.io/tp-dashboard/?search=nathanng-boop&breakdown=true)


* **Project management**:
  * Published the [v2.0 release](https://github.com/AY2627S1-CS2113-T11-2/tp/releases/tag/v2.0) with its JAR file.
  * Merged teammates' PRs into `master` (for example [#47](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/47), [#51](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/51), [#58](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/58)) and resolved Developer Guide merge conflicts.

* **Enhancements to existing features**:
    * Updated the `help` command to list every command and its format, and updated `CommandManagerTest` to match ([#56](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/56)).
    * Refactored `FilterCommand` so both filter commands share one result-formatting method ([#50](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/50)).
    * Wrote JUnit tests for all my commands, in the same PRs as the features.

* **Documentation**:
    * User Guide: `plan` ([#38](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/38)), `unplan` ([#42](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/42)), `filter unplanned` and the command summary table ([#50](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/50)).
    * Developer Guide:
        * Implementation sections for `delete`, `edit`, `plan`, `unplan` ([#39](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/39)), `gpa` and `quota` ([#62](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/62)) and a revamp of the whole `filter` section ([#50](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/50)).
        * Product scope, User Stories, Non-Functional Requirements, Glossary, Instructions for manual testing, and UML versions of the delete, edit, plan and unplan diagrams ([#57](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/57)).
        * Design section: Architecture overview and the UI, Logic, Model, Storage and Common components ([#59](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/59)).
        * UML diagrams (PlantUML): `ArchitectureDiagram`, `ArchitectureSequence`, `UiClassDiagram`, `LogicClassDiagram`, `ModelClassDiagram`, `CommonClassDiagram`, `DeleteSequence`, `EditSequence`, `EditDomainSequence`, `EditActivity`, `PlanClassDiagram`, `PlanSequence`, `PlanObjectBefore`, `PlanObjectAfter`, `UnplanSequence`, `FilterClassDiagram`, `FilterSequence`, `FilterUnplannedSequence`.

* **Community**:
    * PRs reviewed: [#47](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/47), [#40](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/40)

* **Tools**:
    * Set up the PlantUML workflow for the team: sources in `docs/diagrams`, a shared `style.puml`, and PNG exports in `docs/images`, because GitHub Pages does not render Mermaid.

## Contributions to the Developer Guide (Extracts)

Given below are short extracts from my part of the Developer Guide. See the full guide for the other features.

### Filter unplanned feature

`filter unplanned` lists the modules that have no semester. `FilterUnplannedCommand` asks `Student` for the 1-based indexes of the modules whose semester is not set, then fetches each module with `Student#getModule(int)` so that the original numbering is kept. If every module is planned, it returns "All modules are planned." instead.

<img src="../images/FilterUnplannedSequence.png" alt="Sequence diagram of filter unplanned" width="600"/>

### Plan feature

`plan INDEX y/YEAR s/SEM` stores a `Semester` in the chosen module. The object diagrams show the state before and after `plan 2 y/2 s/1`.

<img src="../images/PlanObjectBefore.png" alt="Object diagram before plan" width="330"/>
<img src="../images/PlanObjectAfter.png" alt="Object diagram after plan" width="330"/>
