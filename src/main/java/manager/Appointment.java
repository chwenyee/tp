package manager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an appointment in the clinic management system.
 * Contains information about the patient (NRIC), appointment date/time,
 * description, and completion status.
 */
public class Appointment {

    public static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    public static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");

    private static int runningId = 100;
    private final String id;
    private final String nric;
    private final LocalDateTime dateTime;
    private final String description;
    private boolean isDone;

    /**
     * Constructs a new Appointment with auto-generated ID.
     *
     * @param nric The NRIC of the patient for this appointment
     * @param dateTime The date and time of the appointment
     * @param description The description of the appointment
     */
    public Appointment(String nric, LocalDateTime dateTime, String description) {
        assert nric != null && !nric.isBlank() : "NRIC cannot be null or blank";
        assert dateTime != null : "DateTime cannot be null";
        assert description != null && !description.isBlank() : "Description cannot be null or blank";
        
        this.id = "A" + runningId++;
        this.nric = nric;
        this.dateTime = dateTime;
        this.description = description;
        this.isDone = false;
    }

    /**
     * Constructs an Appointment with a specific ID (used for loading from storage).
     *
     * @param id The predefined ID for the appointment
     * @param nric The NRIC of the patient
     * @param dateTime The date and time of the appointment
     * @param description The description of the appointment
     */
    public Appointment(String id, String nric, LocalDateTime dateTime, String description) {
        this.id = id;
        this.nric = nric;
        this.dateTime = dateTime;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    /**
     * Sets the running ID counter for new appointments.
     *
     * @param newId The new starting value for ID generation
     */
    public static void setRunningId(int newId) {
        runningId = newId;
    }

    public String getNric() {
        return nric;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public static int getRunningId() {
        return runningId;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Checks the completion status of the appointment.
     *
     * @return true if the appointment is completed, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void unmarkAsDone() {
        this.isDone = false;
    }

    /**
     * Sets the completion status of the appointment.
     *
     * @param mark true to mark as done, false to mark as not done
     */
    public void setIsDone(boolean mark) {
        this.isDone = mark;
    }

    /**
     * Gets the status icon of the appointment for display purposes.
     *
     * @return "X" if done, " " (space) if not done
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the formatted string representation of the appointment
     *
     * @return A formatted string showing appointment ID, status, NRIC, date/time and description
     */
    @Override
    public String toString() {
        return "[" + id + "]" + "[" + this.getStatusIcon() + "]" + " - "
                + nric + " - " + dateTime.format(OUTPUT_FORMAT) + " - " + description;
    }

    /**
     * Returns the string representation of the appointment in a file-friendly format (for storage).
     *
     * @return Pipe-delimited string containing all appointment's information
     */
    public String toFileFormat() {
        return id.substring(1) + "|" + this.isDone + "|" + this.nric + "|" +
                dateTime.format(OUTPUT_FORMAT) + "|" + this.description;
    }
}
