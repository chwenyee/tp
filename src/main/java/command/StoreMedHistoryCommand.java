package command;

import exception.PatientNotFoundException;
import exception.UnloadedStorageException;
import manager.ManagementSystem;
import miscellaneous.Ui;


/**
 * Represents a command to store medical history for a specified patient.
 */
//@@author jyukuan
public class StoreMedHistoryCommand extends Command {
    /**
     * Patient's NRIC to which the medical history belongs.
     */
    private String nric;
    /**
     * The medical history (or histories) to store, typically in comma-separated format.
     */
    private String medHistory;

    /**
     * Constructs a StoreMedHistoryCommand with the necessary details.
     * The first element in the array is the patient's NRIC, the second is the medical history text.
     *
     * @param details String array containing [0] = NRIC, [1] = Medical history text.
     */
    public StoreMedHistoryCommand(String[] details) {
        this.nric = details[0];
        this.medHistory = details[1];
    }


    /**
     * Executes the command to store the given medical history for the specified patient.
     *
     * @param manager The ManagementSystem that manages patient and appointment data.
     * @param ui The Ui used to display success or error messages.
     * @throws UnloadedStorageException if saving to storage fails due to uninitialized storage.
     * @throws PatientNotFoundException if the patient with the given NRIC is not found.
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) throws UnloadedStorageException, PatientNotFoundException {
        manager.storeMedicalHistory(nric, medHistory);
    }
}
