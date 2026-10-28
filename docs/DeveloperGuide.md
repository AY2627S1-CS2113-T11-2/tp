# Developer Guide

## Acknowledgements

* ChatGpat is developed on the SE-EDU `duke` project template.
* Diagrams are written in [Mermaid](https://mermaid.js.org/) and rendered directly on GitHub.

## Design

### Architecture

The skeleton follows a layered design:

- **Entry point**: `ChatGpat` constructs the components and runs the main loop.
- **Presentation**: `ChatInterface` reads user input and prints responses.
- **Dispatch**: `CommandManager` parses user input into a `Command` and executes it.
- **Domain**: `Student` owns the module list; `Module` represents a single record.
- **Persistence**: `Storage` reads and writes local text records and returns diagnostics without printing.
- **Cross-cutting**: `Command` (abstract), `Response`, `Parser`, and the custom exceptions.

The diagram below shows the classes and their dependencies in the skeleton.

```mermaid
classDiagram
    class ChatGpat {
        +main(args)
        -run()
    }
    class ChatInterface {
        +printWelcome()
        +printResponse(Response)
        +printError(Exception)
        +getUserQuery() String
    }
    class CommandManager {
        +processQuery(String) Response
    }
    class Command {
        <<abstract>>
        +execute(Student) Response
    }
    class Response {
        +message() String
        +hasModule() boolean
    }
    class Parser {
        <<interface>>
        +parse(String) Optional~Command~
    }
    class Student {
        +getModule(int) Module
        +getModuleCount() int
    }
    class Module {
        -String moduleCode
        -int credit
        -Grade grade
        -boolean isSuApplied
    }

    ChatGpat --> "1" ChatInterface : ui
    ChatGpat --> "1" Student : student
    ChatGpat --> "1" CommandManager : commandManager
    CommandManager --> "1" Student : student
    CommandManager --> "0..*" Parser : parsers
    CommandManager ..> Command : executes
    Parser ..> Command : creates
    Command --> "1" Student : student
    Command ..> Response : returns
    Student --> "0..*" Module : moduleList
    ChatInterface ..> Response
```

The concrete `*Command` subclasses are omitted here. `Parser<Command>` is a functional interface, so each command provides a `parse` method reference that becomes one `Parser` in `CommandManager`.

**How the components interact.** When the user types a command, `ChatGpat` passes it to `CommandManager.processQuery`. `CommandManager` asks each `Parser` in turn; the first one that returns a `Command` wins. The command is executed with the `Student` object, and the returned `Response` is printed by `ChatInterface`. The loop repeats until the process is terminated.

The sequence diagram below shows one full iteration of the loop for `add m/CS2113 c/4 g/A-`.

```mermaid
sequenceDiagram
    participant Main as ChatGpat
    participant UI as ChatInterface
    participant CM as CommandManager
    participant P as Parser
    participant S as Student

    activate Main

    Main->>UI: printWelcome()
    activate UI
    UI-->>Main: banner printed
    deactivate UI

    loop until terminated
        Main->>UI: getUserQuery()
        activate UI
        UI-->>Main: "add m/CS2113 c/4 g/A-"
        deactivate UI

        Main->>CM: processQuery(query)
        activate CM

        CM->>P: parse(query)
        activate P
        create participant Cmd as AddCommand
        P->>Cmd:
        P-->>CM: Optional.of(Cmd)
        deactivate P

        CM->>Cmd: execute(student)
        activate Cmd

        create participant M as Module
        Cmd->>M: new Module(...)
        Cmd->>S: addModule(module)
        activate S
        S-->>Cmd: ok
        deactivate S

        Cmd-->>CM: Response
        deactivate Cmd

        CM-->>Main: Response
        deactivate CM

        Main->>UI: printResponse(response)
        activate UI
        UI-->>Main: response printed
    end

    deactivate Main
```

### UI component

### Logic component

### Model component

### Storage component

### Common classes

---

## Implementation

This section describes noteworthy details on how certain features are implemented.

### Add Module feature

#### Implementation

The add feature stores a new module in the student's record. The domain-side implementation is provided by `Module` and `Student#addModule(Module)`; command parsing is handled by `AddCommand` as shown in the architecture diagrams.

* `AddCommand` — interprets the user's module details and creates a `Module` before passing it to `Student`. The existing architecture illustrates `add m/CS2113 c/4 g/A-`; the uploaded files do not include `AddCommand`, so its complete parsing and validation rules cannot be confirmed here.
* `Module` — holds the module code, name, description, modular credits, grade and S/U status. `Grade#fromLabel(String)` converts a grade label (such as `A-`) to the corresponding `Grade` enum and rejects unknown labels.
* `Student#addModule(Module)` — checks for an existing module with the same code, ignoring letter case, using `isModuleExists`. If a duplicate exists, it logs a warning and throws `DuplicateModuleException`; otherwise, it appends the module to `moduleList`.

Given below is an example usage scenario.

Step 1. The user enters `add m/CS2113 c/4 g/A-` (as illustrated in the architecture section).

Step 2. The add command constructs a `Module` from the parsed details and invokes `Student#addModule(module)`.

Step 3. `Student` checks the module code for duplicates and adds the module to the list if none exists. The command then returns a `Response` for display by `ChatInterface`.

The following sequence diagram focuses on the domain-side add operation.

```mermaid
sequenceDiagram
    participant CM as :CommandManager
    participant AC as :AddCommand
    participant S as :Student
    participant M as :Module

    CM->>AC: execute(student)
    AC->>M: new Module(...)
    AC->>S: addModule(module)
    S->>S: isModuleExists(moduleList, moduleCode)
    alt module code already exists
        S-->>AC: throws DuplicateModuleException
    else module code is unique
        S->>S: moduleList.add(module)
        S-->>AC: completed
        AC-->>CM: Response
    end
```

**Note:** Duplicate checking is case-insensitive. The uploaded `Module` constructor currently initializes `isSuApplied` to `false` even though it accepts an `isSuApplied` parameter. The command's exact syntax beyond the architecture example should be checked against `AddCommand.java`.

### List All Modules feature

#### Implementation

The list feature displays the student's recorded modules in insertion order. In the uploaded domain code, it is facilitated by `Student#printModuleList()` and `Module#toString()`.

* `Student#printModuleList()` — prints `No modules recorded yet.` if the list is empty. Otherwise, it prints a heading and iterates through `moduleList`, numbering entries from 1.
* `Module#toString()` — formats each entry as `MODULE_CODE | CREDIT MCs | Grade: GRADE`, with ` [S/U]` appended if S/U has been applied.
* `Student#getModuleCount()` and `Student#getModule(int)` — provide the count and 1-based indexed access for other commands. `getModule` throws `InvalidIndexException` for an invalid index.

Given below is an example usage scenario.

Step 1. The student has added `CS2113` (4 MCs, A-) and `MA1521` (4 MCs, B+).

Step 2. The user enters `list`. The command dispatch layer selects the list operation (the exact command class is not included in the uploaded files).

Step 3. The displayed list is:

```text
Here are your recorded modules:
1. CS2113 | 4 MCs | Grade: A-
2. MA1521 | 4 MCs | Grade: B+
```

The following sequence diagram illustrates the existing `Student#printModuleList()` method. The command-to-UI integration cannot be verified without the list command source.

```mermaid
sequenceDiagram
    participant Caller as :List caller
    participant S as :Student
    participant M as :Module
    participant Console as :Console

    Caller->>S: printModuleList()
    alt moduleList is empty
        S->>Console: print "No modules recorded yet."
    else moduleList is not empty
        S->>Console: print heading
        loop for each module (index starts at 1)
            S->>M: toString()
            M-->>S: formatted module details
            S->>Console: print index and details
        end
    end
```

**Note:** Unlike the general architecture's response-based output design, the uploaded `printModuleList()` prints directly to `System.out`. If the actual list command uses `Response` instead, its implementation should be documented after inspecting that command.

#### Design considerations

**Aspect: How list output is produced**

* **Alternative 1 (current domain implementation):** `Student#printModuleList()` prints the records directly.
  * Pros: Simple implementation with straightforward numbering and formatting.
  * Cons: Couples domain logic to console output and makes output testing harder.
* **Alternative 2:** A list command builds a `Response` and delegates printing to `ChatInterface`.
  * Pros: Consistent with the layered architecture and easier to test without capturing console output.
  * Cons: Requires the command or response model to carry the formatted list.

### Target GPA feature

#### Implementation

The target GPA feature is facilitated by `TargetGpaCommand`, `Student#targetGPA(double, int)`, `Student#calculateGpaData()` and `Grade#estimateLetterGrade(double)`. It calculates the average grade point needed over the remaining modular credits to reach a desired cumulative GPA.

* `TargetGpaCommand#parse(String)` — matches `target\\s+(\\S+)\\s+r/(\\S+)`, converts the target to a `double` and the remaining MCs to an `int`, and creates a `TargetGpaCommand`. If the pattern does not match, it returns `Optional.empty()`. Numeric conversion errors can propagate as `NumberFormatException`.
* `TargetGpaCommand#execute(Student)` — calls `Student#targetGPA(target, remainingMcs)` and wraps the returned message in a `Response`.
* `Student#calculateGpaData()` — iterates through the recorded modules and returns a private `GpaData` record containing the total weighted grade points and GPA-contributing credits. S/U modules and grades such as `CS` and `CU` are excluded.
* `Student#targetGPA(double, int)` — checks that the target is greater than 0 and at most 5.0, and that the remaining MCs are positive. It throws `TargetException` for invalid values and `GpaException` when no recorded module contributes to GPA. It then calculates the required average and returns the appropriate message.
* `Grade#estimateLetterGrade(double)` — provides the approximate letter grade shown alongside a reachable required average.

Let `P` be the current weighted grade-point total, `C` the current GPA-contributing MCs, `F` the remaining MCs, and `T` the target GPA. The required future average `R` is:

\[
R = \frac{T(C+F)-P}{F}
\]

Given below is an example usage scenario.

Step 1. The student has recorded `CS2113` (4 MCs, A-) and `MA1521` (4 MCs, B+). The current GPA is 4.25 over 8 MCs, so `P = 34`.

Step 2. The user executes `target 4.50 r/8`. `TargetGpaCommand#parse` constructs a command with target 4.50 and 8 remaining MCs.

Step 3. `execute(student)` calls `Student#targetGPA(4.50, 8)`. The required average is `(4.50 × 16 − 34) / 8 = 4.75`. The returned `Response` includes the current GPA, required grade point and an approximate letter grade.

The following sequence diagram shows how `target 4.50 r/8` is processed.

```mermaid
sequenceDiagram
  participant CM as :CommandManager
  participant TC as TargetGpaCommand
  participant S as :Student
  participant M as :Module

  CM->>TC: parse("target 4.50 r/8")
  create participant T as :TargetGpaCommand
  TC->>T: new TargetGpaCommand(4.50, 8)
  TC-->>CM: Optional.of(command)

  CM->>T: execute(student)
  T->>S: targetGPA(4.50, 8)
  S->>S: validate target and remaining MCs
  S->>S: calculateGpaData()
  loop each recorded module
    S->>M: isSu(), getGrade(), getCredit()
    M-->>S: GPA contribution data
  end
  S->>S: calculate required average
  alt required average <= 0
    S-->>T: target already secured message
  else required average > 5
    S-->>T: unreachable target and best achievable GPA
  else required average within (0, 5]
    S->>S: Grade.estimateLetterGrade(required average)
    S-->>T: required average and estimated grade
  end
  T-->>CM: Response(message)
```

**Note:** `Student#targetGPA` handles three outcomes: if `R <= 0`, the target is already secured; if `R > 5`, it reports the best achievable GPA assuming 5.0 in every remaining MC; otherwise it reports `R` and an approximate letter grade. Invalid target values or remaining MCs are rejected before calculation. The current implementation uses `target <= 0` as its lower-bound check, although its exception text describes the interval as inclusive of 0.0.

#### Design considerations

**Aspect: Where the GPA calculation is performed**

* **Alternative 1 (current choice):** `Student#targetGPA` performs the calculation, while `TargetGpaCommand` handles parsing and creates the `Response`.
  * Pros: Keeps GPA business rules in the domain layer and makes the command straightforward to test.
  * Cons: Adds calculation and message-formatting responsibilities to `Student`.
* **Alternative 2:** Calculate the required average inside `TargetGpaCommand`.
  * Pros: Keeps the calculation close to its command.
  * Cons: Duplicates GPA-related logic and mixes command processing with domain rules.

**Aspect: How GPA totals are reused**

* **Alternative 1 (current choice):** Use a private `GpaData` record returned by `calculateGpaData()`.
  * Pros: Both current and target GPA calculations use the same credit-weighted totals and exclusions.
  * Cons: Requires an additional helper method and record.
* **Alternative 2:** Recompute totals separately in `calculateGpa()` and `targetGPA()`.
  * Pros: Fewer helper declarations.
  * Cons: Repeats iteration logic and risks inconsistent exclusions.

### Filter module list feature

#### Implementation

The filter feature is facilitated by `FilterCommand` (a subclass of `Command`) and `Student#filterModule(Semester)`. It displays modules assigned to a selected year and semester without changing the original module list.

* `FilterCommand#parse(String)` — matches `filter\s+y/(\d+)\s+s/(\d+)`, converts the captured year and semester to integers, and returns a `FilterCommand`. Non-matching input returns `Optional.empty()` so the next parser can be tried.
* `FilterCommand#execute(Student)` — constructs a `Semester` using the parsed values, calls `Student#filterModule(semester)`, and builds a `Response` containing the matching modules. If no indexes are returned, it displays `No modules found in Y2S1.` for the requested semester.
* `Student#filterModule(Semester)` — iterates through `moduleList`, compares each module's semester with the selected semester using `equals`, and returns an `ArrayList<Integer>` of matching **1-based indexes**. It does not remove or reorder any modules.
* `Student#getModule(int)` — retrieves each matching module by its original index so `FilterCommand` can display its `toString()` representation.

Given below is an example usage scenario.

Step 1. The user has five modules recorded. Modules 2 and 5 are planned for `Y2S1`.

Step 2. The user executes `filter y/2 s/1`. `FilterCommand#parse` returns a command holding year 2 and semester 1.

Step 3. `execute(student)` constructs `Semester(2, 1)` and calls `Student#filterModule(semester)`, which returns `[2, 5]`. The command retrieves and displays those two modules using their original indexes.

```text
Modules in Y2S1:
2. MA1521 | 4 MCs | Grade: B+ | Y2S1
5. CS2113 | 4 MCs | Grade: A- | Y2S1
```

The following sequence diagram shows how `filter y/2 s/1` is processed.

```mermaid
sequenceDiagram
  participant CM as :CommandManager
  participant FC as FilterCommand
  participant F as :FilterCommand
  participant S as :Student

  CM->>FC: parse("filter y/2 s/1")
  FC->>F: new FilterCommand(2, 1)
  FC-->>CM: Optional.of(command)
  CM->>F: execute(student)
  F->>F: new Semester(2, 1)
  F->>S: filterModule(semester)
  loop for each module in moduleList
    S->>S: compare module.getSemester().equals(semester)
  end
  S-->>F: [2, 5]
  loop for each matching index
    F->>S: getModule(index)
    S-->>F: module
  end
  F-->>CM: Response(message)
```

**Note:** The current `Student#filterModule` calls `Objects.equals(module.getSemester(), semester)` now. Even if an unplanned module is encountered, this function will not `NullPointerException` and it can still compare successfully. However,the current command requires both `y/YEAR` and `s/SEM`; filtering unplanned modules is not implemented. The `Semester` constructor handles year/semester range validation. Filtering is read-only and does not request a storage save.

#### Design considerations

**Aspect: How filtered modules are identified**

* **Alternative 1 (current choice):** Return the original 1-based indexes from `Student#filterModule`.
  * Pros: Indexes remain consistent with `list`, `delete`, `edit` and `plan`; no separate filtered index mapping is needed.
  * Cons: `FilterCommand` must call `getModule` for each matching index, and the displayed numbers may have gaps.
* **Alternative 2:** Return a new list of matching `Module` objects and number them consecutively.
  * Pros: Simple to render the filtered list.
  * Cons: Renumbering would be misleading because other commands use indexes from the full list.

**Aspect: How semester matching is performed**

* **Alternative 1 (current choice):** Compare each module's `Semester` with the requested `Semester` using `equals`.
  * Pros: Reuses the `Semester` value object's equality and keeps the filtering logic short.
  * Cons: Requires a null check for unplanned modules; the current implementation does not have one.
* **Alternative 2:** Group modules by semester in a map and look up the selected semester.
  * Pros: Convenient if repeated filtering and other semester-based queries become common.
  * Cons: Adds extra bookkeeping and complexity for a simple read-only command.

### Index-based module commands

`delete`, `edit`, `plan` and `unplan` all act on one module, chosen by its position in the list shown by `list` (`INDEX`, starting from 1). They share the same flow:

1. `CommandManager` asks each `Parser<Command>` in turn. The `parse` method of the matching command checks the input with regular expressions and returns a `Command`, or an empty `Optional` if the input is not meant for it.
2. `Command#execute(Student)` asks `Student` to change the data, then returns a `Response`. By design, commands do not print: `ChatInterface` prints every `Response`.
3. `Student` looks the module up with `Student#getModule(int)`, which throws `InvalidIndexException` when the index is out of range. Expected errors such as this are `ChatGpatException`s: `ChatGpat#run` catches them and prints the message as a normal response. Any other unexpected exception is caught by a second `catch` block, which prints a generic error and logs the details. Either way the program keeps running, and the commands above validate their input before changing any data.

### Delete feature

#### Implementation

The delete feature is facilitated by `DeleteCommand` (a subclass of `Command`) and `Student#deleteModule(int)`.

* `DeleteCommand#parse(String)` — matches `delete\s+(\d+)` and returns a `DeleteCommand` holding the index, or `Optional.empty()` so that the next parser is tried.
* `Student#deleteModule(int)` — looks the module up with `getModule`, removes it from `moduleList`, and returns the removed module.
* `DeleteCommand#execute(Student)` — calls `deleteModule` and returns a `Response` containing the message and the removed module, so `ChatInterface` can show what was deleted.

Given below is an example usage scenario.

Step 1. The user has three modules recorded and runs `list` to see their numbers.

Step 2. The user executes `delete 2`. `CommandManager` tries each parser until `DeleteCommand#parse` returns a `DeleteCommand` for index 2.

Step 3. `CommandManager` calls `execute(student)` on it. `Student#deleteModule(2)` removes the second module and returns it, and the command returns the response `Removed module 2:` with the deleted module.

The following sequence diagram shows how the `delete 2` command is processed.

```mermaid
sequenceDiagram
  participant CM as :CommandManager
  participant DC as DeleteCommand
  participant S as :Student

  CM->>DC: parse("delete 2")
  create participant D as :DeleteCommand
  DC->>D: new DeleteCommand(2)
  DC-->>CM: Optional.of(command)

  CM->>D: execute(student)
  D->>S: deleteModule(2)
  S->>S: getModule(2)
  S-->>D: removed module
  D-->>CM: Response("Removed module 2:", module)
```

**Note:** If the index is out of range, `deleteModule` throws `InvalidIndexException` before anything is removed, so no data changes. Because the modules after the deleted one move up by one, the user should run `list` again before using another index.

#### Design considerations

**Aspect: How the user picks the module to delete**

* **Alternative 1 (current choice):** By the `INDEX` shown in `list`.
    * Pros: Short to type, and the same for `edit`, `su`, `unsu`, `plan` and `unplan`.
    * Cons: Indexes shift after a delete, so the user must `list` again.
* **Alternative 2:** By module code.
    * Pros: Stable even after other modules are removed.
    * Cons: Longer to type, and typos or letter-case differences need extra handling.

**Aspect: Who reports the result**

* **Alternative 1 (current choice):** `Student` returns the removed module and `DeleteCommand` builds the `Response`.
    * Pros: The output goes through `ChatInterface` only, and `Student` can be tested without capturing console output.
    * Cons: Slightly more code in each command.
* **Alternative 2:** `Student` prints the result itself.
    * Pros: Less code.
    * Cons: Output handling is spread across classes, which is harder to test and breaks the layering.

### Edit feature

#### Implementation

The edit feature is facilitated by `EditCommand`, `Student#editModule(int, String, Integer, Grade)` and the setters in `Module`.

* `EditCommand#parse(String)` — claims the input if it is `edit` or starts with `edit ` (so that a malformed edit gets a helpful message instead of "Unknown command"). It then matches `edit\s+(\d+)` followed by any number of `m/`, `c/` and `g/` fields, converts the credits to a number and the grade to a `Grade`, and builds an `EditCommand`. A field the user did not give is `null`, meaning "keep the current value". A malformed command, a non-numeric credit or an unknown grade throws `EditException`.
* `Student#editModule(...)` — validates everything first, then applies the given fields through `Module#setModuleCode`, `setCredit` and `setGrade`, and returns the edited module.
* `EditCommand#execute(Student)` — calls `editModule` and returns a `Response` with the message and the updated module.

Given below is an example usage scenario.

Step 1. The user runs `list` and sees `2. MA1521 | 4 MCs | Grade: B+`.

Step 2. The user executes `edit 2 c/5 g/A`. `EditCommand#parse` builds a command with index 2, no new code, credits 5 and grade A.

Step 3. `execute` calls `Student#editModule(2, null, 5, A)`. All checks pass, so only the credits and grade are changed. The response shows `MA1521 | 5 MCs | Grade: A`.

The following sequence diagram shows how the `edit 2 c/5 g/A` command is processed.

```mermaid
sequenceDiagram
  participant CM as :CommandManager
  participant EC as EditCommand
  participant S as :Student
  participant M as :Module

  CM->>EC: parse("edit 2 c/5 g/A")
  Note over EC: throws EditException if the input is malformed
  create participant E as :EditCommand
  EC->>E: new EditCommand(2, null, 5, A)
  EC-->>CM: Optional.of(command)

  CM->>E: execute(student)
  E->>S: editModule(2, null, 5, A)
  alt all checks pass
    S->>M: setCredit(5)
    S->>M: setGrade(A)
    S-->>E: edited module
    E-->>CM: Response("Updated module 2:", module)
  else a check fails
    S-->>E: throws InvalidIndexException or EditException
    E-->>CM: exception propagates up
  end
```

Similarly, how `Student#editModule` works inside the `Domain` layer is shown below.

```mermaid
sequenceDiagram
    participant E as :EditCommand
    box LightCoral Domain
        participant S as :Student
        participant M as :Module
    end

    E->>+S: editModule(2, null, 5, A)
    S->>S: getModule(2)
    S->>S: validate the given fields
    S->>+M: setCredit(5)
    M-->>-S: ok
    S->>+M: setGrade(A)
    M-->>-S: ok
    S-->>-E: edited module
```

The following activity diagram shows the checks that `editModule` makes before it changes anything.

```mermaid
flowchart TD
  A([editModule<br/>called]) --> B{index<br/>in range?}
  B -- no --> X1[throw<br/>InvalidIndex<br/>Exception]
  B -- yes --> C{at least one<br/>field given?}
  C -- no --> X2[throw<br/>EditException]
  C -- yes --> D{credits<br/>positive?}
  D -- no --> X2
  D -- yes --> E{new code used<br/>by another<br/>module?}
  E -- yes --> X2
  E -- no --> F[apply the<br/>given fields]
  F --> G([return<br/>edited module])
```

**Note:** All checks happen before any setter is called, so a rejected edit never leaves a module half-edited.

#### Design considerations

**Aspect: How the fields are updated**

* **Alternative 1 (current choice):** Change the existing `Module` through setters, with `null` meaning "keep the current value".
    * Pros: Other data on the module, such as the S/U flag and the semester, is kept automatically, and one command handles any combination of fields.
    * Cons: `Module` becomes mutable, and the meaning of `null` must be documented.
* **Alternative 2:** Create a new `Module` with the changed fields and replace the old one.
    * Pros: `Module` can stay immutable.
    * Cons: Every field must be copied across, so a new field such as the semester is easy to lose.

**Aspect: When the input is validated**

* **Alternative 1 (current choice):** Validate every field first, then apply them.
    * Pros: A failed edit never leaves a half-edited module.
    * Cons: The checks are separated from the code that applies each field.
* **Alternative 2:** Validate and apply each field one at a time.
    * Pros: Each field's check sits next to its update.
    * Cons: A later failure can leave earlier fields already changed.

### [Proposed] Plan feature

#### Proposed Implementation

The proposed plan feature lets the user place a module in a year and semester. It is facilitated by the new `Semester` record, a new optional `semester` field in `Module`, `Student#planModule(int, Semester)` and `PlanCommand`.

```mermaid
classDiagram
  class Student {
    +getModule(int) Module
    +planModule(int, Semester) Module
  }
  class Module {
    -String moduleCode
    -int credit
    -boolean isSuApplied
    +getSemester() Semester
    +setSemester(Semester)
  }
  class Semester {
    <<record>>
    +year() int
    +term() int
    +toString() String
  }
  class Grade {
    <<enumeration>>
  }

  Student --> "0..*" Module : moduleList
  Module --> "1" Grade : grade
  Module --> "0..1" Semester : semester
```

* `Semester` — an immutable record of a year (1 to 4) and a semester number (1 or 2). Its constructor throws `PlanException` for out-of-range values, so an invalid `Semester` cannot exist. `toString` gives the form `Y2S1`.
* `Module#semester` — `null` means the module is not planned yet. `Module#toString` appends ` | Y2S1` only when it is set, so planned modules show their semester in `list`, `add`, `edit` and `su` without any change to those commands.
* `PlanCommand#parse(String)` — claims the input if it is `plan` or starts with `plan `. It matches `plan INDEX y/YEAR s/SEM`, converts the numbers (reporting non-numbers as a `PlanException`), and builds the `Semester`. A malformed command throws a `PlanException` with the usage message.
* `Student#planModule(int, Semester)` — gets the module with `getModule` and sets its semester. If the module is already planned, it is simply moved to the new semester.

Given below is an example usage scenario.

Step 1. The user has added `CS2113`, which is module 1 and is not planned.

Step 2. The user executes `plan 1 y/2 s/1`. `PlanCommand#parse` creates `Semester(2, 1)`, then a `PlanCommand` for index 1.

Step 3. `execute` calls `Student#planModule(1, semester)`, which sets the module's semester. The response is `Planned module 1 for Y2S1:` followed by the module line, which now ends in `| Y2S1`.

The following sequence diagram shows how the `plan 1 y/2 s/1` command goes through the `Dispatch` and `Domain` layers when it succeeds.

```mermaid
sequenceDiagram
  participant Main as :ChatGpat
  box LightBlue Dispatch
    participant CM as :CommandManager
  end
  participant PC as PlanCommand
  participant P as :PlanCommand
  box LightCoral Domain
    participant S as :Student
    participant M as :Module
  end

  Main->>+CM: processQuery("plan 1 y/2 s/1")
  CM->>+PC: parse("plan 1 y/2 s/1")
  PC->>P: new PlanCommand(1, semester)
  PC-->>-CM: Optional.of(command)
  CM->>+P: execute(student)
  P->>+S: planModule(1, semester)
  S->>S: getModule(1)
  S->>+M: setSemester(semester)
  M-->>-S: ok
  S-->>-P: module
  P-->>-CM: Response
  CM-->>-Main: Response
```

**Note:** The `Semester` object is created inside `PlanCommand#parse` and is not shown, to keep the diagram simple. If the index is out of range, `getModule` throws `InvalidIndexException`. If the year or semester is invalid, the `Semester` constructor throws `PlanException`. In both cases nothing is changed.

The following object diagram shows the effect of `plan 1 y/2 s/1` on the first module.

```mermaid
flowchart LR
    subgraph Before["Before plan 1 y/2 s/1"]
        direction TB
        S0[":Student"] --> M0["m1:Module<br/>CS2113<br/>semester = null"]
    end
    subgraph After["After plan 1 y/2 s/1"]
        direction TB
        S1[":Student"] --> M1["m1:Module<br/>CS2113"]
        M1 --> Sem["s:Semester<br/>year = 2<br/>term = 1"]
    end
    Before --> After
```

#### Design considerations

**Aspect: How a semester is represented**

* **Alternative 1 (current choice):** A separate `Semester` record that validates itself.
    * Pros: An invalid year or semester cannot exist, `equals` comes for free (useful for later filtering and summaries), and the `Y2S1` format is defined in one place.
    * Cons: One more class.
* **Alternative 2:** Two plain `int` fields on `Module`.
    * Pros: No new class.
    * Cons: Validation and formatting are repeated wherever they are used, and the year can be set without the semester.

**Aspect: Representing "not planned"**

* **Alternative 1 (current choice):** A `null` semester.
    * Pros: Simple, existing modules and tests are unaffected, and saved data from before the feature can be loaded as unplanned.
    * Cons: Callers must check for `null`.
* **Alternative 2:** `Optional<Semester>` or a special "unplanned" constant.
    * Pros: Makes the unplanned case explicit.
    * Cons: More code, and `Optional` is generally discouraged as a field type.

**Aspect: Where the command lives**

* **Alternative 1 (current choice):** A separate `plan` command.
    * Pros: `edit` stays unchanged and the semester is set in one place.
    * Cons: One more command to learn.
* **Alternative 2:** Add optional `y/` and `s/` fields to `edit`.
    * Pros: Fewer commands.
    * Cons: `edit`'s validation gets more tangled, and the year and semester only make sense together.

### Unplan feature

#### Implementation

The unplan feature removes a module's semester again. It is implemented by `UnplanCommand` and `Student#unplanModule(int)`, and reuses `Module#setSemester`.

* `UnplanCommand#parse(String)` — matches `unplan\s+(\d+)` and returns an `UnplanCommand`. It does not clash with `PlanCommand`, because that parser only claims input that is `plan` or starts with `plan `.
* `Student#unplanModule(int)` — gets the module with `getModule`. If its semester is `null`, it throws a `PlanException` saying the module is not planned. Otherwise it calls `setSemester(null)` and returns the module.
* `UnplanCommand#execute(Student)` — returns `Response.forDataChange` with the message and module, requesting an automatic save.

The following sequence diagram shows how `unplan 1` is processed.

```mermaid
sequenceDiagram
  participant CM as :CommandManager
  participant UC as UnplanCommand
  participant S as :Student
  participant M as :Module

  CM->>UC: parse("unplan 1")
  create participant U as :UnplanCommand
  UC->>U: new UnplanCommand(1)
  UC-->>CM: Optional.of(command)

  CM->>U: execute(student)
  U->>S: unplanModule(1)
  S->>S: getModule(1)
  alt module has a semester
    S->>M: setSemester(null)
    S-->>U: unplanned module
    U-->>CM: Response("Removed module 1 from its semester plan:", module)
  else module has no semester
    S-->>U: throws PlanException
    U-->>CM: exception propagates up
  end
```

**Note:** If the index is out of range, `getModule` throws `InvalidIndexException`, and nothing is changed.

#### Design considerations

**Aspect: Unplanning a module that has no semester**

* **Alternative 1 (current choice):** Report an error.
    * Pros: The user learns that the command had no effect, which usually means a wrong index.
    * Cons: The command is not idempotent.
* **Alternative 2:** Succeed silently.
    * Pros: Safe to repeat.
    * Cons: A mistaken index goes unnoticed.

**Aspect: How the user clears a semester**

* **Alternative 1 (current choice):** A separate `unplan` command, paired with `plan` like `su` and `unsu`.
    * Pros: Consistent with the existing command pairs, and easy to remember.
    * Cons: One more command.
* **Alternative 2:** A special value such as `plan INDEX y/0 s/0`.
    * Pros: No new command.
    * Cons: An unclear special case that makes `Semester` validation harder.

### Local storage

`Storage` owns the UTF-8 file at `data/modules.txt`, relative to the process working directory. Its constructor also accepts a `Path` so tests can use JUnit's `@TempDir`. No file is created by loading a missing path.

The file format has no header and stores one module per physical line. These positions are the persistence contract for future features:

| Position | Field | Validation / default |
| --- | --- | --- |
| 1 | Module code | Required, non-empty, no whitespace; case-insensitive uniqueness |
| 2 | MCs | Required positive Java `int` |
| 3 | Original grade | Required `Grade` label, including `CS` and `CU` |
| 4 | S/U flag | `0` or `1`; omitted/empty defaults to `0` |
| 5 | Semester | `Y1S1` through `Y4S2`, or `-`; omitted/empty is unplanned |
| 6 | Module name | Omitted/empty defaults to the code |
| 7 | Module description | Omitted/empty defaults to an empty string |

Fields are separated by unescaped `|`, with optional surrounding spaces. The writer emits all seven fields. The reader accepts three to seven fields, ignores blank lines, and tolerates a UTF-8 byte order mark. Names and descriptions are saved even though `add` currently supplies the code as the name and an empty description.

Escapes are `\|`, `\\`, `\n`, `\r`, `\t`, and `\s` (literal space). The reader trims separator padding before decoding. The writer escapes boundary spaces so they survive a round trip. Unknown or incomplete escapes invalidate only that line. Extra fields currently invalidate a line, helping catch accidental unescaped pipes.

**Extending the format:** Append new optional fields; do not reorder or reuse these positions. Update `Storage`'s parser, writer and field-count limit together, give each added field a default for shorter records, update this table, and add a round-trip test. Backward compatibility means new releases read old records; it does not promise that old releases read new fields.

**Startup:** `ChatGpat.run` calls `Storage.load()`, adds the returned modules to the student, then passes `LoadResult.warnings()` to `ChatInterface.printWelcome`. Validation and duplicate detection happen per line, so a bad record cannot stop later records loading. Read errors return warnings. Storage neither prints nor logs to the console.

**Commands:** Successful mutating commands return `Response.forDataChange(message, module)`. `ChatGpat` checks `isDataChanged()`, saves `student.getModules()`, and passes any save error to `ChatInterface` alongside the command response. Viewing commands use the ordinary response constructors; failed commands throw before returning a save request. Future commands that modify persisted data must use `forDataChange` too.

**Saving:** The writer creates parent directories as needed, writes a sibling temporary file, then replaces the destination atomically where supported (falling back to a normal replacement where atomic moves are unavailable). This avoids truncating the existing file during serialization or writing. I/O and access errors return a message instead of escaping the command loop. Temporary-file cleanup is attempted on success and failure. The in-memory state is retained after a save failure; the next successful mutating command retries the full list.

**Verification:** `StorageTest` covers a real save/load round trip, escaped text, legacy defaults, malformed lines and duplicates, missing/unreadable files, and failed saves. `ChatGpatTest` verifies that each of the seven mutating commands saves before its response, sessions restore saved state, viewing/rejected commands do not rewrite the file, and save errors reach the UI while commands continue working.

## Product scope
### Target user profile

{Describe the target user profile}

### Value proposition

{Describe the value proposition: what problem does it solve?}

## User Stories

|Version| As a ... | I want to ... | So that I can ...|
|--------|----------|---------------|------------------|
|v1.0|new user|see usage instructions|refer to them when I forget how to use the application|
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
