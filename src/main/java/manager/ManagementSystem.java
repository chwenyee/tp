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

public class ManagementSystem {
    private final List<Appointment> appointments;
    private final List<Patient> patients;
    private final List<Prescription> prescriptions;

    public ManagementSystem(List<Patient> loadedPatients, List<Appointment> loadedAppointments) {
        assert loadedPatients != null : "Patient list cannot be null";
        assert loadedAppointments != null : "Appointment list cannot be null";
        appointments = loadedAppointments;
        patients = loadedPatients;
        prescriptions = new ArrayList<>();
    }

    public ManagementSystem(List<Patient> loadedPatients, List<Appointment> loadedAppointments,
                            List<Prescription> loadedPrescriptions) {
        assert loadedPatients != null : "Patient list cannot be null";
        assert loadedAppointments != null : "Appointment list cannot be null";
        assert loadedPrescriptions != null : "Prescription list cannot be null";
        appointments = loadedAppointments;
        patients = loadedPatients;
        prescriptions = loadedPrescriptions;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments.clear();
        this.appointments.addAll(appointments);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

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
     * Edits the information of an existing patient. Updates only the fields
     * that are not null or blank in the provided parameters.
     *
     * @param nric      The NRIC of the patient to edit.
     * @param newName   The new name to set. (Pass null or blank to leave it unchanged)
     * @param newDob    The new date of birth in "yyyy-MM-dd" format. (null/blank to skip)
     * @param newGender The new gender (e.g., "M" or "F"). (null/blank to skip)
     * @param newAddress The new address. (null/blank to skip)
     * @param newPhone  The new phone number. (null/blank to skip)
     * @throws UnloadedStorageException  If saving the edited patient data to storage fails.
     * @throws PatientNotFoundException  If no patient is found with the given NRIC.
     * @throws InvalidInputFormatException If the newDob string is invalid or
     *                                     if the parsed date is after the current date.
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
     * Stores new medical history entries for the patient with the specified NRIC.
     * Entries can be comma-separated, and each unique entry is appended to
     * the patient's existing medical history if not already present.
     *
     * @param nric The NRIC of the patient whose medical history is to be updated.
     * @param medHistory A comma-separated list of new medical history entries.
     * @throws PatientNotFoundException if no patient matches the given NRIC.
     * @throws UnloadedStorageException if saving to storage fails.
     */
    public void storeMedicalHistory(String nric, String medHistory) throws PatientNotFoundException,
            UnloadedStorageException {
        Patient existingPatient = findPatientByNric(nric);

        if (existingPatient == null) {
            throw new PatientNotFoundException("Patient with NRIC " + nric + " not found. Cannot add history.");
        }

        String[] historyEntries = medHistory.split(",\\s*");
        for (String entry : historyEntries) {
            if (!existingPatient.getMedicalHistory().contains(entry.trim())) {
                existingPatient.getMedicalHistory().add(entry.trim());
            }
        }

        Storage.savePatients(patients);
        Ui.showLine();
        System.out.println("Medical history added for " + existingPatient.getName() + " (NRIC: " + nric + ").");
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
     * Replaces an existing medical history entry for a given patient with new content.
     *
     * @param nric The NRIC of the patient whose medical history will be edited.
     * @param oldHistory The old history text to be replaced.
     * @param newHistory The new history text to replace with.
     * @throws UnloadedStorageException if saving the updated data to storage fails.
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
    public void addAppointment(Appointment appointment) throws UnloadedStorageException, PatientNotFoundException,
            AppointmentClashException {
        assert appointment != null : "Appointment cannot be null";
        assert patients != null : "Patient list cannot be null";

        // Check if there is any scheduled appointment in the list clashing with this newly-added one
        for (Appointment appointmentInList : appointments) {
            long timeDiff = Math.abs(Duration.between(appointmentInList.getDateTime(),
                    appointment.getDateTime()).toMinutes());
            if (timeDiff <= 30) {
                throw new AppointmentClashException
                ("This appointment clashes with another scheduled within 30 minutes.");
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

    public List<Appointment> sortAppointmentsByDateTime(List<Appointment> appointments) {
        appointments.sort(Comparator.comparing(Appointment::getDateTime));
        return appointments;
    }

    public List<Appointment> sortAppointmentsById(List<Appointment> appointments) {
        appointments.sort(Comparator.comparing(Appointment::getId));
        return appointments;
    }

    //@@author dylancmznus
    /**
     * Marks the appointment with the given appointment ID as done.
     *
     * <p>The method searches through the list of appointments to find the one that matches
     * the given ID (case-insensitive). If found, it marks the appointment as done and saves
     * the updated list to storage.</p>
     *
     * @param apptId The ID of the appointment to be marked as done.
     * @return The Appointment object that was marked, or null if no match is found.
     * @throws UnloadedStorageException If the storage system has not been properly initialized.
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
     * Unmarks the appointment with the given appointment ID as not done.
     *
     * <p>This method searches through the list of appointments to find the one that matches
     * the given ID (case-insensitive). If found, it unmarks the appointment and saves
     * the updated list to storage.</p>
     *
     * @param apptId The ID of the appointment to be unmarked.
     * @return The Appointment object that was unmarked, or null if no match is found.
     * @throws UnloadedStorageException If the storage system has not been properly initialized.
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
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    //@@author Basudeb2005
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
        Storage.savePrescriptions(prescriptions);

        return newPrescription;
    }

    //@@author Basudeb2005
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
    public Prescription getPrescriptionById(String prescriptionId) {
        for (Prescription prescription : prescriptions) {
            if (prescription.getPrescriptionId().equals(prescriptionId)) {
                return prescription;
            }
        }
        return null;
    }

}
