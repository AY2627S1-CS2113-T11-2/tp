# ChatGPat

This is a greenfield Java project developed from the Duke project template.

ChatGPat is a desktop application for managing student’s grades, optimized for use through a Command Line Interface (CLI) while retaining the benefits of a Graphical User Interface (GUI). If you type quickly, ChatGPat can help you manage modules and GPA faster than traditional GUI applications.

Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25 (use the exact version), update Intellij to the most recent version.

1. **Ensure Intellij JDK 25 is defined as an SDK**, as described [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk) -- this step is not needed if you have used JDK 25 in a previous Intellij project.
1. **Import the project _as a Gradle project_**, as described [here](https://se-education.org/guides/tutorials/intellijImportGradleProject.html).
1. **Verify the setup**: After the importing is complete, locate the `src/main/java/seedu/chatgpat/ChatGpat.java` file, right-click it, and choose `Run ChatGpat.main()`. If the setup is correct, you should see something like the below:
   ```
   > Task :compileJava
   > Task :processResources NO-SOURCE
   > Task :classes
   
   > Task :ChatGpat.main()

     ██████╗██╗  ██╗ █████╗ ████████╗ ██████╗ ██████╗  █████╗ ████████╗
    ██╔════╝██║  ██║██╔══██╗╚══██╔══╝██╔════╝ ██╔══██╗██╔══██╗╚══██╔══╝
    ██║     ███████║███████║   ██║   ██║  ███╗██████╔╝███████║   ██║
    ██║     ██╔══██║██╔══██║   ██║   ██║   ██║██╔═══╝ ██╔══██║   ██║
    ╚██████╗██║  ██║██║  ██║   ██║   ╚██████╔╝██║     ██║  ██║   ██║
     ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝     ╚═╝  ╚═╝   ╚═╝

   How can I help you?
   ________________________________________________________________________

   >
   ```
   Type some word and press enter to let the execution proceed to the end.

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Build automation using Gradle

* This project uses Gradle for build automation and dependency management. It includes a basic build script as well (i.e. the `build.gradle` file).
* Refer to the [Gradle Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/gradle.html).

## Testing

### I/O redirection tests

* To run _I/O redirection_ tests (aka _Text UI tests_), navigate to the `text-ui-test` and run the `runtest(.bat/.sh)` script.

### JUnit tests

* JUnit tests are under `src/test/java`. Run them via Gradle.
* Refer to the [JUnit Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/junit.html).

## Checkstyle

* A Checkstyle configuration is provided in `config/checkstyle`.
* Refer to the [Checkstyle Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/checkstyle.html).

## CI using GitHub Actions

The project uses [GitHub actions](https://github.com/features/actions) for CI. When pushing a commit to this repo or PR against it, GitHub actions will run automatically to build and verify the code as updated by the commit/PR.

## Documentation

`/docs` folder contains the project documentation.
