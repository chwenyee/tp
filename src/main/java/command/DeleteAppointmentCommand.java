package command;

import exception.UnloadedStorageException;
import manager.Appointment;
import manager.ManagementSystem;
import miscellaneous.Ui;

public class DeleteAppointmentCommand extends Command {
    protected String apptId;

    public DeleteAppointmentCommand(String apptId) {
        this.apptId = apptId;
    }

    public String getApptId() {
        return apptId;
    }

    @Override
    public void execute(ManagementSystem manager, Ui ui) throws UnloadedStorageException {
        Appointment removedAppointment = manager.deleteAppointment(apptId);
        ui.showAppointmentDeleted(manager.getAppointments(), removedAppointment, apptId);
    }
}
