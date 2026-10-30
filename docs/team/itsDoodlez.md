# itsDoodlez's Project Portfolio Page

## Project: ChatGPAT

ChatGPAT is a desktop application designed to help university students manage their academic modules, track their grades, and plan their academic performance. It allows students to organize their modules, calculate their Grade Point Average (GPA), and set academic goals.

The application is written in Java and uses a Command Line Interface (CLI) for user interactions.

Given below are my contributions to the project.

* **New Feature**: Implemented the ability to view summary per semester.
    * **What it does**: Allows users to view each semester's module count, total credits, and credit-weighted GPA using the `summary` command. Semesters appear in chronological order, followed by the totals for unplanned modules.
    * **Justification**: Students need an overview of their workload and academic performance across semesters to identify gaps in their plans and track their progress.
    * **Highlights**: S/U modules and non-GPA grades count towards module and credit totals but are excluded from GPA calculations. Semesters without GPA-contributing credits display `GPA n/a`. The summary preserves the original module indexes and handles empty records. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)
    * **Design considerations**: Grouping and GPA calculations were implemented in the `Student` class, while `SummaryCommand` formats the output. A `SemesterSummary` record carries the results, and the existing GPA calculation helper was adapted for reuse across semester summaries, overall GPA, and target GPA calculations.

* **New Feature**: Added support for local storage.
    * **What it does**: Automatically saves module records to `data/modules.txt` after successful changes and restores them when the application starts. Saved records include module details, original grades, S/U status, and semester assignments.
    * **Justification**: Students should be able to continue managing their academic records across sessions without re-entering their modules.
    * **Highlights**: The implementation supports UTF-8 text, escaped special characters, and older records with missing optional fields. It skips malformed or duplicate records with warnings and reports file errors while keeping the application running. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)
    * **Design considerations**: File handling was separated into `Storage`, which returns data and warnings for the application and user interface to handle. Commands indicate data changes through `Response.forDataChange`, avoiding unnecessary saves for viewing or rejected commands. Saving writes a temporary file before replacing the destination, using an atomic move where supported.

* **New Feature**: Implemented exiting the program.
    * **What it does**: Allows users to enter `exit` to display `Goodbye!` and end the application without displaying another command prompt.
    * **Justification**: Students need a clear, predictable way to finish their session from the command line.
    * **Highlights**: The implementation connected `ExitCommand`, `CommandManager`, `Response`, and the main application loop. An exit flag in `Response` lets the application stop after the user interface displays the farewell message. [#26](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/26)

* **New Feature**: Added a help command.
    * **What it does**: Displays a command summary in the terminal, including command formats, descriptions, and an explanation of module indexes.
    * **Justification**: Students can refer to command syntax while using the application without leaving the terminal.
    * **Highlights**: Registered `HelpCommand` with `CommandManager` and returned the help text through the existing response system. Parsing accepts extra arguments after `help` while rejecting unrelated words such as `helpful`. [#26](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/26)

* **Code contributed**: [[RepoSense link](https://nus-cs2113-ay2627-s1.github.io/tp-dashboard/?search=itsDoodlez&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-09-18T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)]

* **Enhancements to existing features**:
    * Improved unknown-command errors by directing users to the `help` command. [#26](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/26)
    * Integrated automatic saving with the `add`, `edit`, `delete`, `su`, `unsu`, `plan`, and `unplan` commands. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)
    * Fixed the `Module` constructor to preserve the supplied S/U status when restoring saved records. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)


* **Testing**:
    * Wrote JUnit tests for semester summaries, local storage, help, and exit, including integration tests through `CommandManager` and the application loop. [#26](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/26) [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)
    * **Frontend (Command-level) tests**:
        * Tested parsing, extra arguments, invalid command keywords, help output, and exit responses. [#26](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/26)
        * Verified semester ordering, weighted GPA, S/U and non-GPA exclusions, empty records, and consistent decimal formatting across locales. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)
        * Tested automatic saving after each data-changing command, restoration across sessions, and continued operation after save failures. Verified that viewing and rejected commands leave the saved file unchanged. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)
    * **Backend (Model-level) tests**:
        * Tested saving and loading all module fields, preserving module order, Unicode text, escaped characters, and defaults for older records. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)
        * Covered malformed records, duplicate codes, missing or unreadable files, failed saves, temporary-file cleanup, and saving an empty module list. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)
        * Updated module-list tests to check actual `ListCommand` responses instead of a separate printing helper. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)

* **Documentation**:
    * **User Guide**:
        * Added command syntax, example output, and GPA rules for the `summary` feature. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)
        * Documented automatic saving, the local file format, optional fields, escaping, and handling of malformed records and file errors. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)
        * Expanded the `help` instructions and expected output, corrected the quick-start instructions to describe the terminal interface, and documented the exit message. [#26](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/26)
        * Added usage instructions for `unplan`, including automatic saving and error cases. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)
    * **Developer Guide**:
        * Added the persistence layer to the architecture description and documented storage validation, backward compatibility, startup loading, automatic saves, error handling, and testing. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)
        * Documented how to extend the saved-record format and updated the `unplan` section to describe its implementation and automatic-save response. [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)

* **Community**:
    * Merged a teammate's `unplan` implementation and resolved integration issues involving duplicate imports, comment formatting, and test expectations. [#42](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/42) [#45](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/45)

* **Tools**:
    * Used JUnit to implement automated tests.
    * Used Git and GitHub for version control and collaboration.
