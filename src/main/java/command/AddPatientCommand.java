package command;

import exception.DuplicatePatientIDException;
import exception.UnloadedStorageException;
import manager.ManagementSystem;
import manager.Patient;
import miscellaneous.Ui;

/**
 * Represents a command to add a patient to the system.
 */
public class AddPatientCommand extends Command {

    protected Patient patient;

    /**
     * Constructs an AddPatientCommand with the specified patient.
     *
     * @param patient The patient to be added.
     */
    public AddPatientCommand(Patient patient) {
        this.patient = patient;
    }

    /**
     * Executes the add patient command.
     * Adds the patient to the management system and shows confirmation via UI.
     *
     * @param manager The management system to update.
     * @param ui      The user interface to interact with the user.
     * @throws DuplicatePatientIDException If a patient with the same ID already exists.
     * @throws UnloadedStorageException    If the storage is not properly initialized.
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui)
            throws DuplicatePatientIDException, UnloadedStorageException {
        manager.addPatient(patient);
        ui.showPatientAdded(manager.getPatients());
    }
}
