# Dylan's Project Portfolio Page

## Project: ClinicEase

**ClinicEase** is a CLI-based clinic management application that allows users to efficiently manage patient records, medical histories, and appointments. The system provides commands for viewing and updating patient details, managing appointments, and maintaining medical history records. This project aims to simplify clinic administrative tasks with a user-friendly interface.

Given below are my contributions to the project.

---

### New Feature:
**`view-patient`**:
- Allows users to view a patient's personal details via their NRIC (e.g., `view-patient ic/S1234567A`).
- Displays the patient's full name, contact details, and other personal information.
- Ensures that the provided NRIC matches an existing patient before retrieving the data.

### New Feature:

**`find-appointment`**:
- Finds and lists all appointments for a patient using their NRIC (e.g., `find-appointment ic/S1234567A`).
- Retrieves a list of appointments with the specified patient.

### New Feature:
**`mark-appointment`**:
- Allows users to mark an appointment as completed for a specific patient using their NRIC and the appointment ID (e.g., `mark-appointment ic/S1234567A id/001`).
- Verifies that the patient exists and that the appointment ID corresponds to an existing appointment before updating the status.

### New Feature:
**`unmark-appointment`**:
- Unmarks a completed appointment, changing its status as uncompleted (e.g., `unmark-appointment ic/S1234567A id/001`).

---

### Code Contributed:

**Code Dashboard Link**: [Click here](<https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-02-21T00%3A00%3A00&tabOpen=true&tabType=authorship&tabAuthor=dylancmznus&tabRepo=AY2425S2-CS2113-T11b-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false>)

---

### Enhancements Implemented:

**Parser Integration**
    - Implemented some parsing logic (`parseViewPatient`, `parseMarkAppointment`, `parseUnmarkAppointment`, `parseFindAppointment`) to handle new command formats.
    - Ensured error messages provide clarity for invalid commands or incorrect command parameters (e.g., `InvalidInputFormatException`).

**Persistence and Data Integrity**
    - Enhanced the `ManagementSystem` to:
        - Verify the existence of the patient and appointments before marking or unmarking.
        - Ensure that changes to appointment statuses are reflected persistently in the system.

**Exception Handling**
    - Improved user feedback for invalid commands or missing parameters, particularly for appointment management.

---

### User Guide (UG)

- **Sections**:
    - [Viewing Patient Information: `view-patient`](#)
    - [Marking Appointments: `mark-appointment`](#)
    - [Unmarking Appointments: `unmark-appointment`](#)
    - [Finding Appointments: `find-appointment`](#)

  I authored the instructions, formats, and examples for these commands, providing clarification on valid NRIC formats and how to use appointment IDs for marking and unmarking.

---

### Developer Guide (DG)

1. **Use Cases for Essential and Additional Features**
    - Authored the use cases for essential commands like:
        - `add-patient`
        - `delete-patient`
        - `add-appointment`
        - `edit-patient`
        - `sort-appointment`
    - Provided detailed step-by-step scenarios for users to understand how these features should be used to interact with the system.

2. **Implementation and UML Diagrams**
    - Created **Sequence Diagrams** for the `view-patient` feature, demonstrating how the command interacts with the `Parser`, `ManagementSystem`, and `Storage` components.
    - Updated UML diagrams to reflect the data flow and validation steps involved in retrieving patient information using NRIC.

---

### Community Involvement

- Conducted code reviews for pull requests affecting appointment management, particularly focusing on ensuring that appointments are validated correctly and updates are persisted in storage.
- Assisted teammates in resolving command parsing issues and clarified how appointments are managed and retrieved in the system.
- Ensured adherence to Java coding conventions across the codebase, especially in method naming and parameter validation.
- Suggested improvements for better user experience, such as providing clearer feedback on appointment status updates.
- Contributed ideas for expanding the appointment feature, such as introducing appointment reminders or notifications.

---
