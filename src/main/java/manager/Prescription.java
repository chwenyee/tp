package manager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a medical prescription in the clinic management system.
 * Contains information about the patient, symptoms, prescribed medicines,
 * and additional notes provided by the doctor.
 */
//@@author Basudeb2005
public class Prescription {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String patientId;
    private final LocalDateTime timestamp;
    private final String prescriptionId;
    private final List<String> symptoms;
    private final List<String> medicines;
    private final String notes;

    /**
     * Constructs a new Prescription with the current timestamp.
     * This constructor is used when creating a new prescription in the system.
     *
     * @param patientId The ID of the patient this prescription is for
     * @param symptoms  List of patient symptoms
     * @param medicines List of prescribed medicines
     * @param notes     Additional instructions or notes for the patient
     */
    public Prescription(String patientId, List<String> symptoms, List<String> medicines, String notes) {
        this.patientId = patientId;
        this.timestamp = LocalDateTime.now();
        this.prescriptionId = patientId + "-" + "1"; // Will be updated to handle numbering
        this.symptoms = new ArrayList<>(symptoms);
        this.medicines = new ArrayList<>(medicines);
        this.notes = notes;
    }

    /**
     * Constructs a Prescription with specified prescriptionId and timestamp.
     * This constructor is primarily used when loading prescriptions from storage.
     *
     * @param patientId      The ID of the patient this prescription is for
     * @param prescriptionId The unique identifier for this prescription
     * @param timestamp      The date and time when the prescription was created
     * @param symptoms       List of patient symptoms
     * @param medicines      List of prescribed medicines
     * @param notes          Additional instructions or notes for the patient
     */
    public Prescription(String patientId, String prescriptionId, LocalDateTime timestamp,
                        List<String> symptoms, List<String> medicines, String notes) {
        this.patientId = patientId;
        this.timestamp = timestamp;
        this.prescriptionId = prescriptionId;
        this.symptoms = new ArrayList<>(symptoms);
        this.medicines = new ArrayList<>(medicines);
        this.notes = notes;
    }

    /**
     * Gets the patient ID associated with this prescription.
     *
     * @return The patient's unique identifier
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Gets the unique identifier for this prescription.
     *
     * @return The prescription's ID in format "patientID-number"
     */
    public String getPrescriptionId() {
        return prescriptionId;
    }

    /**
     * Gets the timestamp when this prescription was created.
     *
     * @return LocalDateTime representing when the prescription was written
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the list of symptoms recorded for this prescription.
     *
     * @return List of symptom descriptions
     */
    public List<String> getSymptoms() {
        return symptoms;
    }

    /**
     * Gets the list of medicines prescribed.
     *
     * @return List of medicine names and dosage instructions
     */
    public List<String> getMedicines() {
        return medicines;
    }

    /**
     * Gets any additional notes or instructions for this prescription.
     *
     * @return Notes string or empty if no notes were provided
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Returns a string representation of the prescription with formatted details.
     * Includes prescription ID, timestamp, patient ID, and lists of symptoms and medicines.
     *
     * @return A formatted multi-line string representation of the prescription
     */
    @Override
    public String toString() {
        StringBuilder symptomsStr = new StringBuilder();
        for (String symptom : symptoms) {
            symptomsStr.append("- ").append(symptom).append("\n");
        }

        StringBuilder medicinesStr = new StringBuilder();
        for (String medicine : medicines) {
            medicinesStr.append("- ").append(medicine).append("\n");
        }

        return String.format(
                "Prescription [%s] (%s)\n"
                        + "Patient ID: %s\n"
                        + "Symptoms: \n%s"
                        + "Medicines: \n%s"
                        + "Notes: %s",
                prescriptionId, timestamp.format(DATE_TIME_FORMATTER),
                patientId, symptomsStr.toString(), medicinesStr.toString(), notes);
    }

    /**
     * Converts the prescription to a storage-friendly string format.
     * Uses pipe-delimited format to store all prescription attributes.
     *
     * @return A string representation suitable for file storage
     */
    public String toFileFormat() {
        return String.join("|",
                prescriptionId,
                patientId,
                timestamp.format(DATE_TIME_FORMATTER),
                String.join(",", symptoms),
                String.join(",", medicines),
                notes);
    }

    /**
     * Creates a Prescription object from a storage file entry.
     * Parses the pipe-delimited string from storage into a Prescription object.
     *
     * @param fileEntry The string from the storage file
     * @return A new Prescription object with the stored data
     */
    public static Prescription fromFileFormat(String fileEntry) {
        String[] parts = fileEntry.split("\\|");

        // Check if we have sufficient parts to create a valid prescription
        if (parts.length < 5) {
            System.out.println("Warning: Invalid prescription format: " + fileEntry);
            return null;
        }

        String prescriptionId = parts[0];
        String patientId = parts[1];
        LocalDateTime timestamp = LocalDateTime.parse(parts[2], DATE_TIME_FORMATTER);
        List<String> symptoms = parts[3].isEmpty() ? new ArrayList<>() : Arrays.asList(parts[3].split(","));
        List<String> medicines = parts[4].isEmpty() ? new ArrayList<>() : Arrays.asList(parts[4].split(","));
        String notes = parts.length > 5 ? parts[5] : ""; // Default to empty string if notes aren't provided

        return new Prescription(patientId, prescriptionId, timestamp, symptoms, medicines, notes);
    }

