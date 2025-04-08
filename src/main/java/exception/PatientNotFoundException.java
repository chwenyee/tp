package exception;

/**
 * Exception thrown when a patient with the specified identifier is not found in the system.
 */
public class PatientNotFoundException extends Exception {

    /**
     * Constructs a new PatientNotFoundException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public PatientNotFoundException(String message) {
        super(message);
    }
}
