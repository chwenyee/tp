package command;

import exception.AppointmentClashException;
import exception.PatientNotFoundException;
import exception.UnloadedStorageException;
import manager.Appointment;
import manager.ManagementSystem;
import miscellaneous.Ui;

public class AddAppointmentCommand extends Command {

    protected Appointment appointment;

    public AddAppointmentCommand(Appointment appointment) {
        this.appointment = appointment;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    @Override
    public void execute(ManagementSystem manager, Ui ui) throws UnloadedStorageException, PatientNotFoundException,
            AppointmentClashException {
        manager.addAppointment(appointment);
        ui.showAppointmentAdded(manager.getAppointments());
    }

}
