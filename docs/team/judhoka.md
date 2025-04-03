# Judha's Project Portfolio Page

## Project: ClinicEase

**ClinicEase** is a Command-Line Interface (CLI) based clinic management application designed to streamline the handling of patient records, appointment tracking, and medical history storage. The system simplifies daily administrative workflows while ensuring reliability and user-friendliness.

---

### Code Contributed

- **Code Dashboard Link**: [Click the link to see my code contribution](<iframe src="https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=judh&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code&since=2025-02-21" frameBorder="0" width="800px" height="142px"></iframe>)

## Features Implemented

### `add-patient`
- Registers a new patient using their NRIC, name, birthdate, gender, phone number, and address.
- Prevents duplicate entries by checking for existing NRICs.
- Example: `add-patient n/John Doe ic/S1234567A dob/01-01-1990 g/M p/98765432 a/123 Main St`

### `delete-patient`
- Deletes a patient's information from the system using their NRIC.
- Verifies the NRIC exists before deletion.
- Example: `delete-patient ic/S1234567A`

### `list-patient`
- Lists all current patients in a formatted and numbered list.
- Improves usability by including separators for readability.

---

## Command Parsing

- Implemented parsing logic for:
    - `add-patient`
    - `delete-patient`
    - `list-patient`
- Used consistent prefixes (`n/`, `ic/`, `dob/`, `g/`, `p/`, `a/`) for structured input.
- Added validation and exception handling for malformed or incomplete inputs.

---

## Persistent Storage

- Built the file storage system for:
    - Saving and loading patients
    - Saving and loading appointments
- Used serialization to ensure data persists across sessions.
- Automatically syncs after adding or deleting entries.

---

## Unit Testing

- Created JUnit test cases for:
    - `addPatient_Success`
    - `deletePatient_Success`
    - `addPatient_IncompleteDetails`
- Verified both valid and invalid inputs.
- Used `ByteArrayOutputStream` to capture and assert printed outputs.

---

## Exception Handling

- Ensured invalid input commands are safely rejected.
- Gave clear feedback to users for:
    - Duplicate NRICs
    - Deleting non-existent patients
    - Incomplete command inputs

---

## User Guide (UG)

- Designed and authored sections on:
    - Adding patients
    - Deleting patients
    - Listing patients
- Structured the UG to be browsable and beginner-friendly.
- Included command format, usage examples, and error notes.

---

## Developer Guide (DG)

- Documented feature-specific implementation details for patient commands.
- Wrote sequence diagrams for:
    - `add-patient`
    - `delete-patient`
- Helped standardize the Feature section format across the DG for consistency.

---

## Community Involvement

- Reviewed PRs related to appointment and patient logic.
- Provided feedback on command parsing patterns and error messaging.
- Collaborated with teammates on refining UG/DG formats.
- Ensured consistent coding style and naming conventions across related modules.
