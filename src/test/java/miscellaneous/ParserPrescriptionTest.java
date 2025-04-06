package miscellaneous;

import exception.InvalidInputFormatException;
import manager.Prescription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

//@@author Basudeb2005
public class ParserPrescriptionTest {

    @Test
    public void parseAddPrescription_validInput_success() throws InvalidInputFormatException {
        // Valid input with all fields including optional notes
        String input = "add-prescription ic/S1234567A s/Fever, Cough m/Paracetamol, Cough syrup nt/Take after meals";
        
        Prescription prescription = Parser.parseAddPrescription(input);
        
        // Verify all fields are correctly parsed
        Assertions.assertEquals("S1234567A", prescription.getPatientId());
        List<String> symptoms = prescription.getSymptoms();
        Assertions.assertEquals(2, symptoms.size());
        Assertions.assertTrue(symptoms.contains("Fever"));
        Assertions.assertTrue(symptoms.contains("Cough"));
        
        List<String> medicines = prescription.getMedicines();
        Assertions.assertEquals(2, medicines.size());
        Assertions.assertTrue(medicines.contains("Paracetamol"));
        Assertions.assertTrue(medicines.contains("Cough syrup"));
        
        Assertions.assertEquals("Take after meals", prescription.getNotes());
    }
    
    @Test
    public void parseAddPrescription_missingPatientId_throwsException() {
        // Missing the required ic/ parameter
        String input = "add-prescription s/Fever m/Paracetamol nt/Take after meals";
        
        Assertions.assertThrows(InvalidInputFormatException.class, () -> {
            Parser.parseAddPrescription(input);
        });
    }
    
    @Test
    public void parseAddPrescription_missingSymptoms_throwsException() {
        // Missing the required s/ parameter
        String input = "add-prescription ic/S1234567A m/Paracetamol nt/Take after meals";
        
        Assertions.assertThrows(InvalidInputFormatException.class, () -> {
            Parser.parseAddPrescription(input);
        });
    }
    
    @Test
    public void parseAddPrescription_missingMedicines_throwsException() {
        // Missing the required m/ parameter
        String input = "add-prescription ic/S1234567A s/Fever nt/Take after meals";
        
        Assertions.assertThrows(InvalidInputFormatException.class, () -> {
            Parser.parseAddPrescription(input);
        });
    }
    
    @Test
    public void parseAddPrescription_noOptionalNotes_success() throws InvalidInputFormatException {
        // Valid input with only required fields
        String input = "add-prescription ic/S1234567A s/Fever m/Paracetamol";
        
        Prescription prescription = Parser.parseAddPrescription(input);
        
        // Verify required fields are parsed and optional field is empty
        Assertions.assertEquals("S1234567A", prescription.getPatientId());
        Assertions.assertEquals(1, prescription.getSymptoms().size());
        Assertions.assertEquals(1, prescription.getMedicines().size());
        Assertions.assertEquals("", prescription.getNotes());
    }
    
    @Test
    public void parseViewAllPrescriptions_validInput_success() throws InvalidInputFormatException {
        // Valid command format
        String input = "view-all-prescriptions S1234567A";
        
        String patientId = Parser.parseViewAllPrescriptions(input);
        
        Assertions.assertEquals("S1234567A", patientId);
    }
    
    @Test
    public void parseViewAllPrescriptions_missingPatientId_throwsException() {
        // Command without patient ID - needs to be shorter than 22 characters
        String input = "view-all-prescriptions";
        
        Assertions.assertThrows(InvalidInputFormatException.class, () -> {
            Parser.parseViewAllPrescriptions(input);
        });
    }
    
    @Test
    public void parseViewPrescription_validInput_success() throws InvalidInputFormatException {
        // Valid command format
        String input = "view-prescription S1234567A-1";
        
        String prescriptionId = Parser.parseViewPrescription(input);
        
        Assertions.assertEquals("S1234567A-1", prescriptionId);
    }
    
    @Test
    public void parseViewPrescription_missingPrescriptionId_throwsException() {
        // Command without prescription ID - needs to be shorter than 17 characters
        String input = "view-prescription";
        
        Assertions.assertThrows(InvalidInputFormatException.class, () -> {
            Parser.parseViewPrescription(input);
        });
    }
} 



