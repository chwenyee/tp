package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import storage.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@@author Basudeb2005
public class ManagementSystemPrescriptionTest {
    
    private ManagementSystem system;
    private Patient testPatient;
    
    @BeforeEach
    public void setUp() {
        // Create a temporary directory for Storage
        try {
            // Initialize Storage with temp directory
            new Storage(System.getProperty("java.io.tmpdir") + File.separator + "clinicease_test");
            
            // Initialize system with empty lists
            system = new ManagementSystem(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            
            // Add a test patient
            testPatient = new Patient(
                "S1234567A", "John Doe", "01-01-1990", "Male",
                "123 Main St", "98765432", new ArrayList<>()
            );
            
            system.addPatient(testPatient);
        } catch (Exception e) {
            Assertions.fail("Failed to set up test: " + e.getMessage());
        }
    }
    
    @Test
    public void addPrescription_validPrescription_success() {
        // Create a prescription for an existing patient
        List<String> symptoms = Arrays.asList("Fever", "Cough");
        List<String> medicines = Arrays.asList("Paracetamol", "Cough syrup");
        Prescription prescription = new Prescription(testPatient.getId(), symptoms, medicines, "Take after meals");
        
        try {
            // Add prescription
            Prescription addedPrescription = system.addPrescription(prescription);
            
            // Verify prescription was added with correct attributes
            Assertions.assertNotNull(addedPrescription);
            Assertions.assertEquals(testPatient.getId() + "-1", addedPrescription.getPrescriptionId());
            Assertions.assertEquals(testPatient.getId(), addedPrescription.getPatientId());
            Assertions.assertEquals(symptoms, addedPrescription.getSymptoms());
            Assertions.assertEquals(medicines, addedPrescription.getMedicines());
            Assertions.assertEquals("Take after meals", addedPrescription.getNotes());
            
            // Verify it was added to the system's list
            Assertions.assertEquals(1, system.getPrescriptions().size());
        } catch (Exception e) {
            Assertions.fail("Should not throw exception for a valid prescription: " + e.getMessage());
        }
    }
    
    @Test
    public void addPrescription_nonExistentPatient_throwsException() {
        // Create a prescription with non-existent patient ID
        List<String> symptoms = Arrays.asList("Fever");
        List<String> medicines = Arrays.asList("Paracetamol");
        Prescription prescription = new Prescription("NONEXISTENT", symptoms, medicines, "");
        
        // Try to add prescription - should throw IllegalArgumentException
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            system.addPrescription(prescription);
        });
        
        Assertions.assertTrue(exception.getMessage().contains("Patient with NRIC: NONEXISTENT not found"));
    }
    
    @Test
    public void getPrescriptionsForPatient_multipleExistingPrescriptions_returnsAllPrescriptions() {
        try {
            // Add two prescriptions for the same patient
            List<String> symptoms1 = Arrays.asList("Fever");
            List<String> medicines1 = Arrays.asList("Paracetamol");
            Prescription prescription1 = new Prescription(testPatient.getId(), symptoms1, medicines1, "");
            
            List<String> symptoms2 = Arrays.asList("Headache");
            List<String> medicines2 = Arrays.asList("Ibuprofen");
            Prescription prescription2 = new Prescription(testPatient.getId(), symptoms2, medicines2, "");
            
            system.addPrescription(prescription1);
            system.addPrescription(prescription2);
            
            // Get prescriptions for patient
            List<Prescription> results = system.getPrescriptionsForPatient(testPatient.getId());
            
            // Verify both prescriptions are returned
            Assertions.assertEquals(2, results.size());
            Assertions.assertTrue(results.get(0).getSymptoms().contains("Fever"));
            Assertions.assertTrue(results.get(1).getSymptoms().contains("Headache"));
        } catch (Exception e) {
            Assertions.fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void getPrescriptionsForPatient_noExistingPrescriptions_returnsEmptyList() {
        // Get prescriptions for patient with no prescriptions
        List<Prescription> results = system.getPrescriptionsForPatient(testPatient.getId());
        
        // Verify empty list is returned
        Assertions.assertTrue(results.isEmpty());
    }
    
    @Test
    public void getPrescriptionsForPatient_nonExistentPatient_returnsEmptyList() {
        // Get prescriptions for non-existent patient ID
        List<Prescription> results = system.getPrescriptionsForPatient("NONEXISTENT");
        
        // Verify empty list is returned
        Assertions.assertTrue(results.isEmpty());
    }
    
    @Test
    public void getPrescriptionById_existingPrescription_returnsPrescription() {
        try {
            // Add a prescription
            List<String> symptoms = Arrays.asList("Fever");
            List<String> medicines = Arrays.asList("Paracetamol");
            Prescription added = system.addPrescription(
                new Prescription(testPatient.getId(), symptoms, medicines, "")
            );
            
            // Get prescription by ID
            Prescription result = system.getPrescriptionById(added.getPrescriptionId());
            
            // Verify prescription is returned
            Assertions.assertNotNull(result);
            Assertions.assertEquals(added.getPrescriptionId(), result.getPrescriptionId());
            Assertions.assertEquals(testPatient.getId(), result.getPatientId());
            Assertions.assertTrue(result.getSymptoms().contains("Fever"));
            Assertions.assertTrue(result.getMedicines().contains("Paracetamol"));
        } catch (Exception e) {
            Assertions.fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void getPrescriptionById_nonExistentPrescription_returnsNull() {
        // Get prescription with non-existent ID
        Prescription result = system.getPrescriptionById("NON-EXISTENT-ID");
        
        // Verify null is returned
        Assertions.assertNull(result);
    }
} 



