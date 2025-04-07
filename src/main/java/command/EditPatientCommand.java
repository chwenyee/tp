package command;

import exception.InvalidInputFormatException;
import exception.PatientNotFoundException;
import exception.UnloadedStorageException;
import manager.ManagementSystem;
import miscellaneous.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditPatientCommand extends Command {

    // details: [0]=nric, [1]=name, [2]=dob, [3]=gender, [4]=address, [5]=phone
    private final String[] details;

    public EditPatientCommand(String[] details) {
        this.details = details;
    }

    /**
     * Executes the edit-patient command by validating the new date of birth (if present),
     * and then delegating to {@code manager.editPatient(...)} with the appropriate parameters.
     *
     * @param manager The ManagementSystem that manages patient data.
     * @param ui      The UI used to show error or success messages.
     * @throws UnloadedStorageException   If editing the patient data fails when saving to storage.
     * @throws PatientNotFoundException   If no patient with the specified NRIC is found.
     * @throws InvalidInputFormatException If the provided date is invalid or if newDob is after the current date.
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) throws UnloadedStorageException,
            PatientNotFoundException, InvalidInputFormatException {
        String nric = details[0];
        String name = details[1];
        String dob = details[2];
        String gender = details[3];
        String addr = details[4];
        String phone = details[5];

        // If dob is specified, we parse it here to confirm validity before calling editPatient
        if (dob != null && !dob.isBlank()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate.parse(dob, formatter);
            } catch (DateTimeParseException e) {
                ui.showError("Invalid date format for Date of Birth! Please use yyyy-MM-dd (e.g., 1990-05-12).");
                return;
            }
        }

        manager.editPatient(nric, name, dob, gender, addr, phone);
        Ui.showLine();
        System.out.println("Edit-patient command executed.");
        Ui.showLine();
    }
}
