package command;

import exception.AppointmentClashException;
import exception.PatientNotFoundException;
import exception.UnloadedStorageException;
import manager.Appointment;
import manager.ManagementSystem;
import miscellaneous.Ui;

/**
 * Represents a command to add a new appointment to the management system.
 */
public class AddAppointmentCommand extends Command {

    protected Appointment appointment;

    /**
     * Constructs an AddAppointmentCommand with the specified appointment.
     *
     * @param appointment The appointment to be added to the system.
     */
    public AddAppointmentCommand(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * Gets the appointment associated with this command.
     *
     * @return The appointment to be added.
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * Executes the command to add the appointment to the appointment list in management system.
     *
     * @param manager The management system that stores the appointment.
     * @param ui The user interface for displaying messages.
     * @throws UnloadedStorageException If the storage component is not properly loaded.
     * @throws PatientNotFoundException If the patient associated with the appointment is not found.
     * @throws AppointmentClashException If the appointment time conflicts with an existing appointment.
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) throws UnloadedStorageException, PatientNotFoundException,
            AppointmentClashException {
        manager.addAppointment(appointment);
        ui.showAppointmentAdded(manager.getAppointments());
    }

}
