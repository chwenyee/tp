package exception;

/**
 * Represents an exception specific to appointment clash.
 * This exception is thrown when attempting to schedule an appointment that conflicts with
 * an existing appointment in the system.
 */
public class AppointmentClashException extends Exception {

    /**
     * Constructs an AppointmentClashException with the specified detail message.
     *
     * @param message The detail message explaining the appointment conflict
     */
    public AppointmentClashException(String message) {
        super(message);
    }
}
