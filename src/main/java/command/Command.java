package command;

import exception.PatientNotFoundException;
import exception.AppointmentClashException;
import exception.DuplicatePatientIDException;
import exception.InvalidInputFormatException;
import exception.UnloadedStorageException;
import manager.ManagementSystem;
import miscellaneous.Ui;

public abstract class Command {

    public abstract void execute(ManagementSystem manager, Ui ui)
            throws DuplicatePatientIDException, UnloadedStorageException, PatientNotFoundException,
            AppointmentClashException, InvalidInputFormatException;

    public boolean isExit() {
        return false;
    }
}
