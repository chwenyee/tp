package manager;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@@author Basudeb2005
public class PrescriptionTest {

    @Test
    public void toFileFormat_validPrescription_success() {
        // Create a sample prescription
        String patientId = "S1234567A";
        List<String> symptoms = Arrays.asList("Fever", "Cough");
        List<String> medicines = Arrays.asList("Paracetamol", "Cough syrup");
        String notes = "Take after meals";
        
        LocalDateTime timestamp = LocalDateTime.now();
        String prescriptionId = patientId + "-1";
        
        Prescription prescription = new Prescription(
            patientId, prescriptionId, timestamp, symptoms, medicines, notes
        );
        
        // Convert to file format
        String fileFormat = prescription.toFileFormat();
        
        // Verify all required components are in the file format
        assertTrue(fileFormat.contains(prescriptionId));
        assertTrue(fileFormat.contains(patientId));
        assertTrue(fileFormat.contains(timestamp.format(
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        assertTrue(fileFormat.contains("Fever,Cough"));
        assertTrue(fileFormat.contains("Paracetamol,Cough syrup"));
        assertTrue(fileFormat.contains(notes));
    }
    
    @Test
    public void fromFileFormat_validInput_success() {
        // Create a properly formatted file entry
        String fileEntry = "S1234567A-1|S1234567A|2023-04-01 14:30|Fever,Cough|Paracetamol,Cough syrup|Take after meals";
        
        // Parse from file format
        Prescription prescription = Prescription.fromFileFormat(fileEntry);
        
        // Verify all fields are correctly parsed
        assertEquals("S1234567A-1", prescription.getPrescriptionId());
        assertEquals("S1234567A", prescription.getPatientId());
        assertEquals(2, prescription.getSymptoms().size());
        assertTrue(prescription.getSymptoms().contains("Fever"));
        assertTrue(prescription.getSymptoms().contains("Cough"));
        assertEquals(2, prescription.getMedicines().size());
        assertTrue(prescription.getMedicines().contains("Paracetamol"));
        assertTrue(prescription.getMedicines().contains("Cough syrup"));
        assertEquals("Take after meals", prescription.getNotes());
    }
    
    @Test
    public void fromFileFormat_invalidInput_throwsException() {
        // Create an improperly formatted file entry (missing fields)
        String invalidFileEntry = "S1234567A-1|S1234567A";
        
        // This should throw an array index out of bounds exception
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            Prescription.fromFileFormat(invalidFileEntry);
        });
    }
    
    @Test
    public void generateHtml_withPatient_containsPatientInfo() {
        // Create a sample prescription
        String patientId = "S1234567A";
        List<String> symptoms = Arrays.asList("Fever", "Cough");
        List<String> medicines = Arrays.asList("Paracetamol", "Cough syrup");
        String notes = "Take after meals";
        
        LocalDateTime timestamp = LocalDateTime.now();
        String prescriptionId = patientId + "-1";
        
        Prescription prescription = new Prescription(
            patientId, prescriptionId, timestamp, symptoms, medicines, notes
        );
        
        // Create a sample patient
        Patient patient = new Patient(
            patientId, "John Doe", "1990-01-01", "Male", 
            "123 Main St", "98765432", new ArrayList<>()
        );
        
        // Generate HTML
        String html = prescription.generateHtml(patient);
        
        // Verify HTML contains both prescription and patient information
        assertTrue(html.contains("<!DOCTYPE html>"));
        assertTrue(html.contains("<title>Prescription " + prescriptionId + "</title>"));
        assertTrue(html.contains("John Doe"));  // Patient name
        assertTrue(html.contains("Male"));     // Patient gender
        assertTrue(html.contains("Fever"));     // Symptom
        assertTrue(html.contains("Paracetamol")); // Medicine
        assertTrue(html.contains("Take after meals")); // Notes
    }
    
    @Test
    public void generateHtml_withoutPatient_containsOnlyPrescriptionInfo() {
        // Create a sample prescription
        String patientId = "S1234567A";
        List<String> symptoms = Arrays.asList("Fever", "Cough");
        List<String> medicines = Arrays.asList("Paracetamol", "Cough syrup");
        String notes = "Take after meals";
        
        LocalDateTime timestamp = LocalDateTime.now();
        String prescriptionId = patientId + "-1";
        
        Prescription prescription = new Prescription(
            patientId, prescriptionId, timestamp, symptoms, medicines, notes
        );
        
        // Generate HTML without patient
        String html = prescription.generateHtml(null);
        
        // Verify HTML contains prescription info but limited patient info
        assertTrue(html.contains("<!DOCTYPE html>"));
        assertTrue(html.contains("<title>Prescription " + prescriptionId + "</title>"));
        assertFalse(html.contains("John Doe")); // Should not contain patient name
        assertTrue(html.contains(patientId));   // Should contain patient ID
        assertTrue(html.contains("Fever"));     // Should contain symptom
        assertTrue(html.contains("Paracetamol")); // Should contain medicine
        assertTrue(html.contains("Take after meals")); // Should contain notes
    }
} 