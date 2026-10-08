# User Guide

ChatGPat is a desktop application for managing student’s grades, optimized for use through a Command Line Interface (CLI) while retaining the benefits of a Graphical User Interface (GUI). If you type quickly, ChatGPat can help you manage modules and GPA faster than traditional GUI applications.

## Quick start

1. Ensure that Java 25 or later is installed on your computer.
2. **Mac users:** Ensure you have the precise JDK version prescribed here.
3. Download the latest `.jar` file from here.
4. Copy the file to the folder you want to use as the home folder for your ChatGPat.
5. Open a terminal, `cd` to the folder containing the JAR file, and run `java -jar chatgpat.jar`.
6. A GUI similar to the one below should appear in a few seconds. Note how the app contains some sample data.
7. Type a command in the command box and press Enter to execute it. For example, type `help` and press Enter to open the help window.
8. Refer to the [Features](#features) section below for details of each command.

## Features

### Notes about the command format

- Words in `UPPER_CASE` are the parameters to be supplied by the user.
  - For example, in `delete INDEX`, replace `INDEX` with a value such as `2`.
- Items in square brackets are optional.
  - For example, `edit INDEX [m/MODULE_CODE] [c/CREDITS] [g/GRADE]` can be used as `edit 2 m/CS2113` or as `edit 2 c/4`.
- Extraneous parameters for commands that take no parameters, such as `help`, `list`, and `exit`, are ignored.
  - For example, `help 123` is interpreted as `help`.
- If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines, as space characters surrounding line-breaks may be omitted when copied over to the application.

### Viewing help: `help`

Shows a summary of all available commands.

**Format:** `help`

### Adding a module: `add`

Add a module to your records.

**Format:** `add m/MODULE_CODE c/CREDITS g/GRADE`

- `MODULE_CODE` is stored as typed — it is not validated against NUSMods or any external module list.
- `CREDITS` is the module's Modular Credit (MC) weight. Must be a positive integer.

**Examples:**

```text
add m/CS2113 c/4 g/A-
add m/MA1521 c/4 g/B+
```

**Expected output:**

```text
New module added: CS2113 (4 MCs, Grade: A-)
You now have 1 module recorded.
```

### Listing all modules: `list`

Shows every module currently recorded, numbered for use with other commands (`edit`, `delete`, `su`, `unsu`).

**Format:** `list`

**Expected output:**

```text
Here are your recorded modules:
1. CS2113 | 4 MCs | Grade: A-
2. MA1521 | 4 MCs | Grade: B+
```

### Deleting a module: `delete`

Remove a module from your records.

**Format:** `delete INDEX`

**Examples:**

- `list` followed by `delete 2` — deletes the 2nd module shown.

**Expected output:**

```text
Removed module: MA1521 (4 MCs, Grade: A)
You now have 1 module recorded.
```

### Editing a module: `edit`

Edits an existing module's code, credits, and/or grade.

**Format:** `edit INDEX [m/MODULE_CODE] [c/CREDITS] [g/GRADE]`

- At least one optional field must be provided.
- Only the field(s) provided are changed; everything else stays as-is.

**Examples:**

- `edit 2 g/A` — changes the grade of module 2 to A.
- `edit 1 c/5 g/B+` — changes both credits and grade of module 1.

**Expected output:**

```text
Module 2 updated: MA1521 | 4 MCs | Grade: A
```

### Showing total GPA: `gpa`

Shows the student’s calculated GPA. Excluded the SU-ed modules.

**Format:** `gpa`

### Applying SU on a module: `su`

Applies SU on a module, excludes it from the GPA calculation. Shows the remaining SU quota.

**Format:** `su INDEX`

**Examples:**

- `list` followed by `su 2` — applies SU on module 2.

### Removing SU on a module: `unsu`

Removes SU on a module, includes it back in the GPA calculation. Shows the remaining SU quota.

**Format:** `unsu INDEX`

**Examples:**

- `list` followed by `unsu 3` — removes SU on module 3.

### Showing remaining SU quota: `quota`

Shows the remaining SU quota. Lists out all the current SU-ed modules.

**Format:** `quota`

### Exiting the program: `exit`

Exits ChatGPat.

**Format:** `exit`
