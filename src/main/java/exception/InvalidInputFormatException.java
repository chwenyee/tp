package exception;

/**
 * Represents an exception specific to the invalid user input format.
 * This exception is thrown when the user input does not match the expected format
 * for a command or parameter.
 */
public class InvalidInputFormatException extends Exception {

    /**
     * Constructs an InvalidInputFormatException with the specified detail message.
     *
     * @param message The detail message explaining the correct input format
     */
    public InvalidInputFormatException(String message) {
        super(message);
    }
}
