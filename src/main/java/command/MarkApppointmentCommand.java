package command;

import exception.UnloadedStorageException;
import manager.Appointment;
import manager.ManagementSystem;
import miscellaneous.Ui;

/**
 * Represents a command to mark an appointment as done using its unique appointment ID.
 * This command interacts with the ManagementSystem to update the appointment status
 * and utilizes the Ui to provide feedback to the user.
 *
 * <p>This class is part of the Command pattern implementation in the system,
 * using the mark operation for appointments in a reusable and modular way.</p>
 */
public class MarkApppointmentCommand extends Command {
    protected String apptId;

    /**
     * Constructs a MarkApppointmentCommand with the specified appointment ID.
     *
     * @param apptId the ID of the appointment to be marked as completed
     */
    public MarkApppointmentCommand(String apptId) {
        this.apptId = apptId;
    }

    /**
     * Executes the command to mark the appointment with the given ID as completed.
     * It updates the appointment's status through the ManagementSystem and
     * displays the updated appointment list and confirmation via the Ui.
     *
     * @param manager the ManagementSystem responsible for managing appointments
     * @param ui      the Ui responsible for displaying results to the user
     * @throws UnloadedStorageException if the storage system was not initialized when the command was executed
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) throws UnloadedStorageException {
        Appointment appointment = manager.markAppointment(apptId);
        ui.showAppointmentMarked(manager.getAppointments(), appointment, apptId);
    }
}
