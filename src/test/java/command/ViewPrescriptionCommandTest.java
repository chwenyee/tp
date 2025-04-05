package command;

import exception.UnloadedStorageException;
import manager.ManagementSystem;
import manager.Patient;
import manager.Prescription;
import miscellaneous.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.Storage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@@author Basudeb2005
public class ViewPrescriptionCommandTest {
    
    private ManagementSystem system;
    private Ui ui;
    private ByteArrayOutputStream outputStream;
    private Prescription testPrescription;
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
                "S1234567A", "John Doe", "1990-01-01", "Male", 
                "123 Main St", "98765432", new ArrayList<>()
            );
            system.addPatient(patient);
            
            // Add a test prescription
            List<String> symptoms = Arrays.asList("Fever", "Cough");
            List<String> medicines = Arrays.asList("Paracetamol", "Cough syrup");
            Prescription prescription = new Prescription("S1234567A", symptoms, medicines, "Take after meals");
            testPrescription = system.addPrescription(prescription);
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
    }
    
    @Test
    public void execute_existingPrescription_success() {
        // Create command for existing prescription
        ViewPrescriptionCommand command = new ViewPrescriptionCommand(testPrescription.getPrescriptionId());
        
        try {
            // Execute command
            command.execute(system, ui);
            
            // Verify command output
            String output = outputStream.toString();
            assertTrue(output.contains("Prescription details"));
            assertTrue(output.contains(testPrescription.getPrescriptionId()));
            assertTrue(output.contains("Fever"));
            assertTrue(output.contains("Paracetamol"));
            assertTrue(output.contains("Take after meals"));
            assertTrue(output.contains("Prescription HTML file generated"));
        } catch (UnloadedStorageException e) {
            fail("Should not throw exception for an existing prescription: " + e.getMessage());
        }
    }
    
    @Test
    public void execute_nonExistentPrescription_showsError() {
        // Create command for non-existent prescription
        ViewPrescriptionCommand command = new ViewPrescriptionCommand("NONEXISTENT-ID");
        
        try {
            // Execute command
            command.execute(system, ui);
            
            // Verify command output indicates error
            String output = outputStream.toString();
            assertTrue(output.contains("Prescription with ID NONEXISTENT-ID not found"));
        } catch (UnloadedStorageException e) {
            fail("Should handle prescription not found gracefully: " + e.getMessage());
        }
    }
    
    @Test
    public void isExit_returnsFalse() {
        // Create command
        ViewPrescriptionCommand command = new ViewPrescriptionCommand("S1234567A-1");
        
        // Verify isExit returns false
        assertFalse(command.isExit());
    }
} 