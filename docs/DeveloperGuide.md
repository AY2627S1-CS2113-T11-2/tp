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

This section describes the two parts of the skeleton that a new command must interact with: the main loop and the dispatch pipeline. The goal is to make clear what a developer must know before adding the next command.

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
