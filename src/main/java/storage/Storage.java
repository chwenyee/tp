package storage;

import exception.UnloadedStorageException;
import manager.Appointment;
import manager.Patient;
import manager.Prescription;
import miscellaneous.Parser;
import manager.ManagementSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles saving and loading of patient, appointment, and prescription data
 * to and from the local file system.
 */
public class Storage {
    private static String directoryPath;
    private static String patientFilePath;
    private static String appointmentFilePath;
    private static String prescriptionFilePath;
    private static String prescriptionDirPath;
    private static Patient patients;

    /**
     * Initializes storage paths based on a given directory.
     *
     * @param directory The directory to store data files in.
     */
    public Storage(String directory) {
        directoryPath = directory;
        patientFilePath = directory + File.separator + "patient_data.txt";
        appointmentFilePath = directory + File.separator + "appointment_data.txt";
        prescriptionFilePath = directory + File.separator + "prescription_data.txt";
        prescriptionDirPath = directory + File.separator + "prescriptions";
    }

    /**
     * Saves a list of patients to file.
     *
     * @param patientList The list of patients to save.
     * @throws UnloadedStorageException If storage is not initialized.
     */
    public static void savePatients(List<Patient> patientList) throws UnloadedStorageException {
        if (directoryPath == null || patientFilePath == null) {
            throw new UnloadedStorageException("Storage not initialized with a directory!");
        }

        File dir = new File(directoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(patientFilePath))) {
            for (Patient patient : patientList) {
                writer.write(patient.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UnloadedStorageException("Unable to save the patient!");
        }
    }

    /**
     * Loads patients from file.
     *
     * @return A list of patients.
     * @throws UnloadedStorageException If patient data cannot be loaded.
     */
    public static List<Patient> loadPatients() throws UnloadedStorageException {
        List<Patient> patients = new ArrayList<>();
        File file = new File(patientFilePath);
        if (!file.exists()) {
            return patients;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Patient patient = Parser.parseLoadPatient(line);
                if (patient != null) {
                    patients.add(patient);
                }
            }
        } catch (Exception e) {
            throw new UnloadedStorageException("Unable to load patient data!");
        }

        return patients;
    }

    /**
     * Saves a list of appointments to file.
     *
     * @param appointmentList The list of appointments to save.
     * @throws UnloadedStorageException If storage is not initialized.
     */
    public static void saveAppointments(List<Appointment> appointmentList) throws UnloadedStorageException {
        if (directoryPath == null || appointmentFilePath == null) {
            throw new UnloadedStorageException("Storage not initialized with a directory!");
        }

        File dir = new File(directoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(appointmentFilePath))) {
            writer.write("countId:" + Appointment.getRunningId());
            writer.newLine();

            for (Appointment appointment : appointmentList) {
                writer.write(appointment.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UnloadedStorageException("Unable to save the appointment!");
        }
    }

    /**
     * Loads appointments from file and links them to their corresponding patients.
     *
     * @param system The management system containing the patient list.
     * @return A list of appointments.
     * @throws UnloadedStorageException If appointment data cannot be loaded.
     */
    public static List<Appointment> loadAppointments(ManagementSystem system) throws UnloadedStorageException {
        List<Appointment> appointments = new ArrayList<>();
        File file = new File(appointmentFilePath);

        if (!file.exists()) {
            return appointments;
        }

        try (Scanner scanner = new Scanner(file)) {
            int countId = 100;

            if (scanner.hasNextLine()) {
                String firstLine = scanner.nextLine();
                if (firstLine.startsWith("countId:")) {
                    String[] parts = firstLine.split(":");
                    if (parts.length == 2) {
                        countId = Integer.parseInt(parts[1].trim());
                    }
                }
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    Appointment appointment = Parser.parseLoadAppointment(line);
                    if (appointment != null) {
                        appointments.add(appointment);

                        Patient patient = system.findPatientByNric(appointment.getNric());
                        if (patient != null) {
                            patient.addAppointment(appointment);
                        }
                    }
                }
            }

            Appointment.setRunningId(countId);

        } catch (Exception e) {
            throw new UnloadedStorageException("Unable to load appointment data!");
        }

        return appointments;
    }

    /**
     * Saves a list of prescriptions to file.
     *
     * @param prescriptionList The list of prescriptions to save.
     * @throws UnloadedStorageException If storage is not initialized.
     */
    public static void savePrescriptions(List<Prescription> prescriptionList) throws UnloadedStorageException {
        if (directoryPath == null || prescriptionFilePath == null) {
            throw new UnloadedStorageException("Storage not initialized with a directory!");
        }

        File dir = new File(directoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(prescriptionFilePath))) {
            for (Prescription prescription : prescriptionList) {
                writer.write(prescription.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UnloadedStorageException("Unable to save prescriptions!");
        }
    }

    /**
     * Loads prescriptions from file.
     *
     * @return A list of prescriptions.
     * @throws UnloadedStorageException If prescription data cannot be loaded.
     */
    public static List<Prescription> loadPrescriptions() throws UnloadedStorageException {
        List<Prescription> prescriptions = new ArrayList<>();
        File file = new File(prescriptionFilePath);

        if (!file.exists()) {
            return prescriptions;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    Prescription prescription = Prescription.fromFileFormat(line);
                    if (prescription != null) {
                        prescriptions.add(prescription);
                    }
                }
            }
        } catch (Exception e) {
            throw new UnloadedStorageException("Unable to load prescription data: " + e.getMessage());
        }

        return prescriptions;
    }

    /**
     * Generates and saves a prescription HTML file.
     *
     * @param prescription The prescription to convert to HTML.
     * @param patient      The patient associated with the prescription.
     * @throws UnloadedStorageException If storage is not initialized or HTML file cannot be written.
     */
    public static void savePrescriptionHtml(Prescription prescription, Patient patient)
            throws UnloadedStorageException {
        if (directoryPath == null || prescriptionDirPath == null) {
            throw new UnloadedStorageException("Storage not initialized with a directory!");
        }

        File dir = new File(prescriptionDirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = "prescription_" + prescription.getPatientId() + "_"
                + prescription.getPrescriptionId().split("-")[1] + ".html";
        String filePath = prescriptionDirPath + File.separator + fileName;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(prescription.generateHtml(patient));
        } catch (IOException e) {
            throw new UnloadedStorageException("Unable to generate HTML prescription: " + e.getMessage());
        }
    }
}
