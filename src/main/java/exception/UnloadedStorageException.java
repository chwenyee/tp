package exception;

/**
 * Represents an exception specific to storage access failure.
 * This exception is thrown when the system fails to load or save data to/from storage,
 * typically due to file system issues or corrupt data.
 */
public class UnloadedStorageException extends Exception{
    
    /**
     * Constructs an UnloadedStorageException with the specified detail message.
     *
     * @param message The detail message explaining the storage failure
     */
    public UnloadedStorageException (String message) {
        super(message);
    }

}
