package manager;

import exception.InvalidInputFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        Assertions.assertTrue(fileFormat.contains(prescriptionId));
        Assertions.assertTrue(fileFormat.contains(patientId));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedTimestamp = timestamp.format(formatter);
        Assertions.assertTrue(fileFormat.contains(formattedTimestamp));

        Assertions.assertTrue(fileFormat.contains("Fever,Cough"));
        Assertions.assertTrue(fileFormat.contains("Paracetamol,Cough syrup"));
        Assertions.assertTrue(fileFormat.contains(notes));
    }

    @Test
    public void fromFileFormat_validInput_success() {
        // Create a properly formatted file entry
        String fileEntry = "S1234567A-1|S1234567A|2023-04-01 14:30|"
                + "Fever,Cough|Paracetamol,Cough syrup|Take after meals";

        // Parse from file format
        Prescription prescription = Prescription.fromFileFormat(fileEntry);

        // Verify all fields are correctly parsed
        Assertions.assertEquals("S1234567A-1", prescription.getPrescriptionId());
        Assertions.assertEquals("S1234567A", prescription.getPatientId());
        Assertions.assertEquals(2, prescription.getSymptoms().size());
        Assertions.assertTrue(prescription.getSymptoms().contains("Fever"));
        Assertions.assertTrue(prescription.getSymptoms().contains("Cough"));
        Assertions.assertEquals(2, prescription.getMedicines().size());
        Assertions.assertTrue(prescription.getMedicines().contains("Paracetamol"));
        Assertions.assertTrue(prescription.getMedicines().contains("Cough syrup"));
        Assertions.assertEquals("Take after meals", prescription.getNotes());
    }

    @Test
    public void fromFileFormat_invalidInput_throwsException() {
        // Create an improperly formatted file entry (missing fields)
        String invalidFileEntry = "S1234567A-1|S1234567A";

        // This should throw an array index out of bounds exception
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            Prescription.fromFileFormat(invalidFileEntry);
        });
    }

    @Test
    public void generateHtml_withPatient_containsPatientInfo() throws InvalidInputFormatException {
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
        Assertions.assertTrue(html.contains("<!DOCTYPE html>"));
        Assertions.assertTrue(html.contains("<title>Prescription " + prescriptionId + "</title>"));
        Assertions.assertTrue(html.contains("John Doe"));  // Patient name
        Assertions.assertTrue(html.contains("Male"));     // Patient gender
        Assertions.assertTrue(html.contains("Fever"));     // Symptom
        Assertions.assertTrue(html.contains("Paracetamol")); // Medicine
        Assertions.assertTrue(html.contains("Take after meals")); // Notes
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
        Assertions.assertTrue(html.contains("<!DOCTYPE html>"));
        Assertions.assertTrue(html.contains("<title>Prescription " + prescriptionId + "</title>"));
        Assertions.assertFalse(html.contains("John Doe")); // Should not contain patient name
        Assertions.assertTrue(html.contains(patientId));   // Should contain patient ID
        Assertions.assertTrue(html.contains("Fever"));     // Should contain symptom
        Assertions.assertTrue(html.contains("Paracetamol")); // Should contain medicine
        Assertions.assertTrue(html.contains("Take after meals")); // Should contain notes
    }
}