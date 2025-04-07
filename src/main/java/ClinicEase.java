import command.Command;
import exception.AppointmentClashException;
import exception.DuplicatePatientIDException;
import exception.InvalidInputFormatException;
import exception.PatientNotFoundException;
import exception.UnknownCommandException;
import exception.UnloadedStorageException;
import manager.ManagementSystem;
import manager.Patient;
import manager.Appointment;
import manager.Prescription;
import miscellaneous.Parser;
import miscellaneous.Ui;
import storage.Storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class of the ClinicEase application, responsible for initializing
 * components and driving the command execution loop.
 * ClinicEase is a desktop application designed for clinic management,
 * providing a command-line interface for managing patients, appointments, 
 * and prescriptions.
 */
public class ClinicEase {

    private ManagementSystem manager;
    private Ui ui;
    private Storage storage;

    /**
     * Constructs a new ClinicEase application with a specified storage location.
     * Initializes the UI, storage, and management system components.
     * Attempts to load existing data from storage files if available.
     *
     * @param filePath The directory path where data files will be stored
     */
    public ClinicEase(String filePath) {
        assert filePath != null : "File path cannot be null";
        this.ui = new Ui();
        this.storage = new Storage(filePath);

        try {
            List<Patient> patients = Storage.loadPatients();
            List<Prescription> prescriptions = Storage.loadPrescriptions();
            this.manager = new ManagementSystem(patients, new ArrayList<>(), prescriptions);

            List<Appointment> appointments = Storage.loadAppointments(manager);
            manager.setAppointments(appointments);
        } catch (UnloadedStorageException e) {
            ui.showError("Could not load data: " + e.getMessage());
            this.manager = new ManagementSystem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }
    }

    /**
     * Starts the application's main execution loop.
     * Displays welcome message, continuously reads user commands, 
     * executes them until an exit command is received.
     * Handles exceptions by displaying appropriate error messages.
     */
    public void run() {
        ui.showWelcome();
        boolean running = true;

        while (running) {
            try {
                String input = ui.readCommand();
                if (input.isEmpty()) {
                    continue;
                }
                Command command = Parser.parse(input);
                command.execute(manager, ui);
                running = !command.isExit();
            } catch (InvalidInputFormatException | UnknownCommandException | DuplicatePatientIDException |
                     UnloadedStorageException | PatientNotFoundException | AppointmentClashException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * The entry point of the application.
     * Creates a new ClinicEase instance with storage in the "data" directory
     * and starts the application.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        new ClinicEase("data").run();
    }
}