    /**
     * Generates a formatted HTML document for the prescription.
     * Creates a professional-looking prescription that can be printed.
     * Includes patient details if provided, symptoms, medicines, and notes.
     *
     * @param patient The Patient object (can be null if patient details unavailable)
     * @return A string containing HTML markup for the prescription
     */
    public String generateHtml(Patient patient) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\">\n")
                .append("<head>\n")
                .append("  <meta charset=\"UTF-8\">\n")
                .append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
                .append("  <title>Prescription ").append(prescriptionId).append("</title>\n")
                .append("  <style>\n")
                .append("    body { font-family: Arial, sans-serif; margin: 40px; }\n")
                .append("    .prescription { border: 1px solid #333; padding: " +
                        "20px; max-width: 800px; margin: 0 auto; }\n")
                .append("    .header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 10px;\n")
                .append("             margin-bottom: 20px; }\n")
                .append("    .section { margin-bottom: 15px; }\n")
                .append("    h1 { color: #333; }\n")
                .append("    h2 { color: #555; margin-bottom: 5px; }\n")
                .append("    .footer { margin-top: 50px; border-top: 1px solid #ccc; padding-top: 10px;\n")
                .append("             text-align: center; }\n")
                .append("    @media print { .no-print { display: none; } }\n")
                .append("    table { width: 100%; border-collapse: collapse; }\n")
                .append("    td { padding: 5px; }\n")
                .append("    .patient-info td:first-child { font-weight: bold; width: 150px; }\n")
                .append("  </style>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("  <div class=\"prescription\">\n")
                .append("    <div class=\"header\">\n")
                .append("      <h1>ClinicEase Medical Prescription</h1>\n")
                .append("      <p>Prescription ID: ").append(prescriptionId).append("</p>\n")
                .append("      <p>Date: ").append(timestamp.format(DATE_TIME_FORMATTER)).append("</p>\n")
                .append("    </div>\n");

        if (patient != null) {
            html.append("    <div class=\"section\">\n")
                    .append("      <h2>Patient Information</h2>\n")
                    .append("      <table class=\"patient-info\">\n")
                    .append("        <tr><td>Patient ID:</td><td>").append(patient.getId()).append("</td></tr>\n")
                    .append("        <tr><td>Name:</td><td>").append(patient.getName()).append("</td></tr>\n")
                    .append("        <tr><td>Gender:</td><td>").append(patient.getGender()).append("</td></tr>\n")
                    .append("        <tr><td>Date of Birth:</td><td>").append(patient.getDob()).append("</td></tr>\n")
                    .append("        <tr><td>Contact:</td><td>").append(patient.getContactInfo()).append("</td></tr>\n")
                    .append("      </table>\n")
                    .append("    </div>\n");
        } else {
            html.append("    <div class=\"section\">\n")
                    .append("      <h2>Patient Information</h2>\n")
                    .append("      <table class=\"patient-info\">\n")
                    .append("        <tr><td>Patient ID:</td><td>").append(patientId).append("</td></tr>\n")
                    .append("      </table>\n")
                    .append("    </div>\n");
        }

        html.append("    <div class=\"section\">\n")
                .append("      <h2>Symptoms</h2>\n")
                .append("      <ul>\n");

        for (String symptom : symptoms) {
            html.append("        <li>").append(symptom).append("</li>\n");
        }

        html.append("      </ul>\n")
                .append("    </div>\n")
                .append("    <div class=\"section\">\n")
                .append("      <h2>Prescribed Medications</h2>\n")
                .append("      <ul>\n");

        for (String medicine : medicines) {
            html.append("        <li>").append(medicine).append("</li>\n");
        }

        html.append("      </ul>\n")
                .append("    </div>\n");

        if (notes != null && !notes.isEmpty()) {
            html.append("    <div class=\"section\">\n")
                    .append("      <h2>Special Instructions</h2>\n")
                    .append("      <p>").append(notes).append("</p>\n")
                    .append("    </div>\n");
        }

        html.append("    <div class=\"footer\">\n")
                .append("      <p>This prescription was generated by ClinicEase System</p>\n")
                .append("      <button class=\"no-print\" onclick=\"window.print();\">Print Prescription</button>\n")
                .append("    </div>\n")
                .append("  </div>\n")
                .append("</body>\n")
                .append("</html>\n");

        return html.toString();
    }
} 

