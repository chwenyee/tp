package manager;

import exception.PatientNotFoundException;
import exception.AppointmentClashException;
import exception.DuplicatePatientIDException;
import exception.InvalidInputFormatException;
import exception.UnloadedStorageException;
import miscellaneous.Ui;
import storage.Storage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The ManagementSystem class handles all business logic for the clinic management system.
 * It manages patients, appointments, and prescriptions, and coordinates with the storage system.
 * This class serves as the central component that maintains the state of the application
 * and provides operations to access and modify that state.
 */
public class ManagementSystem {
    private final List<Appointment> appointments;
    private final List<Patient> patients;
    private final List<Prescription> prescriptions;

    /**
     * Constructs a ManagementSystem with patients and appointments but no prescriptions.
     * 
     * @param loadedPatients The list of patients to initialize with
     * @param loadedAppointments The list of appointments to initialize with
     */
    public ManagementSystem(List<Patient> loadedPatients, List<Appointment> loadedAppointments) {
        assert loadedPatients != null : "Patient list cannot be null";
        assert loadedAppointments != null : "Appointment list cannot be null";
        appointments = loadedAppointments;
        patients = loadedPatients;
        prescriptions = new ArrayList<>();
    }

    /**
     * Constructs a ManagementSystem with patients, appointments, and prescriptions.
     * 
     * @param loadedPatients The list of patients to initialize with
     * @param loadedAppointments The list of appointments to initialize with
     * @param loadedPrescriptions The list of prescriptions to initialize with
     */
    public ManagementSystem(List<Patient> loadedPatients, List<Appointment> loadedAppointments,
                            List<Prescription> loadedPrescriptions) {
        assert loadedPatients != null : "Patient list cannot be null";
        assert loadedAppointments != null : "Appointment list cannot be null";
        assert loadedPrescriptions != null : "Prescription list cannot be null";
        appointments = loadedAppointments;
        patients = loadedPatients;
        prescriptions = loadedPrescriptions;
    }

    /**
     * Gets the list of all patients in the system.
     *
     * @return List of all Patient objects
     */
    public List<Patient> getPatients() {
        return patients;
    }

    /**
     * Updates the list of appointments in the system.
     * Replaces the entire appointment list with the provided one.
     *
     * @param appointments The new list of appointments to set
     */
    public void setAppointments(List<Appointment> appointments) {
        this.appointments.clear();
        this.appointments.addAll(appointments);
    }

    /**
     * Gets the list of all appointments in the system.
     *
     * @return List of all Appointment objects
     */
    public List<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Adds a new patient to the system.
     * Checks for duplicate NRICs before adding the patient.
     * Saves the updated patient list to storage after successful addition.
     *
     * @param patient The patient to add
     * @throws DuplicatePatientIDException If a patient with the same NRIC already exists
     * @throws UnloadedStorageException If there was an error saving to storage
     */
    public void addPatient(Patient patient) throws DuplicatePatientIDException, UnloadedStorageException {
        assert patient != null : "Patient cannot be null";
        assert patients != null : "Patient list cannot be null";

        for (Patient existingPatient : patients) {
            assert existingPatient != null : "Existing patient in list cannot be null";
            if (existingPatient.getId().equals(patient.getId())) {
                throw new DuplicatePatientIDException("Patient ID already exists!");
            }
        }
        patients.add(patient);
        Storage.savePatients(patients);
    }

    /**
     * Deletes a patient from the system by NRIC.
     * Also removes all appointments associated with the deleted patient.
     * Saves the updated patient and appointment lists to storage after successful deletion.
     *
     * @param nric The NRIC of the patient to delete
     * @return The deleted Patient object, or null if no patient was found with the given NRIC
     * @throws UnloadedStorageException If there was an error saving to storage
     */
    public Patient deletePatient(String nric) throws UnloadedStorageException {
        assert nric != null && !nric.isBlank() : "NRIC must not be null or blank";
        assert patients != null : "Patient list cannot be null";
        
        for (Patient patient : patients) {
            if (patient.getId().equals(nric)) {
                patients.remove(patient);
                // delete all appointments associated with a patient to be deleted
                appointments.removeIf(appointment -> appointment.getNric().equals(nric));
                Storage.savePatients(patients);
                Storage.saveAppointments(appointments);
                return patient;
            }
        }
        return null;
    }

