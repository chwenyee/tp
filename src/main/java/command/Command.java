package command;

import exception.PatientNotFoundException;
import exception.AppointmentClashException;
import exception.DuplicatePatientIDException;
import exception.InvalidInputFormatException;
import exception.UnloadedStorageException;
import manager.ManagementSystem;
import miscellaneous.Ui;

/**
 * Abstract class representing a command in the clinic management system.
 * All commands must extend this class and implement the execute method.
 * Follows the Command pattern to encapsulate requests as objects.
 */
public abstract class Command {

    /**
     * Executes the command with the given management system and UI.
     * Each subclass must implement this method with its specific behavior.
     *
     * @param manager The management system that provides operations on data
     * @param ui The user interface to display results
     * @throws DuplicatePatientIDException If a patient with the same ID already exists
     * @throws UnloadedStorageException If there was an error with storage operations
     * @throws PatientNotFoundException If a requested patient was not found
     * @throws AppointmentClashException If an appointment conflicts with existing ones
     * @throws InvalidInputFormatException If the input format is invalid
     */
    public abstract void execute(ManagementSystem manager, Ui ui)
            throws DuplicatePatientIDException, UnloadedStorageException, PatientNotFoundException,
            AppointmentClashException, InvalidInputFormatException;

    /**
     * Returns whether this command should exit the application.
     * Default implementation returns false.
     * Can be overridden by commands that should exit the application.
     *
     * @return true if the command should cause the application to exit, false otherwise
     */
    public boolean isExit() {
        return false;
    }
}
