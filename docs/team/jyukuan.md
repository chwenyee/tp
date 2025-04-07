# Project Portfolio Page (PPP)

## Overview

**ClinicEase** is a CLI-based clinic management application that enables users to handle patient records, medical
histories, and appointments quickly and effectively. It provides commands for adding, editing, and viewing patient
details, as well as storing and updating their medical histories. This project streamlines a clinic’s administrative
tasks in a single, user-friendly CLI system.

I have been primarily involved in designing and implementing the **patient’s history** functionality to ensure users can
store, view, and edit patients’ medical histories with robust validation and persistence.

---

## Summary of Contributions

### 1. Code Contributed

- **Code Dashboard Link
  **: [Click here to see my code contribution](<iframe src="https://nus-cs2113-ay2425s2.github.io/tp-dashboard/#/widget/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-02-21&tabOpen=true&tabType=authorship&tabAuthor=jyukuan&tabRepo=AY2425S2-CS2113-T11b-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false&chartGroupIndex=20&chartIndex=3" frameBorder="0" width="800px" height="142px"></iframe>)

### 2. Enhancements Implemented

1. **Medical History Commands**
    - **`store-history`**:
        - Allows users to add new medical history entries to an existing patient’s record.
        - Accepts multiple comma-separated entries for one command (e.g., `h/Diabetes, Hypertension`).
        - Performs checks to ensure the patient exists, preventing invalid input from updating records.
    - **`view-history`**:
        - Supports viewing a patient’s medical history by either NRIC (e.g., `view-history ic/S1234567A`) or by name (
          e.g., `view-history John Doe`).
        - Displays all stored entries for the patient(s) found.
    - **`edit-history`**:
        - Enables replacing the first occurrence of a specified old history entry with a new one.
        - Uses `equalsIgnoreCase()` matching, providing leniency in text formatting.

2. **Parser Integration**
    - Implemented specialized parsing logic (`parseStoreHistory`, `parseViewHistory`, `parseEditHistory`) to handle the
      format for new commands.
    - Ensured error messages (e.g., `InvalidInputFormatException`) provide clarity when commands are malformed.

3. **Exception Handling**
    - Enhanced user feedback for invalid commands or missing parameters.
    - Threw `UnloadedStorageException` if data writing fails, allowing the system to safely abort the operation.

4. **Java Doc**
    - Provided code reviews on pull requests that impacted the data structures for storing patient details and medical
      histories.
    - Authored detailed JavaDoc for classes and methods dealing with patient history (`store-history`, `view-history`,
      `edit-history` commands, and related `ManagementSystem` methods). This documentation highlights parameters, return
      values, and exceptions, making the code easier to read and maintain.
    - Helped maintain consistent Java coding conventions (e.g., method naming, usage of final variables) throughout the
      codebase.

5. **Review / Mentoring Contributions**

    - Provided code reviews on pull requests that impacted the data structures storing patient details.
    - Helped maintain consistent Java coding conventions (e.g., method naming, usage of final variables) throughout the
      codebase.

### 3. Contributions to the User Guide  (UG)

- **Sections**:
    - [Adding Medical History: `store-history`](#)
    - [Viewing Medical History: `view-history`](#)
    - [Editing Medical History: `edit-history`](#)

  I wrote the instructions, formats, and examples for these commands. I also included **tips** about commas for multiple
  entries and clarifications on the usage of NRIC vs. name.

### 4. Contributions to the Developer Guide (DG)

1. **Testing Patient Management Features**
    - Authored the guidelines and scenarios on how to manually test and validate patient-related operations.
    - Added use cases and step-by-step instructions to ensure testers can verify correctness for storing, viewing, and
      editing patient history.

2. **Storing Medical History Feature**
    - Wrote the detailed explanation of how `store-history`, `view-history`, and `edit-history` commands work
      internally.
    - Added or updated sequence diagrams in PlantUML to illustrate the flow of data and command execution for medical
      history.

3. **UML Diagrams**
    - Created the **Sequence Diagrams** for the `store-history` feature, demonstrating how the command interacts with
      `Parser`, `ManagementSystem`, and `Storage`.
    - Ensured diagrams accurately reflect the final implementation (parameter extraction, validation steps, and
      file-saving routines).
    - Created the **Class Diagrams** for Command Component.
    - Ensured diagrams accurately reflect the final implementation.

### 5. Contributions to Team-Based Tasks

- Participated in **team discussions** to decide on command naming patterns (`store-history` vs. `add-history`) and the approach to handle multiple comma-separated entries.
- Ensured consistent **exception messages** across the project for invalid data entries.
- Reviewed and tested merges from teammates, particularly those that affected `ManagementSystem` or `Parser`.

---

