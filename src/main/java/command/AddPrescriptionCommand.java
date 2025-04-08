package command;

import exception.UnloadedStorageException;
import manager.ManagementSystem;
import manager.Prescription;
import miscellaneous.Ui;

/**
 * Command to add a new prescription to the system.
 * Handles the creation of a new prescription and displays
 * feedback about the operation to the user.
 */
//@@author Basudeb2005
public class AddPrescriptionCommand extends Command {
    private final Prescription prescription;

    /**
     * Constructs an AddPrescriptionCommand with the specified prescription.
     *
     * @param prescription The prescription to be added to the system
     */
    public AddPrescriptionCommand(Prescription prescription) {
        this.prescription = prescription;
    }

    /**
     * Executes the add prescription command.
     * Adds the prescription to the management system and displays confirmation.
     * Shows error message if the patient ID doesn't exist.
     *
     * @param manager The management system that handles the data
     * @param ui      The user interface to display results
     * @throws UnloadedStorageException If there was an error saving to storage
     */
    @Override
    public void execute(ManagementSystem manager, Ui ui) throws UnloadedStorageException {
        try {
            Prescription addedPrescription = manager.addPrescription(prescription);
            ui.showLine();
            System.out.println("Successfully added prescription:");
            System.out.println(addedPrescription.toString());
            System.out.println("");
            System.out.println("Prescription has been generated.");
            System.out.println("View the prescription for the patient with ID: " + addedPrescription.getPatientId());
            System.out.println("and prescription ID: " + addedPrescription.getPrescriptionId());
            ui.showLine();
        } catch (IllegalArgumentException e) {
            ui.showError("Failed to add prescription: " + e.getMessage());
        }
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

