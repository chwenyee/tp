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

public class Storage {
    private static String directoryPath;
    private static String patientFilePath;
    private static String appointmentFilePath;
    private static String prescriptionFilePath;
    private static String prescriptionDirPath;
    private static Patient patients;

    public Storage(String directory) {
        directoryPath = directory;
        patientFilePath = directory + File.separator + "patient_data.txt";
        appointmentFilePath = directory + File.separator + "appointment_data.txt";
        prescriptionFilePath = directory + File.separator + "prescription_data.txt";
        prescriptionDirPath = directory + File.separator + "prescriptions";
    }

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
