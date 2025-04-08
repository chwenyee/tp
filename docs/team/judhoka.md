# Judha's Project Portfolio Page

## Project: ClinicEase

**ClinicEase** is a CLI-based clinic management system that allows users to manage patient records, appointments, and prescriptions efficiently. The application streamlines administrative workflows in clinics by offering reliable, user-friendly, and persistent command-line interactions.

My main contribution in features was to design and implement patient related features (`add-patient`, `delete-patient`, etc...) to ensure that users can store patient data properly with their respective credentials. I also mainly contributed in creating the storage system for the entire system to ensure that data is properly kept and saved.

---

## Code Contributed

**Code Dashboard Link**: [Click here to see my code contribution](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=judhoka&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-02-21)

---

## Features Implemented

### [`add-patient`](../UserGuide.md/#adding-a-new-patient--add-patient)
- Allows users to register a new patient by specifying fields such as name, NRIC, date of birth, gender, phone number, and address.
- Includes validation to prevent duplicate NRIC entries, ensuring each patient is uniquely identified.
- Example:  
  `add-patient n/John Doe ic/S1234567A dob/1990-01-01 g/M p/98765432 a/123 Main St`

### [`delete-patient`](../UserGuide.md/#deleting-a-patient--delete-patient)
- Enables the removal of a patient using their NRIC.
- Validates whether the NRIC exists in the system before attempting deletion, and provides informative error messages if not found.
- Upon successful deletion, confirms the action and removes any associated appointments and prescription references.

### [`list-patient`](../UserGuide.md/#listing-all-patients-list-patient)
- Displays all patients currently stored in the system in a numbered and neatly formatted list.
- Includes name, NRIC, and other key attributes in a concise format.
- Improves usability with consistent dividers and spacing for readability.
- Helpful in providing an overview of all registered patients before performing operations like `delete-patient` or `view-patient`.

### [`view-patient`](../UserGuide.md/#viewing-patient-details-view-patient)
- Shows full details of a selected patient, including:
  - Name, NRIC, date of birth, gender, phone number, and address.
  - Optionally, linked appointments and prescriptions (if supported by system design).
- Supports lookup by NRIC (e.g., `view-patient ic/S1234567A`), with input validation and out-of-bounds handling.
- Designed to be clear and informative, acting as a one-stop overview of a patient's data.

---

## Parser Integration

- Developed parser logic for all implemented commands and storage system.
- Handled command recognition, parameter extraction, and error messaging.
- Validated argument formats and threw custom exceptions like `InvalidInputFormatException`.

---

## Storage System

- Designed and implemented the file-based persistent `Storage` class.
- Saved and loaded:
  - Patient data from `patient_data.txt`
  - Appointment data from `appointment_data.txt`
  - Prescription data from `prescription_data.txt`
- Linked appointments to patients during data load.
- Generated HTML versions of prescriptions for viewing and printing.
- Ensured initialization safety via `UnloadedStorageException`.

---

## Exception Handling

- Designed exception handling framework with custom messages.
- Threw descriptive exceptions when:
  - File paths were missing.
  - Data parsing failed.
  - Commands were malformed or incomplete.
- Used try-catch in the main loop to ensure graceful error handling.

---

## Testing

- Wrote JUnit test cases for parser logic, patient model, and storage.
- Threw various types of exceptions (e.g, `UnloadedStorageException`) if data writing fails, allowing the system to safely abort the operation.
- Manually tested combinations of valid and invalid inputs.

---

## User Guide (UG)

- Contributed to documentation for all implemented commands.
- Provided consistent format, expected outputs, and usage examples.
- Documented edge cases and failure scenarios for better UX.

---

## Developer Guide (DG)

- Authored complete component sections:
  - Architecture
  - `UI` component
  - `Storage` component
  - `Main` component
- Explained design considerations, structure, and responsibilities.
- Created and updated UML diagrams (Architecture, Class, and Sequence).
- Maintained consistency in formatting, phrasing, and layout across the DG.

---

## Community Involvement

- Reviewed teammate pull requests, particularly those related to parser, patient, appointment and storage system.
- Proposed improvements for error messaging and user experience.
- Ensured coding style consistency (naming, spacing, error format).
- Participated in team sync-ups to align features, naming conventions, and architecture decisions.

---
