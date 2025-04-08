package exception;

/**
 * Represents an exception specific to duplicate patient IDs.
 * This exception is thrown when attempting to add a patient with an ID that already
 * exists in the system.
 */
public class DuplicatePatientIDException extends Exception {

    /**
     * Constructs a DuplicatePatientIDException with the specified detail message.
     *
     * @param message The detail message explaining the duplicate ID issue
     */
    public DuplicatePatientIDException(String message) {
        super(message);
    }
}
