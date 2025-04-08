package manager;

import exception.InvalidInputFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a patient in the clinic management system.
 * Stores patient details such as NRIC, name, date of birth, contact info, gender,
 * address, medical history, and appointments.
 */
public class Patient {

    private String id;
    private String name;
    private LocalDate dob;
    private String contactInfo;
    private String gender;
    private String address;
    private final List<String> medicalHistory;
    private final List<Appointment> appointments;

    /**
     * Constructs a new Patient object with the given details.
     *
     * @param id             The NRIC of the patient.
     * @param name           The name of the patient.
     * @param dobStr         The date of birth in yyyy-MM-dd format.
     * @param gender         The gender (M or F).
     * @param address        The address.
     * @param contactInfo    The 8-digit contact number.
     * @param medicalHistory A list of past medical history.
     * @throws InvalidInputFormatException if any input format is invalid.
     */
    public Patient(String id, String name, String dobStr, String gender, String address,
                   String contactInfo, List<String> medicalHistory) throws InvalidInputFormatException {
        assert id != null && !id.isBlank() : "Patient ID cannot be null or blank";
        assert name != null && !name.isBlank() : "Patient name cannot be null or blank";
        assert dobStr != null : "Date of birth cannot be null";
        assert gender != null : "Gender cannot be null";
        assert address != null : "Address cannot be null";
        assert contactInfo != null : "Contact info cannot be null";
        assert medicalHistory != null : "Medical history list cannot be null";

        this.id = parseValidIC(id);
        this.name = name;
        this.dob = parseAndValidateDob(dobStr);
        this.gender = checkGender(gender);
        this.address = address;
        this.contactInfo = parseContactInfo(contactInfo);
        this.medicalHistory = new ArrayList<>(medicalHistory);
        this.appointments = new ArrayList<>();
    }

