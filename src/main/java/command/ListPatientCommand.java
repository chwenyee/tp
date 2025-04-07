package command;

import manager.ManagementSystem;
import miscellaneous.Ui;

/**
 * Represents a command to list all patients in the system.
 */
public class ListPatientCommand extends Command {

    /**
     * Executes the list patient command.
     * Displays all patients stored in the management system using the UI.
     *
     * @param manager The management system containing the patients.
     * @param ui      The user interface to interact with the user.
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) {
        ui.showPatientList(manager.getPatients());
    }
}
