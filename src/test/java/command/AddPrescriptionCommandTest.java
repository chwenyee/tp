package command;

import exception.UnloadedStorageException;
import manager.ManagementSystem;
import manager.Patient;
import manager.Prescription;
import miscellaneous.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import storage.Storage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@@author Basudeb2005
public class AddPrescriptionCommandTest {

    private ManagementSystem system;
    private Ui ui;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        // Save original System.out
        originalOut = System.out;

        // Redirect System.out to capture output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            // Initialize Storage with temp directory
            new Storage(System.getProperty("java.io.tmpdir") + File.separator + "clinicease_test");

            // Initialize system and UI
            system = new ManagementSystem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            ui = new Ui();

            // Add a test patient
            Patient patient = new Patient(
                    "S1234567A", "John Doe", "1990-01-01", "M",
                    "123 Main St", "98765432", new ArrayList<>()
            );
            system.addPatient(patient);
        } catch (Exception e) {
            Assertions.fail("Failed to set up test: " + e.getMessage());
        }
    }

    @Test
    public void execute_validPrescription_success() {
        // Create prescription and command
        List<String> symptoms = Arrays.asList("Fever", "Cough");
        List<String> medicines = Arrays.asList("Paracetamol", "Cough syrup");
        Prescription prescription = new Prescription("S1234567A", symptoms, medicines, "Take after meals");
        AddPrescriptionCommand command = new AddPrescriptionCommand(prescription);

        try {
            // Execute command
            command.execute(system, ui);

            // Verify command output indicates success
            String output = outputStream.toString();
            Assertions.assertTrue(output.contains("Successfully added prescription"));
            Assertions.assertTrue(output.contains("S1234567A-1")); // Prescription ID
            Assertions.assertTrue(output.contains("Fever"));
            Assertions.assertTrue(output.contains("Paracetamol"));
            Assertions.assertTrue(output.contains("Take after meals"));

            // Verify prescription was added to the system
            Assertions.assertEquals(1, system.getPrescriptions().size());
            Assertions.assertNotNull(system.getPrescriptionById("S1234567A-1"));
        } catch (UnloadedStorageException e) {
            Assertions.fail("Should not throw exception for a valid prescription: " + e.getMessage());
        }
    }

    @Test
    public void execute_nonExistentPatient_showsError() {
        // Create prescription for non-existent patient and command
        List<String> symptoms = Arrays.asList("Fever");
        List<String> medicines = Arrays.asList("Paracetamol");
        Prescription prescription = new Prescription("NONEXISTENT", symptoms, medicines, "");
        AddPrescriptionCommand command = new AddPrescriptionCommand(prescription);

        try {
            // Execute command
            command.execute(system, ui);

            // Verify command output indicates error
            String output = outputStream.toString();
            Assertions.assertTrue(output.contains("Failed to add prescription"));
            Assertions.assertTrue(output.contains("Patient with NRIC: NONEXISTENT not found"));

            // Verify no prescription was added to the system
            Assertions.assertEquals(0, system.getPrescriptions().size());
        } catch (UnloadedStorageException e) {
            Assertions.fail("Should handle patient not found gracefully: " + e.getMessage());
        }
    }

    @Test
    public void isExit_returnsFalse() {
        // Create command
        List<String> symptoms = Arrays.asList("Fever");
        List<String> medicines = Arrays.asList("Paracetamol");
        Prescription prescription = new Prescription("S1234567A", symptoms, medicines, "");
        AddPrescriptionCommand command = new AddPrescriptionCommand(prescription);

        // Verify isExit returns false
        Assertions.assertFalse(command.isExit());
    }
} 



