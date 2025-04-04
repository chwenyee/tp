import command.Command;
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

public class ClinicEase {

    private ManagementSystem manager;
    private Ui ui;
    private Storage storage;

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
                     UnloadedStorageException | PatientNotFoundException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new ClinicEase("data").run();
    }
}
