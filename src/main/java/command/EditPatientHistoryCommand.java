package command;

import exception.UnloadedStorageException;
import manager.ManagementSystem;
import miscellaneous.Ui;

/**
 * Represents a command to edit existing medical history entries for a patient.
 */
public class EditPatientHistoryCommand extends Command {

    /**
     * String array containing:
     * [0] = Patient NRIC
     * [1] = Old history text to be replaced
     * [2] = New history text to replace with
     */
    private final String[] details;

    /**
     * Constructs an EditPatientHistoryCommand with the specified details.
     *
     * @param details Contains patient NRIC, old history entry, and new history entry.
     */
    public EditPatientHistoryCommand(String[] details) {
        this.details = details;
    }

    /**
     * Executes the command to edit the medical history of a specified patient.
     *
     * @param manager The ManagementSystem managing patient data and operations.
     * @param ui      The Ui used to display output to the user.
     * @throws UnloadedStorageException if saving changes to storage fails.
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) throws UnloadedStorageException {
        String nric = details[0];
        String oldHistory = details[1];
        String newHistory = details[2];

        manager.editPatientHistory(nric, oldHistory, newHistory);

        Ui.showLine();
        System.out.println("Edit-history command executed.");
        Ui.showLine();
    }
}
