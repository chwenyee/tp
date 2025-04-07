package command;

import exception.UnloadedStorageException;
import manager.Appointment;
import manager.ManagementSystem;
import miscellaneous.Ui;

/**
 * Represents a command to unmark an appointment, indicating it is no longer completed.
 * This command interacts with the ManagementSystem to revert the appointment status
 * and utilizes the Ui to provide feedback to the user.
 *
 * <p>It is part of the Command pattern implementation, encapsulating the logic
 * for unmarking appointments in a modular and reusable way.</p>
 */
public class UnmarkAppointmentCommand extends Command {
    protected String apptId;

    /**
     * Constructs an UnmarkAppointmentCommand with the specified appointment ID.
     *
     * @param apptId the ID of the appointment to be unmarked
     */
    public UnmarkAppointmentCommand(String apptId) {
        this.apptId = apptId;
    }

    /**
     * Executes command to unmark the appointment with the given ID.
     * It updates appointment's status through the ManagementSystem and
     * displays the updated list and confirmation via the Ui.
     *
     * @param manager the ManagementSystem responsible for managing appointments
     * @param ui the Ui responsible for displaying results to the user
     * @throws UnloadedStorageException if the storage system was not initialized when the command was executed
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) throws UnloadedStorageException {
        Appointment appointment = manager.unmarkAppointment(apptId);
        ui.showAppointmentUnmarked(manager.getAppointments(), appointment, apptId);
    }
}
