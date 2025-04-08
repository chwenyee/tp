package command;

import exception.UnloadedStorageException;
import manager.ManagementSystem;
import manager.Patient;
import manager.Prescription;
import miscellaneous.Ui;
import storage.Storage;

import java.io.File;

/**
 * Command to view a specific prescription and generate its HTML document.
 * Displays detailed prescription information and creates a printable
 * HTML version of the prescription.
 */
//@@author Basudeb2005
public class ViewPrescriptionCommand extends Command {
    private final String prescriptionId;

    /**
     * Constructs a ViewPrescriptionCommand with the specified prescription ID.
     *
     * @param prescriptionId The ID of the prescription to view
     */
    public ViewPrescriptionCommand(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    /**
     * Executes the view prescription command.
     * Retrieves the prescription with the specified ID and displays its details.
     * Also generates an HTML file for printing the prescription.
     *
     * @param manager The management system that handles the data
     * @param ui The user interface to display results
     * @throws UnloadedStorageException If there was an error with storage operations
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) throws UnloadedStorageException {
        Prescription prescription = manager.getPrescriptionById(prescriptionId);
        if (prescription == null) {
            ui.showError("Prescription with ID " + prescriptionId + " not found.");
            return;
        }

        Patient patient = manager.viewPatient(prescription.getPatientId());
        
        ui.showLine();
        System.out.println("Prescription details:");
        System.out.println(prescription.toString());
        System.out.println("");
        
        // Generate HTML file
        Storage.savePrescriptionHtml(prescription, patient);
        
        String fileName = "prescription_" + prescription.getPatientId() + "_" 
                + prescription.getPrescriptionId().split("-")[1] + ".html";
        String filePath = new File("").getAbsolutePath() 
                + File.separator + "data" 
                + File.separator + "prescriptions" 
                + File.separator + fileName;
        
        System.out.println("Prescription HTML file generated at: " + filePath);
        System.out.println("Open this file in a web browser to view and print the prescription.");
        ui.showLine();
    }

    /**
     * Returns whether this command should exit the application.
     *
     * @return false (this command does not exit the application)
     */
    @Override
    public boolean isExit() {
        return false;
    }
} 