    //@@author dylancmznus
    /**
     * Retrieves the patient object that matches the specified NRIC.
     *
     * <p>The method iterates through the list of existing patients to find a match
     * based on the NRIC. If a match is found, the corresponding Patient object is returned;
     * otherwise, it returns null.</p>
     *
     * @param nric The NRIC of the patient to be retrieved.
     * @return The Patient object matching the given NRIC, or null if no match is found.
     * @throws AssertionError if the input NRIC is null or blank.
     */
    public Patient viewPatient(String nric) {
        assert nric != null && !nric.isBlank() : "NRIC must not be null or blank";
        Patient matchedPatient = null;
        for (Patient patient : patients) {
            if (patient.getId().equals(nric)) {
                matchedPatient = patient;
                break;
            }
        }
        return matchedPatient;
    }


    /**
     * Edits an existing patient's information.
     * Updates only the fields that are provided (non-null).
     * Saves changes to storage after successful update.
     *
     * @param nric The NRIC of the patient to edit (cannot be changed)
     * @param newName New name for the patient, or null to keep unchanged
     * @param newDob New date of birth, or null to keep unchanged
     * @param newGender New gender, or null to keep unchanged
     * @param newAddress New address, or null to keep unchanged
     * @param newPhone New phone number, or null to keep unchanged
     * @throws UnloadedStorageException If there was an error saving to storage
     * @throws PatientNotFoundException If no patient with the given NRIC exists
     */
    //@@author jyukuan
    public void editPatient(String nric, String newName, String newDob, String newGender, String newAddress,
                            String newPhone) throws UnloadedStorageException, PatientNotFoundException,
            InvalidInputFormatException {

        assert nric != null && !nric.isBlank() : "NRIC must not be null or blank";
        assert patients != null : "Patient list cannot be null";

        Patient patient = findPatientByNric(nric);
        if (patient == null) {
            throw new PatientNotFoundException("Patient with NRIC " + nric + " not found.");
        }
        if (newName != null && !newName.isBlank()) {
            patient.setName(newName);
        }
        if (newDob != null && !newDob.isBlank()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate parsedDob = LocalDate.parse(newDob, formatter);
                if (parsedDob.isAfter(LocalDate.now())) {
                    throw new InvalidInputFormatException("Date of birth must be before today.");
                }
                patient.setDob(parsedDob);
            } catch (DateTimeParseException e) {
                throw new InvalidInputFormatException("Invalid date format. Use yyyy-MM-dd.");
            }
        }

