package command;

import exception.UnloadedStorageException;
import manager.ManagementSystem;
import manager.Patient;
import miscellaneous.Ui;

/**
 * Represents a command to delete a patient from the system.
 */
public class DeletePatientCommand extends Command {
    protected String nric;

    /**
     * Constructs a DeletePatientCommand with the given NRIC.
     *
     * @param nric The NRIC of the patient to delete.
     */
    public DeletePatientCommand(String nric) {
        this.nric = nric;
    }

    /**
     * Executes the delete patient command.
     * Removes the patient with the specified NRIC from the system and shows confirmation via UI.
     *
     * @param manager The management system to update.
     * @param ui      The user interface to interact with the user.
     * @throws UnloadedStorageException If storage is not properly initialized.
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) throws UnloadedStorageException {
        Patient removedPatient = manager.deletePatient(nric);
        ui.showPatientDeleted(removedPatient, nric);
    }
}
