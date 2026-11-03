# Jerry's Project Portfolio Page

## Project: ChatGPAT

ChatGPAT is a desktop application designed to help university students manage their academic modules, track their grades, and plan their academic performance. It allows students to organize their modules, calculate their Grade Point Average (GPA), and set academic goals.

The application is written in Java and uses a Command Line Interface (CLI) for user interactions.

Given below are my contributions to the project.

* **New Feature**: Added the ability to add academic modules.
    * **What it does**: Allows users to add modules to their academic records, including module codes, credits, and grades.
    * **Justification**: This feature provides the foundation for academic record management, enabling users to track their modules and calculate their GPA.
    * **Highlights**: The implementation required integrating the `AddCommand` with the existing `Student` and `Module` classes. It also involved input validation and duplicate module detection to maintain data consistency.

* **New Feature**: Added the ability to list all academic modules.
    * **What it does**: Allows users to view all modules recorded in the application.
    * **Justification**: Students need a convenient way to review their academic records and monitor their progress.
    * **Highlights**: The implementation involved retrieving modules from the student model and displaying their information in a readable, indexed format.

* **New Feature**: Added a Target GPA calculator.
    * **What it does**: Allows users to specify a target GPA and calculate the average GPA required across their remaining credits to achieve that target.
    * **Justification**: Students often have academic goals but may find it difficult to determine the performance required to achieve them. This feature provides useful information for academic planning.
    * **Highlights**: This feature required implementing credit-weighted GPA calculations while accounting for modules excluded from GPA calculations, such as S/U modules. It also required handling cases where the target GPA is mathematically unattainable.
    * **Design considerations**: The GPA calculation logic was implemented in the `Student` class rather than directly in `TargetGpaCommand`, separating business logic from command processing. A shared calculation method was introduced to avoid duplicating GPA calculation logic.

* **New Feature**: Added the ability to filter modules by semester.
    * **What it does**: Allows users to display modules belonging to a specified academic year and semester using the `filter` command.
    * **Justification**: As students accumulate more modules, navigating their entire academic record becomes inconvenient. This feature allows users to focus on modules from a particular semester.
    * **Highlights**: The implementation required filtering modules based on their semester assignments without modifying the original module list. It also preserves the original module indexes, allowing users to reference filtered modules correctly in other commands.
    * **Design considerations**: The filtering logic was implemented in the `Student` class, while `FilterCommand` handles command parsing and response generation. This separation improves maintainability and testability.

* **Code contributed**: [[RepoSense link](https://nus-cs2113-ay2627-s1.github.io/tp-dashboard/?search=lcy-0976&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-09-18T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)]

* **Enhancements to existing features**:
    * Improved input validation and error handling for module-related commands.
    * Integrated module management features with the existing student data model.


* **Testing**:
    * Wrote JUnit tests for the `add`, `list`, `targetGPA`, and `filter` features. [#52](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/52) [#40](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/40)
    * **Frontend (Command-level) tests**:
        * Tested command parsing, execution, and expected responses for all four features. [#17](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/17)[#40](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/40)
        * Verified error handling for invalid inputs.
    * **Backend (Model-level) tests**:
        * Tested module addition, duplicate detection, and module listing. [#17](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/17)
        * Verified Target GPA calculations, including edge cases and unattainable targets. [#40](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/40)
        * Tested module filtering with multiple matches, no matches, empty records, and unplanned modules. [#47](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/47)

* **Documentation**:
    * **User Guide**:
        * Added documentation for the `targetGPA` feature, including command usage and examples. [#48](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/48)
        * Added documentation for the `filter` feature, including command syntax, parameters, and expected output. [#48](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/48)
    * **Developer Guide**:
        * Added implementation details for the `add` feature. [#41](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/41)
        * Added implementation details for the `list` feature. [#41](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/41)
        * Added implementation details and design considerations for the `targetGPA` feature. [#41](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/41)
        * Added implementation details and design considerations for the `filter` feature. [#41](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/41)
        * Created UML sequence diagrams to illustrate interactions between commands and the student model.[#41](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/41)

* **Community**:
    * Contributions to team discussions: [#17](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/17)

* **Tools**:
    * Used JUnit to implement automated tests. 
    * Used PlantUML to create UML diagrams for the Developer Guide. [#51](https://github.com/AY2627S1-CS2113-T11-2/tp/pull/51)
    * Used Git and GitHub for version control and collaboration.
