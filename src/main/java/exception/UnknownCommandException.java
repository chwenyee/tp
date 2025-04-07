package exception;

/**
 * Represents an exception specific to the unknown user command.
 * This exception is thrown when the user enters a command that is not recognized
 * by the application.
 */
public class UnknownCommandException extends Exception{

    /**
     * Constructs an UnknownCommandException with the specified message.
     *
     * @param message The message indicating the command is invalid/unknown.
     */
    public UnknownCommandException (String message) {
        super(message);
    }

}
