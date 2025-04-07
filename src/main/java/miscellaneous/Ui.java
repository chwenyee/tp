package miscellaneous;

import manager.Appointment;
import manager.Patient;


import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * The Ui class handles all user interaction for the clinic management system.
 * It provides methods to display information to the user and collect input.
 * This class is responsible for formatting output in a consistent way and
 * providing a clean interface between the system and the user.
 */
public class Ui {
    public static final String DIVIDER = "-".repeat(100);
    public static final DateTimeFormatter OUTPUT_TIME_FORMAT = DateTimeFormatter.ofPattern("h:mm a");

    private final Scanner sc;

    public Ui() {
        sc = new Scanner(System.in);
    }

    public static void showLine() {
        System.out.println(DIVIDER);
    }

    public void showWelcome() {
        showLine();
        System.out.println("Welcome to ClinicEase!");
        System.out.println("Type a command, or 'bye' to exit.");
        showLine();
    }

    public void showBye() {
        showLine();
        System.out.println("Goodbye!");
        showLine();
    }

    public String readCommand() {
        System.out.print("> ");
        return sc.nextLine().trim();
    }

    public void showError(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    public void showHelp() {
        showLine();
        System.out.println("These are the available commands:");
        System.out.println("Patient: ");
        System.out.println("- add-patient n/NAME ic/NRIC dob/BIRTHDATE g/GENDER p/PHONE a/ADDRESS");
        System.out.println("- delete-patient NRIC");
        System.out.println("- edit-patient ic/NRIC [n/NAME] [dob/BIRTHDATE] [g/GENDER] [a/ADDRESS] [p/PHONE]");
        System.out.println("- list-patient");
        System.out.println("- view-patient NRIC");
        System.out.println("- store-history n/NAME ic/NRIC h/MEDICAL_HISTORY");
        System.out.println("- view-history NRIC or view-history NAME");
        System.out.println("- edit-history ic/NRIC old/OLD_TEXT new/NEW_TEXT");
        System.out.println("Appointment: ");
        System.out.println("- add-appointment ic/NRIC dt/DATE t/TIME dsc/DESCRIPTION");
        System.out.println("- delete-appointment APPOINTMENT_ID");
        System.out.println("- list-appointment");
        System.out.println("- mark-appointment APPOINTMENT_ID");
        System.out.println("- unmark-appointment APPOINTMENT_ID");
        System.out.println("- sort-appointment byDate or sort-appointment byId");
        System.out.println("- find-appointment PATIENT_NRIC");
        System.out.println("Prescription: ");
        System.out.println("- add-prescription ic/PATIENT_ID s/SYMPTOMS m/MEDICINES [nt/NOTES]");
        System.out.println("- view-all-prescriptions PATIENT_ID");
        System.out.println("- view-prescription PRESCRIPTION_ID");
        showLine();
    }

    //@@author judHoka
    public void showPatientAdded(List<Patient> patients) {
        showLine();
        System.out.println("Patient added successfully: " + patients.get(patients.size() - 1).getName());
        showLine();
    }

    public void showPatientDeleted(Patient removedPatient, String nric) {
        if (removedPatient == null) {
            showLine();
            System.out.println("Patient with NRIC " + nric + " not found.");
            showLine();
            return;
        }
        showLine();
        System.out.println("Patient deleted successfully: " + removedPatient.getName());
        showLine();
    }

    //@@author dylancmznus
    public void showPatientViewed(Patient matchedPatient, String nric) {
        if (matchedPatient == null) {
            showLine();
            System.out.println("Patient with NRIC " + nric + " not found.");
            showLine();
            return;
        }
        System.out.println("-".repeat(42) + "Patient Details" + "-".repeat(42));
        System.out.println(matchedPatient);
        showLine();
    }

    //@@author judHoka
    public void showPatientList(List<Patient> patients) {
        if (patients.isEmpty()) {
            showLine();
            System.out.println("No patients have been added.");
            showLine();
            return;
        }

        System.out.println("-".repeat(42)+ "Patient Details" + "-".repeat(42));

        int count = 1;
        for (Patient p : patients) {
            System.out.println(count + ". " + p.toStringForListView());
            showLine();
            count++;
        }
    }

    //@@author jyukuan
    public static void showPatientHistory(Patient patient) {
        System.out.println("Medical History for " + patient.getName() + " (NRIC: " + patient.getId() + "):");
        List<String> histories = patient.getMedicalHistory();
        if (histories.isEmpty()) {
            System.out.println("No medical history recorded.");
        } else {
            for (String h : histories) {
                System.out.println("- " + h);
            }
            showLine();
        }
    }

    //@@author chwenyee
    public void showAppointmentAdded(List<Appointment> appointments) {
        Appointment currentAppointment = appointments.get(appointments.size() - 1);

        showLine();
        System.out.println("Appointment added for NRIC: " + currentAppointment.getNric() + " on "
                + currentAppointment.getDate() + " at " + currentAppointment.getTime().format(OUTPUT_TIME_FORMAT)
                + ".");
        System.out.println("Now you have " + appointments.size() + " appointment(s) in the list.");
        showLine();
    }

    public void showAppointmentDeleted(List<Appointment> appointments, Appointment removedAppointment, String apptId) {
        if (removedAppointment == null) {
            showLine();
            System.out.println("No appointment found with ID: " + apptId + ".");
            showLine();
            return;
        }

        showLine();
        System.out.println("Appointment " + apptId + " is deleted successfully.");
        System.out.println("Now you have " + appointments.size() + " appointment(s) in the list.");
        showLine();
    }

    //@@author dylancmznus
    public void showAppointmentMarked(List<Appointment> appointments, Appointment markedAppointment, String apptId) {
        if (markedAppointment == null) {
            showLine();
            System.out.println("No appointment found with ID: " + apptId + ".");
            showLine();
            return;
        }

        showLine();
        System.out.println("Appointment " + apptId + " is marked successfully.");
        showLine();
    }

    public void showAppointmentUnmarked(List<Appointment> appointments, Appointment markedAppointment, String apptId) {
        if (markedAppointment == null) {
            showLine();
            System.out.println("No appointment found with ID: " + apptId + ".");
            showLine();
            return;
        }

        showLine();
        System.out.println("Appointment " + apptId + " is unmarked successfully.");
        showLine();
    }

    //@@author Basudeb2005
    public void showAppointmentList(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            showLine();
            System.out.println("No appointments found.");
            showLine();
            return;
        }

        System.out.println("-".repeat(43)+ "Appointments" + "-".repeat(45));
        int count = 1;
        for (Appointment a : appointments) {
            System.out.println(count + ". " + a);
            count++;
        }
        showLine();
    }

    //@@author dylancmznus
    public void showAppointmentsFound(List<Appointment> appointments, String nric) {
        showLine();
        if (appointments.isEmpty()) {
            System.out.println(" No appointments found for NRIC: " + nric);
        } else {
            System.out.println(" Appointments found for NRIC: " + nric);
            for (Appointment appt : appointments) {
                System.out.println(" - " + appt);
            }
        }
        showLine();
    }
}
