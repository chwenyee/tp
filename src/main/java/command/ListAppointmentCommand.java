package command;

import manager.ManagementSystem;
import miscellaneous.Ui;

/**
 * Represents a command to list all appointments in the system.
 * This command displays all current appointments in the appointment list of the management system.
 */
public class ListAppointmentCommand extends Command {
    /**
     * Executes the list appointments command by displaying all appointments.
     *
     * @param manager The management system containing the appointment list.
     * @param ui The user interface for displaying the appointment list.
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) {
        ui.showAppointmentList(manager.getAppointments());
    }
}