    /**
     * Parses a pipe-delimited line into a Patient object.
     * Accepts either 6 tokens (if medical history is empty) or 7 tokens.
     *
     * @param line The line to parse.
     * @return A Patient object or null if the format is invalid.
     * @throws InvalidInputFormatException if any of the fields are invalid.
     */
    public static Patient parseLoadPatient(String line) throws InvalidInputFormatException {
        String[] tokens = line.split("\\|");
        if (tokens.length < 6 || tokens.length > 7) {
            return null;
        }

        String id = tokens[0];
        String name = tokens[1];
        String dobStr = tokens[2];
        String gender = tokens[3];
        String address = tokens[4];
        String contact = tokens[5];
        List<String> medHistory = new ArrayList<>();

        if (tokens.length == 7 && !tokens[6].isBlank()) {
            medHistory = Arrays.stream(tokens[6].split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        return new Patient(id, name, dobStr, gender, address, contact, medHistory);
    }

    /**
     * @return the patient NRIC
     */
    public String getId() {
        return id;
    }

    /**
     * @return the patient name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the patient's date of birth
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * @return the patient's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @return the patient's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the patient's contact number
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * @return the patient's medical history list
     */
    public List<String> getMedicalHistory() {
        return medicalHistory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the patient's list of appointments
     */
    public List<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Adds an appointment to the patient.
     *
     * @param appointment Appointment to be added.
     */
    public void addAppointment(Appointment appointment) {
        assert appointment != null : "Appointment cannot be null";
        assert appointment.getNric().equals(this.id) : "Appointment NRIC must match patient ID";
        appointments.add(appointment);
    }

    /**
     * Removes an appointment from the patient by appointment ID.
     *
     * @param apptId ID of the appointment to be removed.
     */
    public void deleteAppointment(String apptId) {
        assert apptId != null && !apptId.isBlank() : "Appointment ID cannot be null or blank";
        for (Appointment appt : appointments) {
            if (appt.getId().equals(apptId)) {
                appointments.remove(appt);
                break;
            }
        }
    }

    /**
     * Returns a string representation of the patient for display.
     *
     * @return Formatted patient details and appointments.
     */
    @Override
    public String toString() {
        String formattedMedicalHistory = medicalHistory.isEmpty() ? "None" : String.join(", ", medicalHistory);

        String result = String.format(
                "Patient NRIC: %s\n"
                        + "Name: %s\n"
                        + "Date of Birth: %s\n"
                        + "Gender: %s\n"
                        + "Address: %s\n"
                        + "Contact: %s\n"
                        + "Medical History: %s",
                id, name, dob.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                gender, address, contactInfo, formattedMedicalHistory);

        if (appointments.isEmpty()) {
            result += "\nAppointments: None";
        } else {
            result += "\nAppointments:";
            for (Appointment appt : appointments) {
                result += String.format(
                        "\n- [%s][%s]: %s (%s)",
                        appt.getId(),
                        appt.getStatusIcon(),
                        appt.getDateTime().format(Appointment.OUTPUT_FORMAT),
                        appt.getDescription());
            }
        }
        return result;
    }

    /**
     * Returns a formatted string for list display.
     *
     * @return Compact patient details for list view.
     */
    public String toStringForListView() {
        String result = String.format(
                "Patient NRIC: %s\n   "
                        + "Name: %s\n   "
                        + "Date of Birth: %s\n   "
                        + "Gender: %s\n   "
                        + "Address: %s\n   "
                        + "Contact: %s",
                id, name, dob.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), gender, address, contactInfo);

        if (medicalHistory.isEmpty()) {
            result += "\n   Medical History: None";
        } else {
            result += "\n   Medical History:";
            for (String h : medicalHistory) {
                result += "\n   - " + h;
            }
        }

        if (appointments.isEmpty()) {
            result += "\n   Appointments: None";
        } else {
            result += "\n   Appointments:";
            for (Appointment appt : appointments) {
                result += String.format(
                        "\n   - [%s][%s]: %s (%s)",
                        appt.getId(),
                        appt.getStatusIcon(),
                        appt.getDateTime().format(Appointment.OUTPUT_FORMAT),
                        appt.getDescription());
            }
        }
        return result;
    }

    /**
     * Returns a pipe-delimited string for file storage.
     *
     * @return File format string.
     */
    public String toFileFormat() {
        String history = String.join(", ", this.medicalHistory);
        return this.id + "|" + this.name + "|" + dob.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                + "|" + this.gender + "|" + this.address + "|" + this.contactInfo + "|" + history;
    }

    /**
     * Validates and parses the NRIC format.
     *
     * @param ic The NRIC string to validate.
     * @return A validated NRIC string.
     * @throws InvalidInputFormatException If the NRIC format is invalid.
     */
    private String parseValidIC(String ic) throws InvalidInputFormatException {
        if (ic == null || ic.length() != 9) {
            throw new InvalidInputFormatException("IC must be exactly 9 characters long.");
        }
        String prefix = ic.substring(0, 1).toUpperCase();
        String numberPart = ic.substring(1, 8);
        String suffix = ic.substring(8).toUpperCase();

        if (!prefix.matches("[STFGM]")) {
            throw new InvalidInputFormatException("IC must start with S, T, F, G, or M.");
        }
        if (!numberPart.matches("\\d{7}")) {
            throw new InvalidInputFormatException("IC must contain 7 digits after the prefix.");
        }
        if (!suffix.matches("[A-Z]")) {
            throw new InvalidInputFormatException("IC must end with an uppercase letter.");
        }
        return ic;
    }

    /**
     * Validates the gender input.
     *
     * @param gender Gender string to validate.
     * @return Validated gender.
     * @throws InvalidInputFormatException If the gender is not M or F.
     */
    private String checkGender(String gender) throws InvalidInputFormatException {
        if (gender.equals("M") || gender.equals("F")) {
            return gender;
        } else {
            throw new InvalidInputFormatException("The gender must be either M (male) or F (female)");
        }
    }

    /**
     * Parses and validates the date of birth string.
     *
     * @param dobStr Date of birth in yyyy-MM-dd format.
     * @return Parsed LocalDate object.
     * @throws InvalidInputFormatException If format is incorrect or date is in the future.
     */
    private LocalDate parseAndValidateDob(String dobStr) throws InvalidInputFormatException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate dob = LocalDate.parse(dobStr, formatter);
            if (dob.isAfter(LocalDate.now())) {
                throw new InvalidInputFormatException("Date of birth must be before the current date.");
            }
            return dob;
        } catch (DateTimeParseException e) {
            throw new InvalidInputFormatException("Invalid date format. Use yyyy-MM-dd.");
        }
    }

    /**
     * Validates and parses the contact number.
     *
     * @param contactInfo Contact number string.
     * @return Validated contact number.
     * @throws InvalidInputFormatException If not an 8-digit number.
     */
    private String parseContactInfo(String contactInfo) throws InvalidInputFormatException {
        try {
            if (contactInfo.length() != 8 || !contactInfo.matches("\\d+")) {
                throw new InvalidInputFormatException("Contact number must be 8 digits.");
            }
            return contactInfo;
        } catch (NullPointerException e) {
            throw new InvalidInputFormatException("Contact number cannot be null.");
        }
    }
}
