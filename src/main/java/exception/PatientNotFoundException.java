package exception;

/**
 * Represents an exception specific to a patient not being found in the system.
 * This exception is thrown when attempting to perform an operation on a patient
 * that does not exist in the database.
 */
public class PatientNotFoundException extends Exception {
    
    /**
     * Constructs a PatientNotFoundException with the specified detail message.
     *
     * @param message The detail message explaining which patient was not found
     */
    public PatientNotFoundException(String message) {
        super(message);
    }
}

