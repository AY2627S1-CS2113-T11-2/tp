# User Guide
{:.no_toc}

ChatGPat is a command-line application for NUS students who keep track of their own grades, S/U options and study plan. If you type quickly, ChatGPat lets you record your modules, see your GPA, try out S/U decisions, work out the grades you need for a target GPA and plan your modules by semester, all with short typed commands instead of a spreadsheet.

{::options toc_levels="2..3" /}

* Table of Contents
{:toc}

## Quick start

1. Ensure that you have **Java 25** installed. To check, open a terminal and run `java -version`; the output should mention version 25.
2. Download the latest `chatgpat.jar` from the [releases page](https://github.com/AY2627S1-CS2113-T11-2/tp/releases).
3. Copy the file into an empty folder where you are allowed to create files, for example a new folder in your Documents. ChatGPat saves your data in this folder.
4. Open a terminal, `cd` into that folder, and run `java -jar chatgpat.jar`. Do not double-click the file, because ChatGPat needs a terminal to read your commands.
5. The ChatGPat banner and a command prompt (`>`) appear. Type a command and press Enter to run it. For example:
   * `add m/CS2113 c/4 g/A-` records CS2113, worth 4 MCs, with grade A-.
   * `list` shows all your modules.
   * `gpa` shows your GPA.
   * `exit` closes ChatGPat.
6. Refer to the [Features](#features) section below for details of each command.

## Features

### Notes about the command format

- Words in `UPPER_CASE` are the parameters to be supplied by you.
  - For example, in `delete INDEX`, replace `INDEX` with a number such as `2`.
- Items in square brackets are optional.
  - For example, `edit INDEX [m/MODULE_CODE] [c/CREDITS] [g/GRADE]` can be used as `edit 2 m/CS2113` or as `edit 2 c/4`.
- Parameters must be given **in the order shown**. For example, `add c/4 m/CS2113 g/A` is not accepted; type `add m/CS2113 c/4 g/A` instead.
- Command words are in lower case. `list` works, but `LIST` does not.
- `INDEX` is the number of a module in the list shown by `list`, starting from 1. After you delete a module, the modules below it move up by one, so run `list` again before using another index.
- Extra words after commands that take no parameters (`help`, `list`, `gpa`, `quota`, `summary` and `exit`) are ignored. For example, `help 123` is interpreted as `help`.
- If you type a command in the wrong format, ChatGPat shows how to type it, for example `Usage: delete INDEX`.
- If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines, as space characters surrounding line-breaks may be omitted when copied over to the application.

### Viewing help: `help`

Shows a summary of all the commands, then returns to the command prompt.

**Format:** `help`

**Expected output:**

```text
Available commands:
  help - Show this command summary.
  add m/MODULE_CODE c/CREDITS g/GRADE - Add a module to your records.
  list - List all recorded modules.
  edit INDEX [m/MODULE_CODE] [c/CREDITS] [g/GRADE] - Edit a module's details.
  delete INDEX - Delete a module.
  gpa - Show GPA, excluding SU-ed modules and non-GPA grades.
  summary - Show module counts, MCs, and GPA by semester, plus unplanned totals.
  su INDEX - Apply SU to a module and show the remaining SU quota.
  unsu INDEX - Remove SU from a module and show the remaining SU quota.
  quota - Show remaining SU quota and the modules you have SU-ed so far.
  target TARGET_GPA r/REMAINING_MCS - Show the average grade needed to reach a target GPA.
  plan INDEX y/YEAR s/SEM - Plan a module for a year and semester.
  unplan INDEX - Remove a module's semester assignment.
  filter y/YEAR s/SEM - Show the modules planned for a semester.
  filter unplanned - Show the modules not planned for any semester.
  exit - Exit ChatGPat.

INDEX is the module's position in your records, starting from 1.
```

### Adding a module: `add`

Adds a module to your records.

**Format:** `add m/MODULE_CODE c/CREDITS g/GRADE`

- `MODULE_CODE` is stored as typed and must not contain spaces. It is not checked against NUSMods, so that you can also record modules taken on student exchange (SEP) and codes that do not follow the usual NUS format. Two modules cannot have the same code; letter case is ignored, so `cs2113` and `CS2113` count as the same code.
- `CREDITS` is the module's Modular Credits (MCs), a whole number from 1 to 40. The limit is higher than most modules need, so that internships and attachments worth more MCs can also be recorded.
- `GRADE` is one of `A+`, `A`, `A-`, `B+`, `B`, `B-`, `C+`, `C`, `D+`, `D`, `F`, `CS` or `CU`. Letter case is ignored. `CS` and `CU` grades do not count towards your GPA.

**Examples:**

- `add m/CS2113 c/4 g/A-`
- `add m/MA1521 c/4 g/B+`

**Expected output** of `add m/CS2113 c/4 g/A-` (the last line repeats the module that was added):

```text
New module added: CS2113 (4 MCs, Grade: A-)
You now have 1 module recorded.
  CS2113 | 4 MCs | Grade: A-
```

**Errors:**

- A module with the same code shows `Module already exists: CS2113`.
- Credits outside 1 to 40 show `Credits must be a whole number from 1 to 40. Got: 41`.
- An unknown grade shows `Invalid grade: Z`.
- A missing or misplaced field shows `Usage: add m/MODULE_CODE c/CREDITS g/GRADE`.

### Listing all modules: `list`

Shows every module you have recorded, numbered for use with other commands such as `edit`, `delete`, `su` and `plan`.

**Format:** `list`

**Expected output:**

```text
Here are your recorded modules:
  1. CS2113 | 4 MCs | Grade: A- | Y2S1
  2. MA1521 | 4 MCs | Grade: B+
```

A module that is planned for a semester shows the semester at the end of its line, like `Y2S1` above. A module with S/U applied shows `[S/U]` after its grade. If you have no modules yet, ChatGPat shows `No modules recorded yet.`

### Deleting a module: `delete`

Removes a module from your records.

**Format:** `delete INDEX`

**Example:**

- `list` followed by `delete 3` deletes the 3rd module shown.

**Expected output:**

```text
Removed module 3:
  CS2040 | 4 MCs | Grade: A
```

**Errors:**

- An index that is not in your list shows `Invalid module index: 99. You currently have 3 module(s).`
- An index that is not a whole number shows `Usage: delete INDEX`.

### Editing a module: `edit`

Changes the code, credits and/or grade of an existing module. Its S/U status and semester stay the same.

**Format:** `edit INDEX [m/MODULE_CODE] [c/CREDITS] [g/GRADE]`

- At least one of the optional fields must be given.
- Only the fields you give are changed; everything else stays as it is.
- The same rules as in `add` apply: credits from 1 to 40, a known grade, and a code that no other module uses.
- If the module has S/U applied, its new credits must also fit in your S/U quota.
- If you give the same field more than once, for example `edit 1 g/A g/B`, the last one is used.

**Examples:**

- `edit 2 g/A` changes the grade of module 2 to A.
- `edit 1 c/5 g/B+` changes both the credits and the grade of module 1.

**Expected output** of `edit 2 g/A`:

```text
Updated module 2:
  MA1521 | 4 MCs | Grade: A
```

**Errors:**

- No field given shows `Provide at least one field to edit.`
- A code that another module already uses shows `Module already exists: MA1521`.
- New credits that do not fit in the S/U quota of an S/U-ed module show, for example, `Module 1 is S/U-ed, and only 28 MCs of S/U quota are available for it. Remove the S/U with unsu 1 first, or use fewer credits.`
- A missing or invalid index, or an unknown field, shows `Usage: edit INDEX [m/MODULE_CODE] [c/CREDITS] [g/GRADE]`.

### Planning a module for a semester: `plan`

Places a module in a year and semester of your study plan. Running the command again on the same module moves it to the new semester.

**Format:** `plan INDEX y/YEAR s/SEM`

- `YEAR` is your year of study, from 1 to 4.
- `SEM` is the semester, either 1 or 2.
- All three parts are required.

**Examples:**

- `plan 1 y/2 s/1` plans module 1 for Year 2 Semester 1.
- `plan 1 y/3 s/2` moves module 1 to Year 3 Semester 2.

**Expected output** of `plan 1 y/2 s/1`:

```text
Planned module 1 for Y2S1:
  CS2113 | 4 MCs | Grade: A- | Y2S1
```

**Errors:**

- A missing or wrongly formatted part shows `Usage: plan INDEX y/YEAR s/SEM`.
- A year outside 1 to 4 shows `Year must be between 1 and 4, there is an invalid year: 9`.
- A semester outside 1 to 2 shows `Semester must be 1 or 2, there is an invalid semester: 3`.

### Removing a module's semester: `unplan`

Removes a module from its semester. The module stays in your records; it just no longer has a semester.

**Format:** `unplan INDEX`

**Example:**

- `unplan 1` removes module 1 from its semester.

**Expected output:**

```text
Removed module 1 from its semester plan:
  CS2113 | 4 MCs | Grade: A-
```

If the module has no semester, ChatGPat shows `Module 1 is not planned.`

### Filtering modules: `filter`

Shows only some of your modules: either the modules planned for one semester, or the modules that are not planned yet. The numbers shown are the same as in `list`, so you can use them directly with commands such as `edit`, `delete`, `plan` and `unplan`. Filtering does not change your records.

**Formats:**

- `filter y/YEAR s/SEM` shows the modules planned for that semester. `YEAR` is from 1 to 4 and `SEM` is 1 or 2.
- `filter unplanned` shows the modules that have no semester.

**Examples:**

- `filter y/2 s/1` shows the modules planned for Year 2 Semester 1.
- `filter unplanned` shows the modules still to be planned. You can then plan one of them, for example with `plan 1 y/2 s/1`.

**Expected output** of `filter y/2 s/1`, if module 2 is planned for Y2S1:

```text
Modules in Y2S1:
2. MA1521 | 4 MCs | Grade: B+ | Y2S1
```

**Expected output** of `filter unplanned`, if only module 1 has no semester:

```text
Modules not planned for any semester:
1. CS2113 | 4 MCs | Grade: A-
```

**Other possible results:**

- No module in the semester: `No modules found in Y3S2.`
- Every module is planned: `All modules are planned.`
- No modules recorded: `No modules recorded yet.`
- An invalid year or semester shows the same errors as `plan`, and any other format shows `Usage: filter y/YEAR s/SEM, or filter unplanned`.

### Showing your GPA: `gpa`

Shows your GPA, calculated from the grades and credits of your modules on the NUS 5-point scale. Modules with S/U applied and modules graded `CS` or `CU` are left out.

**Format:** `gpa`

**Expected output**, with CS2113 (4 MCs, A-) and MA1521 (4 MCs, B+):

```text
Your GPA is 4.25
```

If no module counts towards your GPA, ChatGPat shows `No modules contribute to GPA. Add a graded module first.`

### Applying S/U on a module: `su`

Applies S/U on a module, so that it is left out of your GPA. Its grade is kept, so you can remove the S/U again later with `unsu`. You have an S/U quota of **28 MCs**; a module can only be S/U-ed if its credits fit in the remaining quota.

**Format:** `su INDEX`

**Example:**

- `list` followed by `su 2` applies S/U on module 2.

**Expected output:**

```text
Applied SU on module 2: MA1521 | 4 MCs | Grade: B+ [S/U]
Remaining SU quota: 24 MCs.
  MA1521 | 4 MCs | Grade: B+ [S/U]
```

**Errors:**

- A module that already has S/U applied shows `Module 2 is already SU-ed.`
- A module with more credits than your remaining quota shows `Insufficient SU quota. Remaining: 24 MCs, required: 30 MCs.`

### Removing S/U from a module: `unsu`

Removes S/U from a module, so that it counts towards your GPA again, and gives its credits back to your S/U quota.

**Format:** `unsu INDEX`

**Example:**

- `unsu 2` removes S/U from module 2.

**Expected output:**

```text
Removed SU on module 2: MA1521 | 4 MCs | Grade: B+
Remaining SU quota: 28 MCs.
  MA1521 | 4 MCs | Grade: B+
```

If the module does not have S/U applied, ChatGPat shows `Module 2 is already not SU-ed.`

### Showing your S/U quota: `quota`

Shows how many MCs of S/U quota you have left, and lists the modules that have S/U applied, with their numbers from `list`.

**Format:** `quota`

**Expected output:**

```text
Remaining SU quota: 24 MCs.
Modules SU-ed so far:
  2. MA1521 | 4 MCs | Grade: B+ [S/U]
```

If no module has S/U applied, the second line is `No modules have been SU-ed yet.`

### Calculating the grades needed for a target GPA: `target`

Works out the average grade point you need in your remaining MCs to reach the overall GPA you want. It uses the same modules as `gpa`, so modules with S/U applied and `CS`/`CU` grades are left out. This command does not change your records.

**Format:** `target TARGET_GPA r/REMAINING_MCS`

- `TARGET_GPA` is the GPA you want, greater than 0 and at most 5.0.
- `REMAINING_MCS` is the number of MCs you still have to take, a positive whole number.
- At least one of your modules must count towards your GPA.
- The result assumes that all your remaining MCs will count towards your GPA.

**Example:** with CS2113 (4 MCs, A-) and MA1521 (4 MCs, B+), your GPA is 4.25 over 8 MCs. To reach 4.5 after 8 more MCs:

```text
target 4.5 r/8
```

**Expected output:**

```text
Current GPA: 4.25 over 8 MCs.
To reach 4.5 after 8 more MCs, you need an average grade point of 4.75 (About A)
```

The letter grade in brackets is the lowest grade whose grade point is at least the average you need.

**Other possible results:**

- If you would reach the target even with the lowest grades: `You already secured your target GPA!`
- If you cannot reach it even with all A grades: `Your target GPA is unreachable! The best you can reach is 4.50.`
- A target outside the allowed range shows `Target GPA must be greater than 0 and at most 5.0.`
- A missing field or a value that is not a number shows the usage of `target`.

### Summarising your semesters: `summary`

Shows one line for each semester that has modules, in order of year and semester, followed by a line for the modules that are not planned.

**Format:** `summary`

- Each semester line shows the number of modules, the total MCs and the GPA of that semester, rounded to two decimal places.
- Modules with S/U applied and `CS`/`CU` grades count towards the number of modules and MCs, but not towards the GPA, just as with `gpa`. A semester with no such module shows `GPA n/a`.
- The `Unplanned` line shows the number of modules and MCs only. It is always shown, even when every module is planned.

**Expected output:**

```text
Y1S2 | 1 module | 4 MCs | GPA 5.00
Y2S1 | 1 module | 4 MCs | GPA 4.00
Unplanned | 1 module | 4 MCs
```

### Exiting the program: `exit`

Shows `Goodbye!` and closes ChatGPat. Your data has already been saved after each change, so nothing is lost.

**Format:** `exit`

Ending the input (pressing `Ctrl+D`, or `Ctrl+Z` and then Enter on Windows) also closes ChatGPat in the same way.

### Saving the data

ChatGPat saves your modules automatically to `data/modules.txt`, in the folder where you run it. There is no need to save manually.

- Successful `add`, `edit`, `delete`, `su`, `unsu`, `plan` and `unplan` commands save immediately. Commands that only show information, and commands that fail, do not change the file.
- The `data` folder and the file are created the first time you change your data. Deleting your last module saves an empty file.
- If saving fails, for example because the folder is write-protected, ChatGPat shows the reason. Your changes stay in memory: fix the folder access, then run a data-changing command to save again before you exit.

### Editing the data file

Advanced users can edit `data/modules.txt` directly. Exit ChatGPat first, and open the file in a text editor using **UTF-8** encoding. Each module is one line, with the fields in this order:

```text
code | MCs | grade | S/U flag | semester | name | description
```

For example (the file itself has no header):

```text
CS2113 | 4 | A- | 0 | Y2S1 | CS2113 |
MA1521 | 4 | B+ | 1 | - | MA1521 |
```

- Code, MCs and grade are required, with the same rules as in `add`: a code without spaces, MCs from 1 to 40, and a known grade.
- The S/U flag is `0` or `1`. The semester is `Y1S1` to `Y4S2`, or `-` if the module is not planned.
- Missing optional fields default to S/U `0`, not planned, a name equal to the code and an empty description, so shorter lines from older versions still load.
- Spaces around the `|` separators and blank lines are ignored. To use a special character inside a field, write `\|` for a pipe, `\\` for a backslash, `\n` for a new line, `\r` for a carriage return, `\t` for a tab, and `\s` for a space at the start or end of a field.

When ChatGPat starts, any line it cannot read is skipped, and a warning names the file, the line number and the reason. The other lines still load, and if two lines have the same code, the first one is kept. The next time ChatGPat saves, skipped lines are removed from the file, so fix them before you change your data.

## FAQ

**Q**: How do I move my data to another computer?<br>
**A**: Install ChatGPat on the other computer, then copy the `data/modules.txt` file from your old ChatGPat folder into the `data` folder of the new one.

**Q**: What is the `chatgpat.log` file in my ChatGPat folder?<br>
**A**: It records technical details, such as rejected commands, that help the developers find problems. You do not need to open it, and it is safe to delete when ChatGPat is not running.

**Q**: Can I change my S/U quota?<br>
**A**: Not yet. The quota is fixed at 28 MCs.

## Known issues

1. **The banner looks garbled.** The banner at startup uses special characters, which some terminals cannot show. This does not affect any command. Using a terminal that supports UTF-8, such as Windows Terminal, fixes it.

## Command summary

`INDEX` is the module number shown by `list`, starting from 1. Parts in square brackets are optional.

| Action | Format | Example |
|--------|--------|---------|
| Add a module | `add m/MODULE_CODE c/CREDITS g/GRADE` | `add m/CS2113 c/4 g/A-` |
| List all modules | `list` | `list` |
| Delete a module | `delete INDEX` | `delete 2` |
| Edit a module | `edit INDEX [m/MODULE_CODE] [c/CREDITS] [g/GRADE]` | `edit 2 c/4 g/A` |
| Plan a module for a semester | `plan INDEX y/YEAR s/SEM` | `plan 1 y/2 s/1` |
| Remove a module's semester | `unplan INDEX` | `unplan 1` |
| Filter modules by semester | `filter y/YEAR s/SEM` | `filter y/2 s/1` |
| Filter modules with no semester | `filter unplanned` | `filter unplanned` |
| Show your GPA | `gpa` | `gpa` |
| Apply S/U to a module | `su INDEX` | `su 2` |
| Remove S/U from a module | `unsu INDEX` | `unsu 2` |
| Show your S/U quota | `quota` | `quota` |
| Find the grades needed for a target GPA | `target TARGET_GPA r/REMAINING_MCS` | `target 4.5 r/8` |
| Summarise your semesters | `summary` | `summary` |
| Show the command list | `help` | `help` |
| Exit the program | `exit` | `exit` |
