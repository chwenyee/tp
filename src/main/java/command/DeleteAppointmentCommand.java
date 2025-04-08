package command;

import exception.UnloadedStorageException;
import manager.Appointment;
import manager.ManagementSystem;
import miscellaneous.Ui;

/**
 * Represents a command to delete an existing appointment from the management system.
 */
public class DeleteAppointmentCommand extends Command {
    protected String apptId;

    /**
     * Constructs a DeleteAppointmentCommand with the specified appointment ID.
     *
     * @param apptId The unique identifier of the appointment to be deleted.
     */
    public DeleteAppointmentCommand(String apptId) {
        this.apptId = apptId;
    }

    /**
     * Gets the appointment ID associated with this command.
     *
     * @return The ID of the appointment to be deleted.
     */
    public String getApptId() {
        return apptId;
    }


    /**
     * Executes the command to delete the appointment from the management system.
     *
     * @param manager The management system that contains the appointment.
     * @param ui      The user interface for displaying messages.
     * @throws UnloadedStorageException If the storage component is not properly loaded.
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) throws UnloadedStorageException {
        Appointment removedAppointment = manager.deleteAppointment(apptId);
        ui.showAppointmentDeleted(manager.getAppointments(), removedAppointment, apptId);
    }
}
