# User Guide

ChatGPat is a desktop application for managing student grades, optimized for use through a Command Line Interface (CLI). If you type quickly, ChatGPat can help you manage modules and GPA faster than traditional GUI applications.

## Quick start

1. Ensure that Java 25 or later is installed on your computer.
2. **Mac users:** Ensure you have the precise JDK version prescribed here.
3. Download the latest `.jar` file from here.
4. Copy the file to the folder you want to use as the home folder for your ChatGPat.
5. Open a terminal, `cd` to the folder containing the JAR file, and run `java -jar chatgpat.jar`.
6. The ChatGPat banner and a command prompt (`>`) should appear in the terminal.
7. Type a command at the prompt and press Enter to execute it. For example, type `help` and press Enter to display the command summary in the terminal.
8. Refer to the [Features](#features) section below for details of each command.

## Features

### Saving and editing local data

ChatGPat loads your records from `data/modules.txt` when it starts. This path is relative to the folder where you run the program. There is no database. A missing file starts an empty session, and the folder and file are created on the first successful data-changing command.

Successful `add`, `edit`, `delete`, `su`, `unsu`, `plan`, and `unplan` commands save immediately. Viewing commands and rejected commands do not rewrite the file. Deleting the last module saves an empty file.

To edit records yourself, exit ChatGPat and open the file in a text editor using **UTF-8** encoding. Each module occupies one line, in this field order:

```text
code | MCs | grade | S/U flag | semester | name | description
```

For example (the file itself has no header):

```text
CS2113 | 4 | A- | 0 | Y2S1 | CS2113 |
MA1521 | 4 | B+ | 1 | - | MA1521 |
```

- Code, MCs and grade are required. Codes must be non-empty without whitespace, MCs must be positive integers, and grades must be recognised by ChatGPat.
- S/U is `0` or `1`; the original letter grade is retained even when S/U is applied.
- Semester is `Y1S1` through `Y4S2`, or `-` for unplanned.
- Missing or empty optional fields default to S/U `0`, unplanned semester, name equal to the code, and an empty description. Your older three-, four- and five-field records still load.
- Spaces around separators and blank lines are ignored. For literal special characters inside a field, use `\|` for a pipe, `\\` for a backslash, `\n` for a newline, `\r` for a carriage return, `\t` for a tab, and `\s` for a space that must be preserved at a field's edge.

Malformed lines are skipped with a warning naming the file, line number and reason. Loading continues with the other lines; duplicate codes are treated case-insensitively and the first valid record wins. Loading does not change your file. The next successful save writes the loaded records and any changes, so skipped lines will no longer be in the file.

Read and save errors appear in the terminal without closing the program. If saving fails, your changes remain in memory: fix the file or folder access, then run a data-changing command to retry before exiting.

### Notes about the command format

- Words in `UPPER_CASE` are the parameters to be supplied by the user.
  - For example, in `delete INDEX`, replace `INDEX` with a value such as `2`.
- Items in square brackets are optional.
  - For example, `edit INDEX [m/MODULE_CODE] [c/CREDITS] [g/GRADE]` can be used as `edit 2 m/CS2113` or as `edit 2 c/4`.
- Extraneous parameters for commands that take no parameters, such as `help`, `list`, and `exit`, are ignored.
  - For example, `help 123` is interpreted as `help`.
- If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines, as space characters surrounding line-breaks may be omitted when copied over to the application.

### Viewing help: `help`

Shows a summary of all currently available commands in the terminal, then returns to the command prompt.

**Format:** `help`

**Expected output:**

```text
Available commands:
  help     - Show this command summary.
  gpa      - Show GPA, excluding SU-ed modules and non-GPA grades.
  summary  - Show module counts, MCs, and GPA by semester, plus unplanned totals.
  su INDEX - Apply SU to a module and show the remaining SU quota.
  unplan INDEX - Remove a module's semester assignment.
  exit     - Exit ChatGPat.

INDEX is the module's position in your records, starting from 1.
```

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
Modules planned with `plan` also show their semester at the end of the line, for example `1. CS2113 | 4 MCs | Grade: A- | Y2S1`.

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

### Planning a module for a semester: `plan`

Assigns a module to a year and semester so you can organise your modules into a study plan. Running the command again on the same module moves it to the new semester.

**Format:** `plan INDEX y/YEAR s/SEM`

- `INDEX` is the module's number in `list`, starting from 1.
- `YEAR` is your year of study, from 1 to 4.
- `SEM` is the semester, either 1 or 2.
- All three parts are required.

**Examples:**

- `plan 1 y/2 s/1` — plans module 1 for Year 2 Semester 1.
- `plan 1 y/3 s/2` — moves module 1 to Year 3 Semester 2.

**Expected output:**

```text
Planned module 1 for Y2S1:
  CS2113 | 4 MCs | Grade: A- | Y2S1
```

**Errors:**

- A missing or wrongly formatted part shows `Usage: plan INDEX y/YEAR s/SEM`.
- A year outside 1 to 4 shows `Year must be between 1 and 4, there is an invalid year: 9`.
- A semester outside 1 to 2 shows `Semester must be between 1 and 2, there is an invalid year: 3`.
- An index that isn't in your list shows an invalid module index message.

### Removing a semester plan: `unplan`

Removes a module's semester assignment while retaining its code, credits, grade and S/U status. The change is saved immediately.

**Format:** `unplan INDEX`

For example, `unplan 1` removes module 1 from its semester plan. An invalid index or an already unplanned module shows an error and does not change your records.

### Summarising semesters: `summary`

Shows one line per semester containing modules, sorted by year and then semester, followed by an `Unplanned` line.

**Format:** `summary`

- Each semester line shows its module count, total MCs, and credit-weighted GPA rounded to two decimal places.
- SU-ed modules and non-GPA grades (`CS` and `CU`) count towards module and MC totals but are excluded from GPA, just as with `gpa`.
- A semester with no GPA-contributing credits shows `GPA n/a`.
- The `Unplanned` line shows module and MC totals without a GPA. It is always included, even when there are no unplanned modules.
- If no modules are recorded, the output is `Unplanned | 0 modules | 0 MCs`.

**Example output:**

```text
Y1S1 | 3 modules | 18 MCs | GPA 4.35
Y2S1 | 2 modules | 8 MCs | GPA 4.25
Unplanned | 1 module | 4 MCs
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

Prints `Goodbye!` and exits ChatGPat.

**Format:** `exit`
