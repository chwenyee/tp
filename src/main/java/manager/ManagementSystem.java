package manager;

import exception.DuplicatePatientIDException;
import exception.UnloadedStorageException;
import miscellaneous.Ui;
import storage.Storage;

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
                Storage.savePatients(patients);
                return patient;
            }
        }
        return null;
    }

    //@@author dylancmznus
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

    //@@author jyukuan
    public void editPatient(String nric, String newName, String newDob, String newGender,
                            String newAddress, String newPhone) throws UnloadedStorageException {
        assert nric != null && !nric.isBlank() : "NRIC must not be null or blank";
        assert patients != null : "Patient list cannot be null";

        Patient patient = findPatientByNric(nric);
        if (patient == null) {
            System.out.println("Patient with NRIC " + nric + " not found.");
            return;
        }
        if (newName != null && !newName.isBlank()) {
            patient.setName(newName);
        }
        if (newDob != null && !newDob.isBlank()) {
            patient.setDob(newDob);
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

    public void storeMedicalHistory(String name, String nric, String medHistory) throws UnloadedStorageException{
        Patient existingPatient = findPatientByNric(nric);

        assert name != null && !name.isBlank() : "Name must not be null or blank";
        assert nric != null && !nric.isBlank() : "NRIC must not be null or blank";
        assert medHistory != null && !medHistory.isBlank() : "Medical history must not be null or blank";


        if (existingPatient == null) {
            throw new UnloadedStorageException("Patient with NRIC not found. Patient's history can not be added");
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
        System.out.println("Medical history added for " + name + " (NRIC: " + nric + ").");
        Ui.showLine();
    }

    public void viewMedicalHistoryByNric(String nric) {
        Patient foundPatients = findPatientByNric(nric.trim());

        if (foundPatients == null) {
            Ui.showLine();
            System.out.println("No patient/patients found with NRIC " + nric + ".");
        } else {
            Ui.showLine();
            Ui.showPatientHistory(foundPatients);
        }
    }

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

    private Patient findPatientByNric(String nric) {
        String object = nric.trim().toUpperCase();
        for (Patient p : patients) {
            String patientId = p.getId().trim().toUpperCase();
            if (patientId.equals(object)) {
                return p;
            }
        }
        return null;
    }

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
    public void addAppointment(Appointment appointment) throws IllegalArgumentException, UnloadedStorageException {
        assert appointment != null : "Appointment cannot be null";
        assert patients != null : "Patient list cannot be null";
        
        Patient patient = findPatientByNric(appointment.getNric());
        if (patient == null) {
            throw new IllegalArgumentException("Patient with NRIC: " + appointment.getNric() + " not found");
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

    public Appointment findAppointmentByNric(String nric) {
        for (Appointment appt : appointments) {
            if (appt.getNric().equals(nric)) {
                return appt;
            }
        }
        return null;
    }

    //@@author Basudeb2005
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }
    
    //@@author Basudeb2005
    public void addPrescription(Prescription prescription) 
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
        
        return;
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