        if (newGender != null && !newGender.isBlank()) {
            patient.setGender(newGender);
        }
        if (newAddress != null && !newAddress.isBlank()) {
            patient.setAddress(newAddress);
        }
        if (newPhone != null && !newPhone.isBlank()) {
            patient.setContactInfo(newPhone);
        }
        Storage.savePatients(patients);
        System.out.println("Patient with NRIC " + nric + " updated successfully.");
    }

    /**
     * Stores new medical history entries for a patient.
     * If an entry already exists in the patient's history, it will not be duplicated.
     * Saves the updated patient list to storage.
     *
     * @param nric The NRIC of the patient
     * @param medHistory Comma-separated medical history entries to add
     * @throws PatientNotFoundException If no patient with the given NRIC exists
     * @throws UnloadedStorageException If there was an error saving to storage
     */
    public void storeMedicalHistory(String nric, String medHistory) throws PatientNotFoundException,
            UnloadedStorageException {
        Patient existingPatient = findPatientByNric(nric);

        assert nric != null && !nric.isBlank() : "NRIC must not be null or blank";
        assert medHistory != null && !medHistory.isBlank() : "Medical history must not be null or blank";


        if (existingPatient == null) {
            throw new PatientNotFoundException("Patient with NRIC not found. Patient's history can not be added");
        } else {
            Ui.showLine();
        }

        String[] historyEntries = medHistory.split(",\\s*");
        for (String entry : historyEntries) {
            if (!existingPatient.getMedicalHistory().contains(entry.trim())) {
                existingPatient.getMedicalHistory().add(entry.trim());
            }
        }
        Storage.savePatients(patients);
        System.out.println("Medical history added for patient with NRIC: " + nric + ".");
        Ui.showLine();
    }


    /**
     * Views the medical history for a single patient identified by NRIC,
     * and displays it to the user via the Ui component.
     *
     * @param nric The NRIC of the patient whose medical history should be displayed.
     * @throws PatientNotFoundException if no patient with the specified NRIC is found.
     */
    public void viewMedicalHistoryByNric(String nric) throws PatientNotFoundException {
        Patient foundPatients = findPatientByNric(nric.trim());

        if (foundPatients == null) {
            throw new PatientNotFoundException("No patient/patients found with NRIC " + nric + ".");
        } else {
            Ui.showLine();
            Ui.showPatientHistory(foundPatients);
        }
    }

    /**
     * Finds one or more patients by name, then displays each patient's medical history.
     * If multiple patients share the same name, all their histories are shown.
     *
     * @param name The name of the patient(s) whose medical history should be displayed.
     */
    public void viewMedicalHistoryByName(String name) {
        List<Patient> foundPatients = findPatientsByName(name.trim());

        Ui.showLine();

        if (foundPatients.isEmpty()) {
            System.out.println("No patients found with name '" + name + "'.");
            Ui.showLine();
        } else {
            System.out.println("Found " + foundPatients.size() + " patient(s) with name '" + name + "'");
            for (Patient p : foundPatients) {
                Ui.showPatientHistory(p);
            }
        }
    }

    /**
     * Edits a specific medical history entry for a patient.
     * Replaces the old history text with new text if the old text is found.
     * 
     * @param nric The patient's unique identifier
     * @param oldHistory The existing history text to be replaced
     * @param newHistory The new history text to replace it with
     * @throws UnloadedStorageException If there was an error saving to storage
     */
    public void editPatientHistory(String nric, String oldHistory, String newHistory) throws UnloadedStorageException {

        assert nric != null && !nric.isBlank() : "NRIC must not be null or blank";
        assert oldHistory != null && !oldHistory.isBlank() : "Old history must not be blank";
        assert newHistory != null && !newHistory.isBlank() : "New history must not be blank";

        Patient patient = findPatientByNric(nric);
        if (patient == null) {
            System.out.println("Patient with NRIC " + nric + " not found.");
            return;
        }
        List<String> histories = patient.getMedicalHistory();
        boolean foundOld = false;
        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).equalsIgnoreCase(oldHistory.trim())) {
                histories.set(i, newHistory.trim());
                foundOld = true;
                System.out.println("Replaced old history \"" + oldHistory + "\" with \"" + newHistory + "\".");
                break;
            }
        }
        Storage.savePatients(patients);
        if (!foundOld) {
            System.out.println("Old history \"" + oldHistory + "\" not found for patient " + patient.getName());
        }
    }


    /**
     * Finds a patient by their NRIC, ignoring case.
     *
     * @param nric The NRIC string to search for.
     * @return The Patient object if found, or null if no matching patient is found.
     */
    public Patient findPatientByNric(String nric) {
        String object = nric.trim().toUpperCase();
        for (Patient p : patients) {
            String patientId = p.getId().trim().toUpperCase();
            if (patientId.equals(object)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Finds a list of patients who have a matching name (case-insensitive).
     *
     * @param name The name string to search for.
     * @return A List of Patient objects that match the given name (could be empty if none found).
     */
    private List<Patient> findPatientsByName(String name) {
        List<Patient> result = new ArrayList<>();
        for (Patient p : patients) {
            if (p.getName().trim().equalsIgnoreCase(name)) {
                result.add(p);
            }
        }
        return result;
    }

    //@@author chwenyee
    /**
     * Adds a new appointment to the system.
     * Checks for appointment clashes (within 1 hour) and verifies the patient exists.
     * Saves the updated appointment list to storage after successful addition.
     *
     * @param appointment The appointment to add
     * @throws UnloadedStorageException If there was an error saving to storage
     * @throws PatientNotFoundException If the patient associated with the appointment doesn't exist
     * @throws AppointmentClashException If the appointment clashes with an existing appointment
     */
    public void addAppointment(Appointment appointment) throws UnloadedStorageException, PatientNotFoundException,
            AppointmentClashException {
        assert appointment != null : "Appointment cannot be null";
        assert patients != null : "Patient list cannot be null";

        // Check if there is any scheduled appointment in the list clashing with this newly-added one
        for (Appointment appointmentInList : appointments)  {
            long timeDiff = Math.abs(Duration.between(appointmentInList.getDateTime(),
                    appointment.getDateTime()).toMinutes());
            if (timeDiff < 60) {
                throw new AppointmentClashException("This appointment clashes with another scheduled within 1 hour.");
            }
        }

        Patient patient = findPatientByNric(appointment.getNric());
        if (patient == null) {
            throw new PatientNotFoundException("Patient with NRIC: " + appointment.getNric() + " not found");
        }

        appointments.add(appointment);
        patient.addAppointment(appointment);
        Storage.saveAppointments(appointments);
    }

    /**
     * Deletes an appointment from the system by appointment ID.
     * Also removes the appointment from the patient's appointment list.
     * Saves the updated appointment list to storage after successful deletion.
     *
     * @param apptId The ID of the appointment to delete
     * @return The deleted Appointment object, or null if no appointment was found with the given ID
     * @throws UnloadedStorageException If there was an error saving to storage
     */
    public Appointment deleteAppointment(String apptId) throws UnloadedStorageException {
        assert apptId != null && !apptId.isBlank() : "Appointment ID cannot be null or blank";
        assert appointments != null : "Appointment list cannot be null";
        
        for (Appointment appointment : appointments) {
            if (appointment.getId().equalsIgnoreCase(apptId)) {
                appointments.remove(appointment);
                Patient patient = findPatientByNric(appointment.getNric());
                if (patient != null) {
                    patient.deleteAppointment(apptId);
                    Storage.saveAppointments(appointments);
                }
                return appointment;
            }
        }
        return null;
    }

    /**
     * Sorts a list of appointments by date and time.
     * Orders appointments chronologically from earliest to latest.
     *
     * @param appointments The list of appointments to sort
     * @return The sorted list of appointments
     */
    public List<Appointment> sortAppointmentsByDateTime(List<Appointment> appointments) {
        appointments.sort(Comparator.comparing(Appointment::getDateTime));
        return appointments;
    }

    /**
     * Sorts a list of appointments by their ID.
     * Orders appointments alphanumerically based on their appointment ID.
     *
     * @param appointments The list of appointments to sort
     * @return The sorted list of appointments
     */
    public List<Appointment> sortAppointmentsById(List<Appointment> appointments) {
        appointments.sort(Comparator.comparing(Appointment::getId));
        return appointments;
    }

    /**
     * Marks an appointment as completed.
     * Updates the appointment status and saves changes to storage.
     *
     * @param apptId The ID of the appointment to mark as done
     * @return The updated appointment, or null if no appointment with the given ID was found
     * @throws UnloadedStorageException If there was an error saving to storage
     */
    public Appointment markAppointment(String apptId) throws UnloadedStorageException {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equalsIgnoreCase(apptId)) {
                appointment.markAsDone();
                Storage.saveAppointments(appointments);
                return appointment;
            }
        }
        return null;
    }

    /**
     * Unmarks a previously completed appointment.
     * Updates the appointment status and saves changes to storage.
     *
     * @param apptId The ID of the appointment to unmark
     * @return The updated appointment, or null if no appointment with the given ID was found
     * @throws UnloadedStorageException If there was an error saving to storage
     */
    public Appointment unmarkAppointment(String apptId) throws UnloadedStorageException {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equalsIgnoreCase(apptId)) {
                appointment.unmarkAsDone();
                Storage.saveAppointments(appointments);
                return appointment;
            }
        }
        return null;
    }


    /**
     * Finds all appointments associated with the specified NRIC.
     *
     * <p>This method searches through the list of stored appointments and collects
     * all appointments that match the given NRIC exactly.</p>
     *
     * @param nric The NRIC used to search for matching appointments.
     * @return A list of appointments that are associated with the provided NRIC.
     */
    public List<Appointment> findAppointmentsByNric(String nric) {
        List<Appointment> matchingAppointments = new ArrayList<>();
        for (Appointment appt : appointments) {
            if (appt.getNric().equals(nric)) {
                matchingAppointments.add(appt);
            }
        }
        return matchingAppointments;
    }

    //@@author Basudeb2005
    /**
     * Gets all prescriptions stored in the system.
     *
     * @return A list of all prescription records
     */
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    //@@author Basudeb2005
    /**
     * Adds a new prescription to the system.
     * Verifies the patient exists before adding the prescription.
     * Generates a unique prescription ID based on patient ID and prescription count.
     * Saves the updated prescription list to storage.
     *
     * @param prescription The prescription to add (without final ID)
     * @return The newly created prescription with final ID
     * @throws IllegalArgumentException If the patient doesn't exist or maximum prescriptions reached
     * @throws UnloadedStorageException If there was an error saving to storage
     */
    public Prescription addPrescription(Prescription prescription) 
            throws IllegalArgumentException, UnloadedStorageException {
        assert prescription != null : "Prescription cannot be null";
        assert patients != null : "Patient list cannot be null";
        
        Patient patient = findPatientByNric(prescription.getPatientId());
        if (patient == null) {
            throw new IllegalArgumentException("Patient with NRIC: " + prescription.getPatientId() + " not found");
        }

        // Generate prescription ID with counter
        int prescriptionCount = 1;
        for (Prescription p : prescriptions) {
            if (p.getPatientId().equals(prescription.getPatientId())) {
                prescriptionCount++;
            }
        }
        
        // Check for maximum prescription limit
        final int MAX_PRESCRIPTIONS_PER_PATIENT = 100; // Reasonable upper limit
        if (prescriptionCount > MAX_PRESCRIPTIONS_PER_PATIENT) {
            throw new IllegalArgumentException("Maximum number of prescriptions (" + 
                                              MAX_PRESCRIPTIONS_PER_PATIENT + 
                                              ") reached for patient: " + prescription.getPatientId());
        }
        
        String prescriptionId = prescription.getPatientId() + "-" + prescriptionCount;
        
        // Create a new prescription with updated ID
        Prescription newPrescription = new Prescription(
            prescription.getPatientId(),
            prescriptionId,
            prescription.getTimestamp(),
            prescription.getSymptoms(),
            prescription.getMedicines(),
            prescription.getNotes()
        );
        
        prescriptions.add(newPrescription);
        
        try {
            Storage.savePrescriptions(prescriptions);
        } catch (UnloadedStorageException e) {
            // Roll back the addition if saving fails
            prescriptions.remove(newPrescription);
            throw e; // Re-throw to notify the caller
        }
        
        return newPrescription;
    }

    //@@author Basudeb2005
    /**
     * Retrieves all prescriptions for a specific patient.
     * Filters the complete prescription list to find those matching the provided patient ID.
     *
     * @param patientId The unique identifier of the patient
     * @return A list of prescriptions for the specified patient (may be empty if none found)
     */
    public List<Prescription> getPrescriptionsForPatient(String patientId) {
        List<Prescription> patientPrescriptions = new ArrayList<>();
        for (Prescription prescription : prescriptions) {
            if (prescription.getPatientId().equals(patientId)) {
                patientPrescriptions.add(prescription);
            }
        }
        return patientPrescriptions;
    }

    //@@author Basudeb2005
    /**
     * Finds a specific prescription by its unique identifier.
     *
     * @param prescriptionId The unique ID of the prescription to find
     * @return The prescription if found, or null if no matching prescription exists
     */
    public Prescription getPrescriptionById(String prescriptionId) {
        for (Prescription prescription : prescriptions) {
            if (prescription.getPrescriptionId().equals(prescriptionId)) {
                return prescription;
            }
        }
        return null;
    }

}
