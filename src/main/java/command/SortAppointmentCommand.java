package command;

import exception.DuplicatePatientIDException;
import exception.UnloadedStorageException;
import manager.Appointment;
import manager.ManagementSystem;
import miscellaneous.Ui;

import java.util.List;

/**
 * Represents a command to sort appointments in the management system.
 */
public class SortAppointmentCommand extends Command {
    protected String type;

    /**
     * Constructs a SortAppointmentCommand with the specified sorting type.
     *
     * @param type The type of sorting to perform ("date" or other supported types).
     */
    public SortAppointmentCommand(String type) {
        this.type = type;
    }

    /**
     * Executes the command to sort appointments in the management system.
     *
     * @param manager The management system containing the appointments.
     * @param ui      The user interface for displaying the sorted appointments.
     * @throws DuplicatePatientIDException If duplicate patient IDs are encountered.
     * @throws UnloadedStorageException    If the storage component is not properly loaded.
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) throws DuplicatePatientIDException,
            UnloadedStorageException {
        if (type.equals("date")) {
            List<Appointment> sortedApptByDateTime = manager.sortAppointmentsByDateTime(manager.getAppointments());
            ui.showAppointmentList(sortedApptByDateTime);
        } else {
            List<Appointment> sortedApptById = manager.sortAppointmentsById(manager.getAppointments());
            ui.showAppointmentList(sortedApptById);
        }
    }
}
