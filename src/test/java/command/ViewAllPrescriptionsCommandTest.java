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
public class ViewAllPrescriptionsCommandTest {
    
    private ManagementSystem system;
    private Ui ui;
    private ByteArrayOutputStream outputStream;
    private Patient testPatient;
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
            testPatient = new Patient(
                "S1234567A", "John Doe", "1990-01-01", "M",
                "123 Main St", "98765432", new ArrayList<>()
            );
            system.addPatient(testPatient);
        } catch (Exception e) {
            Assertions.fail("Failed to set up test: " + e.getMessage());
        }
    }
    
    @Test
    public void execute_patientWithPrescriptions_success() {
        try {
            // Add some prescriptions for the test patient
            List<String> symptoms1 = Arrays.asList("Fever", "Cough");
            List<String> medicines1 = Arrays.asList("Paracetamol", "Cough syrup");
            system.addPrescription(new Prescription(testPatient.getId(), symptoms1, medicines1, "Take after meals"));
            
            List<String> symptoms2 = Arrays.asList("Headache");
            List<String> medicines2 = Arrays.asList("Ibuprofen");
            system.addPrescription(new Prescription(testPatient.getId(), symptoms2, medicines2, "Take with water"));
            
            // Create command
            ViewAllPrescriptionsCommand command = new ViewAllPrescriptionsCommand(testPatient.getId());
            
            // Execute command
            command.execute(system, ui);
            
            // Verify command output
            String output = outputStream.toString();
            Assertions.assertTrue(output.contains("Prescriptions for patient John Doe"));
            Assertions.assertTrue(output.contains("Fever"));
            Assertions.assertTrue(output.contains("Cough"));
            Assertions.assertTrue(output.contains("Headache"));
            Assertions.assertTrue(output.contains("Paracetamol"));
            Assertions.assertTrue(output.contains("Ibuprofen"));
            Assertions.assertTrue(output.contains("Take after meals"));
            Assertions.assertTrue(output.contains("Take with water"));
            Assertions.assertTrue(output.contains("Total prescriptions: 2"));
        } catch (UnloadedStorageException e) {
            Assertions.fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void execute_patientWithNoPrescriptions_showsMessage() {
        try {
            // Create command for patient with no prescriptions
            ViewAllPrescriptionsCommand command = new ViewAllPrescriptionsCommand(testPatient.getId());
            
            // Execute command
            command.execute(system, ui);
            
            // Verify command output
            String output = outputStream.toString();
            Assertions.assertTrue(output.contains("No prescriptions found for patient John Doe"));
        } catch (UnloadedStorageException e) {
            Assertions.fail("Should not throw exception for patient with no prescriptions: " + e.getMessage());
        }
    }
    
    @Test
    public void execute_nonExistentPatient_showsError() {
        // Create command for non-existent patient
        ViewAllPrescriptionsCommand command = new ViewAllPrescriptionsCommand("NONEXISTENT");
        
        try {
            // Execute command
            command.execute(system, ui);
            
            // Verify command output indicates error
            String output = outputStream.toString();
            Assertions.assertTrue(output.contains("Patient with ID NONEXISTENT not found"));
        } catch (UnloadedStorageException e) {
            Assertions.fail("Should handle patient not found gracefully: " + e.getMessage());
        }
    }
    
    @Test
    public void isExit_returnsFalse() {
        // Create command
        ViewAllPrescriptionsCommand command = new ViewAllPrescriptionsCommand("S1234567A");
        
        // Verify isExit returns false
        Assertions.assertFalse(command.isExit());
    }
} 





