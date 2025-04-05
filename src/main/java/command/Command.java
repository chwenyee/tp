package command;

import exception.AppointmentClashException;
import exception.DuplicatePatientIDException;
import exception.PatientNotFoundException;
import exception.UnloadedStorageException;
import manager.ManagementSystem;
import miscellaneous.Ui;

public abstract class Command {

    public abstract void execute(ManagementSystem manager, Ui ui)
            throws DuplicatePatientIDException, UnloadedStorageException, PatientNotFoundException,
            AppointmentClashException;

    public boolean isExit() {
        return false;
    }
}
